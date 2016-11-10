package Replay.IO;

import Constants.BitmaskEnum;
import Constants.KeyStroke;
import Replay.Action;
import Replay.LifeBarSample;
import com.google.common.collect.ImmutableList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public final class StringDecode {
    public static <E> List<E> toList(InputStream input, Function<String, E> parser) {
        try (Scanner scanner = new Scanner(input)) {
            return listFromScanner(parser, scanner);
        }
    }

    public static <E> List<E> toList(String input, Function<String, E> parser) {
        return toList(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)), parser);
    }

    public static <E> E toTuple(String input, Function<Scanner, E> parser) {
        try (Scanner scanner = new Scanner(input).useDelimiter("[|]")) {
            return parser.apply(scanner);
        }
    }

    public static LifeBarSample parseLifeBarSample(String input) {
        return toTuple(input, (Scanner scanner) -> {
            long u = scanner.nextLong();
            float v = scanner.nextFloat();
            return new LifeBarSample(u, v);
        });
    }

    public static Action parseAction(String input) {
        return toTuple(input, (Scanner scanner) -> {
            long millis = scanner.nextLong();
            float x = scanner.nextFloat();
            float y = scanner.nextFloat();
            EnumSet<KeyStroke> keys = BitmaskEnum.fromMask(KeyStroke.class, scanner.nextInt());

            return new Action(millis, x, y, keys);
        });
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
