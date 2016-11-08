package Replay.Parse;

import com.google.common.collect.ImmutableList;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public final class DataParsing {
    public static <E> List<E> toList(InputStream input, Function<String, E> parser) {
        try (Scanner scanner = new Scanner(input)) {
            return listFromScanner(parser, scanner);
        }
    }

    public static <E> List<E> toList(String input, Function<String, E> parser) {
        try (Scanner scanner = new Scanner(input)) {
            return listFromScanner(parser, scanner);
        }
    }

    public static <E> E toTuple(String input, Function<Scanner, E> parser) {
        try (Scanner scanner = new Scanner(input).useDelimiter("[|]")) {
            return parser.apply(scanner);
        }
    }

    private static <E> List<E> listFromScanner(Function<String, E> parser, Scanner scanner) {
        scanner.useDelimiter(",");
        ImmutableList.Builder<E> builder = ImmutableList.builder();
        while (scanner.hasNext()) {
            builder.add(parser.apply(scanner.next()));
        }
        return builder.build();
    }
}
