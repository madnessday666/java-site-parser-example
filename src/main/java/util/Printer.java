package util;

import model.BaseModel;
import model.category.Category;
import util.file_writer.FileFormat;

import java.util.List;

public class Printer {

    private Printer() {
    }

    private static final String delimiter =
            "+--------------------------------------------------------------------------------------------------+";

    public static void printList(List<? extends BaseModel> list, String prefix, String postfix) {
        StringBuilder stringBuilder = new StringBuilder();
        int size = list.size();
        int left;
        int right;
        if (size % 2 == 0) {
            right = left = size / 2;
        } else {
            left = size / 2 + 1;
            right = left - 1;
        }
        for (int i = 0; i < left; i++) {
            String titleLeft = list.get(i).getTitle();
            String ordinalNumberLeft = i + 1 + ") ";
            stringBuilder
                    .append(prefix)
                    .append(ordinalNumberLeft)
                    .append(titleLeft);
            if (i < right) {
                String titleRight = list.get(left + i).getTitle();
                String ordinalNumberRight = left + i + 1 + ") ";
                String spacing = " ".repeat(100 - 50 - titleLeft.length() - ordinalNumberLeft.length());
                String trailingSpace = " ".repeat(
                        100 -
                                prefix.length() -
                                ordinalNumberLeft.length() -
                                titleLeft.length() -
                                spacing.length() -
                                ordinalNumberRight.length() -
                                titleRight.length() -
                                postfix.length()
                );
                stringBuilder
                        .append(spacing)
                        .append(ordinalNumberRight)
                        .append(titleRight)
                        .append(trailingSpace);
            } else {
                String trailingSpace = " ".repeat(
                        100 -
                                prefix.length() -
                                ordinalNumberLeft.length() -
                                titleLeft.length() -
                                postfix.length()
                );
                stringBuilder.append(trailingSpace);
            }
            stringBuilder.append(postfix).append("\n");
        }
        System.out.print(stringBuilder);
    }

    public static void printHelp() {
        System.out.println("""
                USAGE:
                  parser.jar [OPTIONS]

                EXAMPLE:
                  parser.jar -c 1 -p 2                               Parses 2 pages of 1 category
                  parser.jar -c 1-3,^2 -p 1                          Parses 1 page from categories 1 to 3, excluding 2 category
                  parser.jar -u $CATEGORY_URL                        Parses category with specified url
                  parser.jar -c 1 -r Казань                          Parses 1 category in Казань region
                  parser.jar -l categories                           Prints list of categories
                  parser.jar -c 1 -f csv                             Parses 1 category and sets output file format to "csv"
                  parser.jar -c 1 -f html -o /home/user/files        Parses 1 category and saves file in "/home/user/files"

                OPTIONS:
                  -c, --category <category>            category number
                  -f, --format <format>                output file format
                  -h, --help                           print help
                  -l, --list [categories | regions]    print specified list
                  -p, --pages <pages>                  number of pages
                  -o, --output <directory>             output directory
                  -r, --region <region>                region name
                  -u, --url <url>                      category url
                """);
    }

    public static void printInfo(List<Category> categories,
                                 String region,
                                 int pages,
                                 FileFormat format) {
        System.out.printf("%s\n|Выбранные категории:%s|%n", delimiter, " ".repeat(78));
        printList(categories, "|  ", "|");
        System.out.printf(
                "%s%s%s%s%s%s%s\n\n",
                delimiter,
                "\n|Регион: " + region + " ".repeat(100 - 9 - region.length() - 1) + "|\n",
                delimiter,
                "\n|Количество страниц: " + pages + " ".repeat(100 - 21 - String.valueOf(pages).length() - 1) + "|\n",
                delimiter,
                "\n|Формат конечного файла: " + format + " ".repeat(100 - 25 - format.toString().length() - 1) + "|\n"
                , delimiter
        );
    }

}
