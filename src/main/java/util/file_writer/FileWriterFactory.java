package util.file_writer;

public class FileWriterFactory {

    public static FileWriter createFileWriter(FileFormat format) {
        switch (format) {
            case html -> {
                return new HtmlFileWriter();
            }
            case csv -> {
                return new CsvFileWriter();
            }
        }
        return new HtmlFileWriter();
    }

}
