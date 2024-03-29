package parser;

import config.WebDriverConfiguration;
import lombok.Getter;
import lombok.Setter;
import model.category.Category;
import model.product.Product;
import model.region.Region;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.json.Json;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

import static element.ElementAttribute.*;
import static element.ElementAttributeValue.*;
import static element.ElementTag.*;

@Getter
@Setter
public class PageParser {

    private final WebDriverConfiguration configuration;

    private List<Category> categories = new ArrayList<>();
    private List<Region> regions = new ArrayList<>();
    private List<Product> products = new ArrayList<>();

    private String defaultRegion;

    public PageParser(WebDriverConfiguration configuration) {
        this.configuration = configuration;
    }

    private boolean hasProducts(Document categoryPage) {
        return !categoryPage
                .select(
                        "%s[%s~=%s]".formatted(
                                DIV,
                                DATA_META_NAME,
                                PRODUCT
                        ))
                .select(
                        "%s[%s~=%s]".formatted(
                                A,
                                DATA_META_NAME,
                                SNIPPET_TITLE
                        ))
                .attr(TITLE)
                .isEmpty();
    }

    private boolean hasSubcategories(Element element) {
        Elements subcategories = element.select(
                "%s[%s~=%s]".formatted(
                        DIV,
                        DATA_META_NAME,
                        CATEGORY_CARDS
                )
        );
        return !subcategories.isEmpty();
    }

    private String parseAvailability(Element element) {
        Elements availability = element.select(
                "%s[%s~=%s]".formatted(
                        BUTTON,
                        DATA_META_NAME,
                        AVAILABLE_NOW
                ));
        if (availability.isEmpty()) {
            return "-";
        }
        return availability.text();
    }

    private String parseBadges(Element element) {
        List<String> badgesList = element
                .select(
                        "%s[%s~=%s]".formatted(
                                DIV,
                                DATA_META_NAME,
                                BADGES_WRAPPER
                        ))
                .select(
                        "%s".formatted(
                                SPAN
                        ))
                .stream()
                .map(Element::text)
                .distinct()
                .toList();
        if (badgesList.isEmpty()) {
            return "-";
        }
        return String.join(", ", badgesList);
    }

    public void parseCategory(String categoryUrl, String regionUrl) {
        try {
            Element element = Jsoup.connect(categoryUrl + regionUrl).followRedirects(true).get().body();
            String data = element.select(
                    "%s[%s~=%s]".formatted(
                            SCRIPT,
                            ID,
                            NEXT_DATA
                    )).toString();
            String categoryName = data.contains("portalName") ?
                    data.substring(
                            data.indexOf("\"portalName\":\"") + "\"portalName\":\"".length(),
                            data.indexOf(",\"visitorCityId\"") - 1
                    ) :
                    data.substring(
                            data.indexOf("\"categoryName\":\"") + "\"categoryName\":\"".length(),
                            data.indexOf(",\"categoryId\"") - 1
                    );
            this.setCategories(List.of(new Category(categoryName, categoryUrl)));
        } catch (IOException e) {
            System.out.println("Произошла ошибка при обработке запроса.\n" + e.getMessage());
            System.exit(1);
        }
    }

    public void parseCategories(String regionUrl) {
        try {
            Element element = Jsoup
                    .connect("https://www.citilink.ru/catalog/" + regionUrl)
                    .followRedirects(true)
                    .get()
                    .body();
            Elements categoryElements = element.select(
                    "%s[%s~=%s]".formatted(
                            DIV,
                            CLASS,
                            CATEGORY_WRAPPER
                    )
            );
            for (Element category : categoryElements) {
                this.categories.add(
                        Category
                                .builder()
                                .title(category.select("%s > %s".formatted(H4, A)).text())
                                .url(category.select("%s > %s".formatted(H4, A)).attr(HREF))
                                .build()
                );
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка при обработке запроса.\n" + e.getMessage());
            System.exit(1);
        }
    }

    public List<Product> parseCategoryProducts(List<Category> categories, String regionUrl, int pages) {
        for (Category category : categories) {
            this.parseCategoryProductPages(category, regionUrl, pages);
        }
        return products;
    }

    public void parseCategoryProductPages(Category category, String regionUrl, int pages) {
        boolean isPagesModified = false;
        for (int i = 1; i <= pages; i++) {
            System.out.printf(
                    "\rСбор информации о категории \"%s\", стр. %d %s",
                    category.getTitle(),
                    i,
                    " ".repeat(40)
            );
            WebDriver driver = configuration.getWebDriver();
            String categoryUrl = category.getUrl() + regionUrl + "&p=" + i;
            try {
                driver.get(categoryUrl);
            } catch (TimeoutException ignored) {
            } finally {
                Document categoryPage = Jsoup.parse(driver.getPageSource());
                if (!hasProducts(categoryPage) && !hasSubcategories(categoryPage)) {
                    categoryPage = this.retry(categoryUrl, category.getTitle(), i);
                }
                if (hasSubcategories(categoryPage)) {
                    this.parseCategoryProducts(this.parseSubcategories(categoryPage), regionUrl, pages);
                }
                if (!isPagesModified) {
                    isPagesModified = true;
                    pages = Math.min(pages, this.parsePages(categoryPage));
                }
                this.parseProducts(categoryPage);
            }
            driver.close();
        }
    }

    private int parseComments(Element element) {
        Elements comments = element.select(
                "%s[%s~=%s]".formatted(
                        DIV,
                        DATA_META_NAME,
                        COMMENT
                ));
        if (comments.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(comments.text());
    }

    public void parseDefaultRegion() {
        try {
            Element element = Jsoup
                    .connect("https://www.citilink.ru/catalog/")
                    .get()
                    .body();
            this.defaultRegion = Objects.requireNonNull(
                    element.selectFirst(
                            "%s[%s=%s]".formatted(
                                    DIV,
                                    CLASS,
                                    CITY
                            ))
            ).text();
        } catch (IOException e) {
            System.out.println("Произошла ошибка при обработке запроса.\n" + e.getMessage());
            System.exit(1);
        }
    }

    private String parseDeliverability(Element element) {
        Elements deliverability = element.select(
                "%s[%s~=%s]".formatted(
                        BUTTON,
                        DATA_META_NAME,
                        AVAILABLE_TO_DELIVERY
                ));
        if (deliverability.isEmpty()) {
            return "-";
        }
        return deliverability.text();
    }

    private String parseDeliveryTime(Element element) {
        Elements deliverability = element.select(
                "%s[%s~=%s]".formatted(
                        BUTTON,
                        DATA_META_NAME,
                        AVAILABLE_TO_DELIVERY
                ));
        if (deliverability.isEmpty()) {
            return "-";
        } else {
            if (deliverability.next().isEmpty()) {
                return "-";
            }
        }
        return deliverability.next().text().replaceAll("[^a-zA-Zа-яА-Я0-9\\s]", "");
    }

    private int parsePages(Element element) {
        String pages = element.select(
                        "%s[%s=%s]".formatted(
                                DIV,
                                DATA_META_NAME,
                                PAGINATION
                        ))
                .select(A)
                .text()
                .replaceAll("\\D\\W", "");
        if (pages.isEmpty()) {
            return 1;
        } else {
            return Arrays
                    .stream(pages.split(" "))
                    .map(Integer::parseInt)
                    .max(Integer::compareTo)
                    .orElse(1);
        }
    }

    private int parsePrice(Element element) {
        Elements price = element.select(
                "%s[%s]".formatted(
                        SPAN,
                        DATA_META_PRICE
                ));
        if (price.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(price.attr(DATA_META_PRICE));
    }

    public void parseProducts(Element categoryPage) {
        categoryPage.select(
                        "%s[%s~=%s]".formatted(
                                DIV,
                                DATA_META_NAME,
                                PRODUCT
                        ))
                .forEach(element ->
                        this.products.add(
                                Product
                                        .builder()
                                        .availableIn(this.parseAvailability(element))
                                        .badges(this.parseBadges(element))
                                        .comments(this.parseComments(element))
                                        .deliveryIn(this.parseDeliveryTime(element))
                                        .deliveryTo(this.parseDeliverability(element))
                                        .price(this.parsePrice(element))
                                        .rating(this.parseRating(element))
                                        .title(this.parseTitle(element))
                                        .url(this.parseUrl(element))
                                        .build()
                        )
                );
    }

    private double parseRating(Element element) {
        Elements rating = element.select(
                "%s[%s~=%s]".formatted(
                        DIV,
                        DATA_META_NAME,
                        RATING
                ));
        if (rating.isEmpty()) {
            return 0.0;
        }
        return Double.parseDouble(rating.text());
    }

    public void parseRegions() {
        try {
            Element element = Jsoup
                    .connect("https://www.citilink.ru/amp/catalog/")
                    .followRedirects(true)
                    .get()
                    .body();
            String data = element
                    .select(
                            "%s[%s=%s]".formatted(
                                    SCRIPT,
                                    TYPE,
                                    APPLICATION_JSON
                            ))
                    .stream()
                    .max(Comparator.comparingInt(e -> e.data().length()))
                    .get()
                    .data();
            Json json = new Json();
            Map<String, List<Map<String, String>>> mappedData = json.toType(data, Map.class);
            for (Map<String, String> o : mappedData.get("items")) {
                this.regions.add(new Region(o.get("city"), o.get("url").substring(9)));
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка при обработке запроса.\n" + e.getMessage());
            System.exit(1);
        }
    }

    private List<Category> parseSubcategories(Element element) {
        List<Category> subcategories = new ArrayList<>();
        Elements subcategoryElements = element
                .select(
                        "%s[%s~=%s]".formatted(
                                DIV,
                                DATA_META_NAME,
                                CATEGORY_CARDS
                        ))
                .select(A);
        for (Element subcategory : subcategoryElements) {
            if (!subcategory.text().isEmpty()) {
                String subcategoryTitle = subcategory.text();
                String subcategoryUrl = "https://www.citilink.ru" + subcategory.attr(HREF);
                subcategories.add(
                        Category
                                .builder()
                                .title(subcategoryTitle)
                                .url(subcategoryUrl)
                                .build()
                );
            }
        }
        return subcategories;
    }

    private String parseTitle(Element element) {
        String title = element.select(
                "%s[%s]".formatted(
                        A,
                        HREF
                ))
                .attr(TITLE)
                .replaceAll("\\[.*]", "");
        if (title.isEmpty()) {
            return element.select(
                            "%s[%s] > %s".formatted(
                                    A,
                                    HREF,
                                    SPAN
                            ))
                    .text();
        }
        return title;
    }

    private String parseUrl(Element element) {
        return "https://www.citilink.ru" + element.select(
                "%s[%s]".formatted(
                        A,
                        HREF
                )).attr(HREF);
    }

    @SuppressWarnings({"all"})
    public Document retry(String url, String title, int page) {
        System.out.printf(
                "\r[RETRY] Сбор информации о категории \"%s\", стр. %d %s",
                title,
                page,
                " ".repeat(30)
        );
        WebDriver driver = configuration.getWebDriver();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(15));
        try {
            driver.get(url);
        } catch (TimeoutException ignored) {
        } finally {
            Document document = Jsoup.parse(driver.getPageSource());
            if (!hasProducts(document) && !hasSubcategories(document)) {
                System.out.printf(
                        "\n\nПроизошла ошибка при попытке получении инфорации о категории \"%s\", стр. %d",
                        title,
                        page
                );
                System.exit(1);
            }
            driver.close();
            return document;
        }
    }

}
