package com.zerocooldown.libosu.replay.io;

import com.zerocooldown.libosu.common.BitmaskEnum;
import com.zerocooldown.libosu.common.KeyStroke;
import com.zerocooldown.libosu.replay.LifeBarSample;
import com.zerocooldown.libosu.replay.Moment;
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
 * Library for encoding/decoding String types as used in osu! com.zerocooldown.libosu.replay files.
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
     * @return list of parsed values.
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
     * Encodes a list of values to a osu! com.zerocooldown.libosu.replay string.
     *
     * @param items   list of items to encode.
     * @param encoder function to turn the item into a String.
     * @param <E>     type of the list items.
     * @return a osu! com.zerocooldown.libosu.replay string encoding of the items list.
     */
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
     * @return a parsed tuple from the encoded string.
     */
    public static <E> E toTuple(String input, Function<Scanner, E> parser) {
        try (Scanner scanner = new Scanner(input).useDelimiter("[|]")) {
            return parser.apply(scanner);
        }
    }

    /**
     * Decodes a com.zerocooldown.libosu.replay.LifeBarSample tuple value from a string.
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

    /**
     * Encodes a com.zerocooldown.libosu.replay.LifeBarSample tuple value into a string.
     * <p>
     * Preserves four decimal places in the x and y coordinate values.
     *
     * @param sample the lifebar sample instance to encode.
     * @return the string representation of the {@link LifeBarSample}
     */
    public static String encodeLifeBarSample(LifeBarSample sample) {
        return String.format("%d|%f", sample.offsetMillis(), sample.lifeFraction());
    }

    /**
     * Decodes a com.zerocooldown.libosu.replay.Moment tuple value from a string.
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
     * Encodes a com.zerocooldown.libosu.replay.Moment tuple value into a string.
     * <p>
     * Preserves four decimal places in the x and y coordinate values.
     *
     * @param moment the moment instance to encode.
     * @return the string representation of the {@link Moment}
     */
    public static String encodeMoment(Moment moment) {
        return String.format(
                "%d|%.4f|%.4f|%d",
                moment.millisSincePrev(),
                moment.cursor().x(),
                moment.cursor().y(),
                BitmaskEnum.toMask(moment.keys()));
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
