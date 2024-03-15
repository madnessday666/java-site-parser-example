package util.file_writer;

import model.product.Product;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import static util.file_writer.FileFormat.html;

public class HtmlFileWriter extends FileWriter {

    public HtmlFileWriter() {
        super();
        super.setFormat(html);
    }

    @Override
    public void writeToFile(String pathToFile, List<Product> products) {
        StringBuilder body = new StringBuilder();
        int counter = 1;
        body
                .append("<html><body>")
                .append("<table border=\"1\"><thead  align=\"center\"><tr>")
                .append("<td>№</td>")
                .append("<td>Наименование</td>")
                .append("<td>Цена</td>")
                .append("<td>Рейтинг</td>")
                .append("<td>Отзывы</td>")
                .append("<td>В наличии</td>")
                .append("<td>Доставка</td>")
                .append("<td>Срок доставки</td>")
                .append("<td>Доп. информация</td>")
                .append("<td>Ссылка</td>")
                .append("</tr></thead><tbody  align=\"center\">");
        for (Product product : products) {
            body
                    .append("<tr>")
                    .append("<td>%s</td>".formatted(counter++))
                    .append("<td  align=\"left\">%s</td>".formatted(product.getTitle()))
                    .append("<td>%s</td>".formatted(product.getPrice()))
                    .append("<td>%s</td>".formatted(product.getRating()))
                    .append("<td>%s</td>".formatted(product.getComments()))
                    .append("<td>%s</td>".formatted(product.getAvailableIn()))
                    .append("<td>%s</td>".formatted(product.getDeliveryTo()))
                    .append("<td>%s</td>".formatted(product.getDeliveryIn()))
                    .append("<td>%s</td>".formatted(product.getBadges()))
                    .append("<td  align=\"left\">%s</td>".formatted(product.getUrl()))
                    .append("</tr>");
        }
        body.append("</table></body></html>");
        try (PrintWriter printWriter = new PrintWriter(pathToFile)) {
            printWriter.write(body.toString());
            System.out.println("Отчет успешно сформирован!\nПуть до файла: " + pathToFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
