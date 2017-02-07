package com.zerocooldown.libosu.beatmap.io;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Streams;
import com.zerocooldown.libosu.beatmap.Beatmap;
import com.zerocooldown.libosu.beatmap.datatypes.Colour;
import com.zerocooldown.libosu.beatmap.datatypes.HitObject;
import com.zerocooldown.libosu.beatmap.datatypes.TimingPoint;
import com.zerocooldown.libosu.beatmap.section.Difficulty;
import com.zerocooldown.libosu.beatmap.section.Editor;
import com.zerocooldown.libosu.beatmap.section.Events;
import com.zerocooldown.libosu.beatmap.section.General;
import com.zerocooldown.libosu.beatmap.section.Metadata;
import com.zerocooldown.libosu.constants.GameMode;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;

public class BeatmapReader {

    private static final Splitter COMMA_SPLITTER = Splitter.on(',').omitEmptyStrings();
    private static final Splitter PIPE_SPLITTER = Splitter.on('|');
    private static final Splitter SPACE_SPLITTER = Splitter.on(' ');

    /**
     * Parses a {@link Beatmap} instance from the input data.
     * <p>
     * See <a href="https://osu.ppy.sh/wiki/Osu_%28file_format%29#Sections">osu! wiki page</a> for the full file spec.
     *
     * @param source an {@link InputStream} containing an entire osu! beatmap file.
     * @return a parsed {@link Beatmap} instance.
     * @throws IOException if an exception occurs when reading the stream.
     */
    public static Beatmap read(InputStream source) {
        Scanner scanner = new Scanner(source);

        Beatmap.Builder builder = Beatmap.builder();

        builder.osuFormatVersion(scanner.nextLine());

        String line;
        while (!(line = skipEmptyLines(scanner).orElse("")).equals("")) {
            switch (line.substring(1, line.length() - 1)) {
                case "General":
                    builder.general(parseGeneral(scanner));
                    break;
                case "Editor":
                    builder.editor(parseEditor(scanner));
                    break;
                case "Metadata":
                    builder.metadata(parseMetadata(scanner));
                    break;
                case "Difficulty":
                    builder.difficulty(parseDifficulty(scanner));
                    break;
                case "Events":
                    builder.events(parseEvents(scanner));
                    break;
                case "TimingPoints":
                    builder.timingPoints(parseTimingPoints(scanner));
                    break;
                case "Colours":
                    builder.colours(parseColours(scanner));
                    break;
                case "HitObjects":
                    builder.hitObjects(parseHitObjects(scanner));
                    break;
                default:
                    throw new IllegalStateException("Encountered unrecognized header: " + line);
            }
        }

        return builder.build();
    }

    private static Optional<String> skipEmptyLines(Scanner scanner) {
        String line;
        do {
            if (!scanner.hasNextLine()) {
                return Optional.empty();
            }
            line = scanner.nextLine();
        } while (line.equals(""));
        return Optional.of(line);
    }

    private static void processNonEmptyLines(Consumer<String> processor, Scanner scanner) {
        String line;
        while (scanner.hasNextLine() && !(line = scanner.nextLine()).equals("")) {
            processor.accept(line);
        }
    }

    private static Map<String, String> readKeyValueSection(Scanner scanner) {
        return readKeyValueSection(Function.identity(), Function.identity(), scanner);
    }

    private static <K, V> Map<K, V> readKeyValueSection(
            Function<String, K> keyProcessor, Function<String, V> valueProcessor, Scanner scanner) {
        ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
        processNonEmptyLines(
                (String line) -> {
                    String[] parts = line.split("\\s*:\\s*", 2);
                    builder.put(keyProcessor.apply(parts[0]), valueProcessor.apply(parts[1]));
                }, scanner);
        return builder.build();
    }

    private static <K, V> Optional<V> maybeGet(Map<K, V> map, K key) {
        if (map.containsKey(key)) {
            return Optional.of(map.get(key));
        }
        return Optional.empty();
    }

    private static <K, V> V mustGet(Map<K, V> map, K key, String mapName) {
        Preconditions.checkState(
                map.containsKey(key), String.format("Could not find '%s' in '%s'.", map, mapName));
        return map.get(key);
    }

    private static <R> List<R> readListSection(Function<String, R> lineProcessor, Scanner scanner) {
        ImmutableList.Builder<R> builder = ImmutableList.builder();
        processNonEmptyLines((String line) -> builder.add(lineProcessor.apply(line)), scanner);
        return builder.build();
    }

    private static boolean asBool(String boolString) {
        return 1 == Integer.parseInt(boolString);
    }

    private static General parseGeneral(Scanner scanner) {
        Map<String, String> map = readKeyValueSection(scanner);
        Function<String, String> mustGet = (String aspect) -> mustGet(map, aspect, "General");

        GameMode mode;
        int modeValue = Integer.parseInt(mustGet.apply("Mode"));
        switch (modeValue) {
            case 0:
                mode = GameMode.STANDARD;
                break;
            case 1:
                mode = GameMode.TAIKO;
                break;
            case 2:
                mode = GameMode.CATCH_THE_BEAT;
                break;
            case 3:
                mode = GameMode.MANIA;
                break;
            default:
                throw new IllegalStateException("Unrecognized game mode: " + modeValue);
        }
        return General.builder()
                .audioFilename(mustGet.apply("AudioFilename"))
                .audioLeadIn(Integer.parseInt(mustGet.apply("AudioLeadIn")))
                .previewTime(Integer.parseInt(mustGet.apply("PreviewTime")))
                .countdown(asBool(mustGet.apply("Countdown")))
                .sampleSet(mustGet.apply("SampleSet"))
                .stackLeniency(Float.parseFloat(mustGet.apply("StackLeniency")))
                .mode(mode)
                .letterboxInBreaks(asBool(mustGet.apply("LetterboxInBreaks")))
                .widescreenStoryboard(asBool(mustGet.apply("WidescreenStoryboard")))
                .build();
    }

    private static Editor parseEditor(Scanner scanner) {
        Map<String, String> map = readKeyValueSection(scanner);
        Function<String, String> mustGet = (String aspect) -> mustGet(map, aspect, "Editor");
        return Editor.builder()
                .bookmarks(ImmutableList.copyOf(Streams
                        .stream(COMMA_SPLITTER.split(maybeGet(map, "Bookmarks").orElse("")))
                        .mapToInt(Integer::parseInt).iterator()))
                .distanceSpacing(Float.parseFloat(mustGet.apply("DistanceSpacing")))
                .beatDivisor(Integer.parseInt(mustGet.apply("BeatDivisor")))
                .gridSize(Integer.parseInt(mustGet.apply("GridSize")))
                .timelineZoom(Float.parseFloat(mustGet.apply("TimelineZoom")))
                .build();
    }

    private static Metadata parseMetadata(Scanner scanner) {
        Map<String, String> map = readKeyValueSection(scanner);
        Function<String, String> mustGet = (String aspect) -> mustGet(map, aspect, "Metadata");
        Metadata.Builder builder = Metadata.builder()
                .title(mustGet.apply("Title"))
                .artist(mustGet.apply("Artist"))
                .creator(mustGet.apply("Creator"))
                .version(mustGet.apply("Version"))
                .source(mustGet.apply("Source"))
                .tags(maybeGet(map, "Tags").map(SPACE_SPLITTER::splitToList).orElse(ImmutableList.of()))
                .beatmapID(Integer.parseInt(mustGet.apply("BeatmapID")))
                .beatmapSetID(Integer.parseInt(mustGet.apply("BeatmapSetID")));
        maybeGet(map, "TitleUnicode").ifPresent(builder::titleUnicode);
        maybeGet(map, "ArtistUnicode").ifPresent(builder::artistUnicode);
        return builder.build();
    }

    private static Difficulty parseDifficulty(Scanner scanner) {
        Map<String, String> map = readKeyValueSection(scanner);
        Function<String, Float> mustGet = (String aspect) ->
                Float.parseFloat(mustGet(map, aspect, "Difficulty"));
        return Difficulty.builder()
                .hpDrainRate(mustGet.apply("HPDrainRate"))
                .circleSize(mustGet.apply("CircleSize"))
                .overallDifficulty(mustGet.apply("OverallDifficulty"))
                .approachRate(mustGet.apply("ApproachRate"))
                .sliderMultiplier(mustGet.apply("SliderMultiplier"))
                .sliderTickRate(mustGet.apply("SliderTickRate"))
                .build();
    }

    private static Events parseEvents(Scanner scanner) {
        return Events.create(readListSection(Function.identity(), scanner));
    }

    private static List<TimingPoint> parseTimingPoints(Scanner scanner) {
        return readListSection(
                (String line) -> {
                    String[] parts = line.split(",", 8);
                    return TimingPoint.builder()
                            .offset(Integer.parseInt(parts[0]))
                            .millisecondsPerBeat(Float.parseFloat(parts[1]))
                            .meter(Integer.parseInt(parts[2]))
                            .sampleType(Integer.parseInt(parts[3]))
                            .sampleSet(Integer.parseInt(parts[4]))
                            .volume(Integer.parseInt(parts[5]))
                            .inherited(asBool(parts[6]))
                            .kiaiMode(asBool(parts[7]))
                            .build();
                }, scanner);
    }

    private static Map<Integer, Colour> parseColours(Scanner scanner) {

        return readKeyValueSection(
                (String rawKey) -> Integer.parseInt(rawKey.substring("combo".length())),
                (String rawVal) -> {
                    String[] parts = rawVal.split(",", 3);
                    return Colour.create(
                            Integer.parseInt(parts[0]),
                            Integer.parseInt(parts[1]),
                            Integer.parseInt(parts[2]));
                }, scanner);
    }

    private static List<HitObject> parseHitObjects(Scanner scanner) {
        Function<String, HitObject> lineParser = (String line) -> {
            Iterator<String> parts = COMMA_SPLITTER.split(line).iterator();
            HitObject.Builder builder = HitObject.builder()
                    .point(HitObject.Point.of(
                            parts.next(),
                            parts.next()))
                    .time(Integer.parseInt(parts.next()));

            int rawTypeBits = Integer.parseInt(parts.next());
            HitObject.Type objType = HitObject.Type.fromBits(rawTypeBits);

            builder
                    .rawType(rawTypeBits)
                    .hitSound(Integer.parseInt(parts.next()));

            switch (objType) {
                case CIRCLE:
                    break;
                case SLIDER:
                    builder.sliderAttributes(parseSliderAttributes(
                            parts.next(),
                            parts.next(),
                            parts.next(),
                            parts.hasNext() ? parts.next() : null,
                            parts.hasNext() ? parts.next() : null));
                    break;
                case SPINNER:
                    builder.spinnerEndTime(Integer.parseInt(parts.next()));
                    break;
            }

            if (parts.hasNext()) {
                builder.addition(parts.next());
            }

            return builder.build();
        };
        return readListSection(lineParser, scanner);
    }

    private static HitObject.SliderAttributes parseSliderAttributes(
            String rawSliderPoints, String rawRepeat, String rawPixelLength,
            String maybeRawEdgeHitSound, String maybeRawEdgeAddition) {
        Iterator<String> tokens = PIPE_SPLITTER.split(rawSliderPoints).iterator();
        HitObject.SliderAttributes.Builder builder = HitObject.SliderAttributes.builder()
                .type(HitObject.SliderAttributes.SliderType.of(tokens.next().charAt(0)));

        List<HitObject.Point> sliderPoints = ImmutableList.copyOf(Streams.stream(tokens)
                .map((String rawPoint) -> {
                    String[] pointParts = rawPoint.split(":", 2);
                    return HitObject.Point.of(pointParts[0], pointParts[1]);
                })
                .iterator());

        builder
                .sliderPoints(sliderPoints)
                .repeat(Integer.parseInt(rawRepeat))
                .pixelLength(Float.parseFloat(rawPixelLength));
        if (maybeRawEdgeHitSound != null) {
            builder.edgeHitSound(maybeRawEdgeHitSound);
        }
        if (maybeRawEdgeAddition != null) {
            builder.edgeAddition(maybeRawEdgeAddition);
        }
        return builder.build();
    }
}
