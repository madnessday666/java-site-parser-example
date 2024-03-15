import config.WebDriverConfiguration;
import model.category.Category;
import model.product.Product;
import parser.PageParser;
import util.DataAdjuster;
import util.Printer;
import util.command_line.CommandLineArgumentResolver;
import util.file_writer.FileFormat;
import util.file_writer.FileWriter;
import util.file_writer.FileWriterFactory;

import java.io.File;
import java.util.List;
import java.util.Map;

public class JavaSiteParser {

    public static void main(String[] args) throws InterruptedException {
        WebDriverConfiguration configuration = new WebDriverConfiguration();
        PageParser parser = new PageParser(configuration);
        CommandLineArgumentResolver resolver = new CommandLineArgumentResolver(parser);
        Map<String, String> params = resolver.resolve(args);
        DataAdjuster dataAdjuster = new DataAdjuster(parser, params);

        while (parser.getRegions().isEmpty() || parser.getCategories().isEmpty() || parser.getDefaultRegion() == null) {
            Thread.sleep(100);
        }

        String region = dataAdjuster.adjustRegion();
        String regionUrl = dataAdjuster.adjustRegionUrl(region);
        List<Category> categories = dataAdjuster.adjustCategories(regionUrl);
        int pages = dataAdjuster.adjustPages();
        int timeout = dataAdjuster.adjustTimeout();
        FileFormat format = dataAdjuster.adjustFormat();
        String outputDir = dataAdjuster.adjustOutput();
        configuration.setTimeout(timeout);

        Printer.printInfo(categories, region, pages, timeout, format);

        FileWriter fileWriter = FileWriterFactory.createFileWriter(format);
        File outputFile = fileWriter.createFile(outputDir, region);
        List<Product> products = parser.parseProducts(categories, pages, regionUrl);
        System.out.printf("\rСбор информации завершен! Формирование отчета...%s\n\n", " ".repeat(20));
        fileWriter.writeToFile(outputFile.getAbsolutePath(), products);
        System.exit(0);
    }

}
