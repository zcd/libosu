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


/**
 * Library for encoding/decoding String types as used in osu! Replay files.
 *
 * See <a href="https://osu.ppy.sh/wiki/Osr_(file_format)">reference page</a> for more detail.
 */
public final class DataStringCodec {
    /**
     * Decodes a list of values from a string.
     *
     * @param input the encoded string.
     * @param parser function that parses values from the list elements.
     * @param <E> type of the encoded elements.
     * @return
     */
    public static <E> List<E> toList(InputStream input, Function<String, E> parser) {
        try (Scanner scanner = new Scanner(input)) {
            return listFromScanner(parser, scanner);
        }
    }

    public static <E> List<E> toList(String input, Function<String, E> parser) {
        return toList(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)), parser);
    }

    /**
     * Decodes tuple values from a string.
     *
     * @param input the encoded string.
     * @param parser function that parses the tuple value.
     * @param <E> type of the encoded tuple.
     * @return
     */
    public static <E> E toTuple(String input, Function<Scanner, E> parser) {
        try (Scanner scanner = new Scanner(input).useDelimiter("[|]")) {
            return parser.apply(scanner);
        }
    }

    /**
     * Decodes a Replay.LifeBarSample tuple value from a string.
     *
     * @param input a string with a single LifeBarSample tuple encoding.
     * @return the decoded tuple.
     */
    public static LifeBarSample parseLifeBarSample(String input) {
        return toTuple(input, (Scanner scanner) -> {
            long u = scanner.nextLong();
            float v = scanner.nextFloat();
            return new LifeBarSample(u, v);
        });
    }

    /**
     * Decodes a Replay.Action tuple value from a string.
     *
     * @param input a string with a single Action tuple encoding.
     * @return the decoded tuple.
     */
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
