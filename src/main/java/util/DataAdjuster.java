package util;

import model.category.Category;
import model.region.Region;
import parser.PageParser;
import util.file_writer.FileFormat;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static util.command_line.CommandLineArgument.*;

public class DataAdjuster {

    private final PageParser parser;
    private final Map<String, String> params;

    public DataAdjuster(PageParser parser, Map<String, String> params) {
        this.parser = parser;
        this.params = params;
    }

    public List<Category> adjustCategories(String regionUrl) {
        List<Category> adjustedList;
        if (!params.containsKey(ARGUMENT_URL)) {
            parser.parseCategories(regionUrl);
            adjustedList = this.adjustList(
                    this.parser.getCategories(),
                    this.params.getOrDefault(
                            ARGUMENT_CATEGORY,
                            "1"
                    )
            );
        } else {
            parser.parseCategory(params.get(ARGUMENT_URL), regionUrl);
            adjustedList = parser.getCategories();
        }
        adjustedList.forEach(category -> {
            if (!category.getUrl().endsWith("/")){
                category.setUrl(category.getUrl()+"/");
            }
        });
        return adjustedList;
    }

    public <T> List<T> adjustList(List<T> list, String param) {
        String[] params = param.split(",");
        List<T> adjustedList = new ArrayList<>();
        for (String p : params) {
            if (p.matches("[1-9]([0-9]+)?+")) {
                int indexToAdd = Integer.parseInt(p) - 1;
                adjustedList.add(list.get(indexToAdd));

            } else if (p.matches("\\^[1-9]([0-9]+)?+")) {
                int indexToRemove = Integer.parseInt(p.replace("^", "")) - 1;
                adjustedList.remove(list.get(indexToRemove));

            } else if (p.matches("[1-9]([0-9]+)?+-[1-9]([0-9]+)?+")) {
                String[] split = p.split("-");
                int[] range = new int[]{Integer.parseInt(split[0]), Integer.parseInt(split[1])};
                int from = Math.min(range[0], range[1]) - 1;
                int to = Math.min(Math.max(range[0], range[1]), list.size());
                adjustedList.addAll(list.subList(from, to));

            } else if (p.matches("\\^[1-9]([0-9]+)?+-[1-9]([0-9]+)?+")) {
                String[] split = p.split("-");
                int[] range = new int[]{Integer.parseInt(split[0].replace("^", "")), Integer.parseInt(split[1])};
                int from = Math.min(range[0], range[1]) - 1;
                int to = Math.min(Math.max(range[0], range[1]), list.size());
                adjustedList.removeAll(list.subList(from, to));
            }
        }
        return adjustedList;
    }

    public FileFormat adjustFormat() {
        return FileFormat.valueOf(this.params.getOrDefault(ARGUMENT_FORMAT, "html").toLowerCase());
    }

    public String adjustOutput() {
        return this.params.getOrDefault(ARGUMENT_OUTPUT, Paths.get("").toAbsolutePath() + "/");
    }

    public int adjustPages() {
        return Integer.parseInt(this.params.getOrDefault(ARGUMENT_PAGE, "1"));
    }

    public String adjustRegion() {
        String region = params.getOrDefault(ARGUMENT_REGION, this.parser.getDefaultRegion());
        for (Region r : this.parser.getRegions()) {
            if (r.getTitle().equalsIgnoreCase(region)) {
                return r.getTitle();
            }
        }
        return this.parser.getDefaultRegion();
    }

    public String adjustRegionUrl(String region) {
        String regionUrl = "";
        for (Region r : this.parser.getRegions()) {
            if (r.getTitle().equalsIgnoreCase(region)) {
                region = r.getTitle();
                regionUrl = r.getUrl();
            }
        }
        return regionUrl;
    }

    public int adjustTimeout() {
        return Integer.parseInt(params.getOrDefault(ARGUMENT_TIMEOUT, "5"));
    }

}
