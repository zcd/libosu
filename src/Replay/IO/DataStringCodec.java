package Replay.IO;

import Constants.BitmaskEnum;
import Constants.KeyStroke;
import Replay.Moment;
import Replay.LifeBarSample;
import com.google.common.collect.ImmutableList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Library for encoding/decoding String types as used in osu! Replay files.
 * <p>
 * See <a href="https://osu.ppy.sh/wiki/Osr_(file_format)">reference page</a> for more detail.
 */
public final class DataStringCodec {
    /**
     * Decodes a list of values from a string.
     *
     * @param input  the encoded string.
     * @param parser function that parses values from the list elements.
     * @param <E>    type of the encoded elements.
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

    public static <E> String toEncodedString(List<E> items, Function<E, String> encoder) {
        return items
                .stream()
                .map(encoder)
                .collect(Collectors.joining(","));
    }

    /**
     * Decodes tuple values from a string.
     *
     * @param input  the encoded string.
     * @param parser function that parses the tuple value.
     * @param <E>    type of the encoded tuple.
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
            return LifeBarSample.create(u, v);
        });
    }

    public static String encodeLifeBarSample(LifeBarSample sample) {
        return String.format("%d|%f", sample.offsetMillis(), sample.lifeFraction());
    }

    /**
     * Decodes a Replay.Moment tuple value from a string.
     *
     * @param input a string with a single Moment tuple encoding.
     * @return the decoded tuple.
     */
    public static Moment parseMoment(String input) {
        return toTuple(input, (Scanner scanner) -> {
            long millis = scanner.nextLong();
            float x = scanner.nextFloat();
            float y = scanner.nextFloat();
            EnumSet<KeyStroke> keys = KeyStroke.fromMask(scanner.nextInt());

            return Moment.create(millis, x, y, keys);
        });
    }

    /**
     * Encodes a Replay.Moment tuple value into a string.
     * <p>
     * Preserves four decimal places in the x and y coordinate values.
     *
     * @param sample
     * @return
     */
    public static String encodeMoment(Moment sample) {
        return String.format(
                "%d|%.4f|%.4f|%d",
                sample.millisSincePrev(),
                sample.cursorX(),
                sample.cursorY(),
                BitmaskEnum.toMask(sample.keys()));
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
