package util.command_line;

import parser.PageParser;
import util.Printer;
import util.file_writer.FileFormat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static util.command_line.CommandLineArgument.*;

public class CommandLineArgumentResolver {

    private final PageParser parser;

    public CommandLineArgumentResolver(PageParser parser) {
        this.parser = parser;
        CompletableFuture.runAsync(() -> {
            parser.parseCategories("");
            parser.parseRegions();
            parser.parseDefaultRegion();
        });
    }

    public Map<String, String> resolve(String[] args) {
        if (args.length == 0) {
            Printer.printHelp();
            System.exit(0);
        }
        Map<String, String> params = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            this.checkArgumentValue(args, i);
            params.put(args[i], args[i + 1]);
            i++;
        }
        if (params.containsKey(ARGUMENT_URL)) {
            params.remove(ARGUMENT_CATEGORY);
        }
        return params;
    }

    private void checkArgumentValue(String[] arguments, int currentArgIndex) {
        String argument = arguments[currentArgIndex];
        try {
            switch (argument) {
                case ARGUMENT_CATEGORY, ARGUMENT_CATEGORY_SHORT -> {
                    if (!arguments[currentArgIndex + 1].matches("\\^?[1-9]([0-9]+)?+(,\\^?[1-9]([0-9]+)?|\\^?-[1-9]([0-9]+)?)*")) {
                        System.out.printf(
                                "Некорректное значение параметра: \"%s\" -> \"%s\"",
                                argument,
                                arguments[currentArgIndex + 1]
                        );
                        System.exit(1);
                    }
                    arguments[currentArgIndex] = ARGUMENT_CATEGORY;
                }

                case ARGUMENT_HELP, ARGUMENT_HELP_SHORT -> {
                    Printer.printHelp();
                    System.exit(0);
                }

                case ARGUMENT_FORMAT, ARGUMENT_FORMAT_SHORT -> {
                    try {
                        arguments[currentArgIndex + 1] = arguments[currentArgIndex + 1].toLowerCase();
                        FileFormat.valueOf(arguments[currentArgIndex + 1].toLowerCase());
                    } catch (IllegalArgumentException e) {
                        System.out.printf(
                                "Некорректное значение параметра: \"%s\" -> \"%s\"",
                                argument,
                                arguments[currentArgIndex + 1]
                        );
                        System.exit(1);
                    }
                    arguments[currentArgIndex] = ARGUMENT_FORMAT;
                }

                case ARGUMENT_LIST, ARGUMENT_LIST_SHORT -> {
                    if (!arguments[currentArgIndex + 1].equalsIgnoreCase("categories") &&
                            !arguments[currentArgIndex + 1].equalsIgnoreCase("regions")) {
                        System.out.printf(
                                "Некорректное значение параметра: \"%s\" -> \"%s\"",
                                argument,
                                arguments[currentArgIndex + 1]
                        );
                        System.exit(1);
                    }
                    while (parser.getCategories().isEmpty() || parser.getRegions().isEmpty()) {
                        Thread.sleep(100);
                    }
                    switch (arguments[currentArgIndex + 1]) {
                        case "categories" -> Printer.printList(parser.getCategories(), "", "");
                        case "regions" -> Printer.printList(parser.getRegions(), "", "");
                    }
                    System.exit(0);
                }

                case ARGUMENT_OUTPUT, ARGUMENT_OUTPUT_SHORT -> {
                    if (!arguments[currentArgIndex + 1].endsWith("/")) {
                        arguments[currentArgIndex + 1] += "/";
                    }
                    if (!Files.isDirectory(Path.of(arguments[currentArgIndex + 1]))) {
                        System.out.printf(
                                "Некорректное значение параметра: \"%s\" -> \"%s\"",
                                argument,
                                arguments[currentArgIndex + 1]
                        );
                        System.exit(1);
                    }
                    arguments[currentArgIndex] = ARGUMENT_OUTPUT;
                }

                case ARGUMENT_PAGE, ARGUMENT_PAGE_SHORT -> {
                    if (!arguments[currentArgIndex + 1].matches("^[1-9]([0-9]+)?")) {
                        System.out.printf(
                                "Некорректное значение параметра: \"%s\" -> \"%s\"",
                                argument,
                                arguments[currentArgIndex + 1]
                        );
                        System.exit(1);
                    }
                    arguments[currentArgIndex] = ARGUMENT_PAGE;
                }

                case ARGUMENT_REGION, ARGUMENT_REGION_SHORT -> {
                    if (!arguments[currentArgIndex + 1].matches("[а-яА-Я]+")) {
                        System.out.printf(
                                "Некорректное значение параметра: \"%s\" -> \"%s\"",
                                argument,
                                arguments[currentArgIndex + 1]
                        );
                        System.exit(1);
                    }
                    arguments[currentArgIndex] = ARGUMENT_REGION;
                }

                case ARGUMENT_URL, ARGUMENT_URL_SHORT -> {
                    if (!arguments[currentArgIndex + 1].startsWith("https://www.citilink.ru")) {
                        System.out.printf(
                                "Некорректное значение параметра: \"%s\" -> \"%s\"",
                                argument,
                                arguments[currentArgIndex + 1]
                        );
                        System.exit(1);
                    }
                    arguments[currentArgIndex] = ARGUMENT_URL;
                }

                default -> {
                    System.out.printf("Неизвестный параметр \"%s\"", argument);
                    System.exit(1);
                }
            }
        } catch (ArrayIndexOutOfBoundsException | InterruptedException e) {
            System.out.printf("Отсутствует значение для параметра \"%s\"", argument);
            System.exit(1);
        }

    }

}
