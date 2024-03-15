package util.file_writer;

import model.product.Product;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import static util.file_writer.FileFormat.csv;

public class CsvFileWriter extends FileWriter {

    public CsvFileWriter() {
        super();
        super.setFormat(csv);
    }

    @Override
    public void writeToFile(String pathToFile, List<Product> data) {
        StringBuilder body = new StringBuilder();
        body
                .append("\"").append("Наименование").append("\",")
                .append("\"").append("Цена").append("\",")
                .append("\"").append("Рейтинг").append("\",")
                .append("\"").append("Отзывы").append("\",")
                .append("\"").append("В наличии").append("\",")
                .append("\"").append("Доставка").append("\",")
                .append("\"").append("Срок доставки").append("\",")
                .append("\"").append("Доп. информация").append("\",")
                .append("\"").append("Ссылка").append("\"\n");
        for (Product product : data) {
            body
                    .append("\"").append(product.getTitle()).append("\",")
                    .append(product.getPrice()).append(",")
                    .append(product.getRating()).append(",")
                    .append(product.getComments()).append(",")
                    .append("\"").append(product.getAvailableIn()).append("\",")
                    .append("\"").append(product.getDeliveryTo()).append("\",")
                    .append("\"").append(product.getDeliveryIn()).append("\",")
                    .append("\"[").append(product.getBadges()).append("]\",")
                    .append("\"").append(product.getUrl()).append("\"\n");
        }
        try (PrintWriter printWriter = new PrintWriter(pathToFile)) {
            printWriter.write(body.toString());
            System.out.println("Отчет успешно сформирован!\nПуть до файла: " + pathToFile);
        } catch (FileNotFoundException e) {
            System.out.println("Возникла ошибка во время формирования отчета");
            System.exit(1);
        }
    }


}
