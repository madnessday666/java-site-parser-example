package util.file_writer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.product.Product;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public abstract class FileWriter {

    private FileFormat format;

    public File createFile(String pathToDir, String region){
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("yyyy-dd-MM_HH:mm:ss"));
        String fileName = region.replaceAll(" ", "_") + "_" + timestamp + ".%s".formatted(this.format);
        File dir = new File(pathToDir);
        try {
            if (dir.exists() && dir.isDirectory()) {
                File output = new File(pathToDir + fileName);
                output.createNewFile();
                return output;
            } else {
                System.out.printf("\nНе удалось создать конечный файл\nПроверьте указанный путь: %s", pathToDir);
                System.exit(1);
            }
        } catch (IOException e) {
            System.out.printf("\nНе удалось создать конечный файл\nПроверьте указанный путь: %s", pathToDir);
            System.exit(1);
        }
        return new File("");
    };

    abstract public void writeToFile(String pathToFile, List<Product> data);

}
