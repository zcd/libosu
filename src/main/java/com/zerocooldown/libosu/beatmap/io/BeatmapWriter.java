package com.zerocooldown.libosu.beatmap.io;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.zerocooldown.libosu.beatmap.Beatmap;
import com.zerocooldown.libosu.beatmap.datatypes.Colour;
import com.zerocooldown.libosu.beatmap.datatypes.HitObject;
import com.zerocooldown.libosu.beatmap.datatypes.TimingPoint;
import com.zerocooldown.libosu.beatmap.section.Difficulty;
import com.zerocooldown.libosu.beatmap.section.Editor;
import com.zerocooldown.libosu.beatmap.section.General;
import com.zerocooldown.libosu.beatmap.section.Metadata;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * File writer for osu! beatmaps.
 *
 * The written beatmap is **not** guaranteed to be bitwise identical to the file it was read from.
 */
public class BeatmapWriter implements Closeable, AutoCloseable, Flushable {
    private static final Joiner COMMA_JOINER = Joiner.on(',').skipNulls();
    private static final Joiner PIPE_JOINER = Joiner.on('|').skipNulls();
    private static final Joiner SPACE_JOINER = Joiner.on(' ');
    private static final String NEWLINE = "\r\n";

    private final BufferedWriter sink;

    /**
     * Writes out a {@link Beatmap} instance.
     * <p>
     * See <a href="https://osu.ppy.sh/wiki/Osu_%28file_format%29#Sections">osu! wiki page</a> for the full file spec.
     *
     * @param source a {@link Beatmap} instance to write.
     * @param sink the stream to write to.
     */
    public static void write(Beatmap source, OutputStream sink) throws IOException {
        new BeatmapWriter(new BufferedWriter(new OutputStreamWriter(sink, Charsets.UTF_8))).run(source);
    }

    private BeatmapWriter(BufferedWriter sink) {
        this.sink = sink;
    }

    @Override
    public void close() throws IOException {
        sink.close();
    }


    @Override
    public void flush() throws IOException {
        sink.flush();
    }

    private void run(Beatmap source) throws IOException {
        sink.append(source.osuFormatVersion());
        delimitSection();

        writeSectionGeneral(source);
        delimitSection();

        writeSectionEditor(source);
        delimitSection();

        writeSectionMetadata(source);
        delimitSection();

        writeSectionDifficulty(source);
        delimitSection();

        writeSectionEvents(source);
        delimitSection();

        writeSectionTimingPoints(source);
        delimitSection();

        writeSectionColours(source);
        delimitSection();

        writeSectionHitObjects(source);
        close();
    }

    // Sections
    private void writeSectionGeneral(Beatmap beatmap) throws IOException {
        General general = beatmap.general();
        writeSectionHeader("General");

        writeKeyValue("AudioFilename", general.audioFilename());
        writeKeyValue("AudioLeadIn", general.audioLeadIn());
        writeKeyValue("PreviewTime", general.previewTime());
        writeKeyValue("Countdown", toNumericBool(general.countdown()));
        writeKeyValue("SampleSet", general.sampleSet());
        writeKeyValue("StackLeniency", general.stackLeniency());
        writeKeyValue("Mode", general.mode().ordinal());
        writeKeyValue("LetterboxInBreaks", toNumericBool(general.letterboxInBreaks()));
        writeKeyValue("WidescreenStoryboard", toNumericBool(general.widescreenStoryboard()));
    }

    private void writeSectionEditor(Beatmap beatmap) throws IOException {
        Editor editor = beatmap.editor();
        writeSectionHeader("Editor");

        if (!editor.bookmarks().isEmpty()) {
            writeKeyValue("Bookmarks", SPACE_JOINER.join(editor.bookmarks()));
        }
        writeKeyValue("DistanceSpacing", editor.distanceSpacing());
        writeKeyValue("BeatDivisor", editor.beatDivisor());
        writeKeyValue("GridSize", editor.gridSize());
        writeKeyValue("TimelineZoom", editor.timelineZoom());
    }

    private void writeSectionMetadata(Beatmap beatmap) throws IOException {
        Metadata metadata = beatmap.metadata();
        writeSectionHeader("Metadata");

        writeKeyValue("Title", metadata.title());
        if (metadata.titleUnicode().isPresent()) {
            writeKeyValue("TitleUnicode", metadata.titleUnicode().get());
        }
        writeKeyValue("Artist", metadata.artist());
        if (metadata.artistUnicode().isPresent()) {
            writeKeyValue("ArtistUnicode", metadata.artistUnicode().get());
        }
        writeKeyValue("Creator", metadata.creator());
        writeKeyValue("Version", metadata.version());
        writeKeyValue("Source", metadata.source());
        if (!metadata.tags().isEmpty()) {
            writeKeyValue("Tags", SPACE_JOINER.join(metadata.tags()));
        }
        writeKeyValue("BeatmapID", metadata.beatmapID());
        writeKeyValue("BeatmapSetID", metadata.beatmapSetID());
    }

    private void writeSectionDifficulty(Beatmap beatmap) throws IOException {
        Difficulty difficulty = beatmap.difficulty();
        writeSectionHeader("Difficulty");

        writeKeyValue("HPDrainRate", difficulty.hpDrainRate());
        writeKeyValue("CircleSize", difficulty.circleSize());
        writeKeyValue("OverallDifficulty", difficulty.overallDifficulty());
        writeKeyValue("ApproachRate", difficulty.approachRate());
        writeKeyValue("SliderMultiplier", difficulty.sliderMultiplier());
        writeKeyValue("SliderTickRate", difficulty.sliderTickRate());
    }

    private void writeSectionEvents(Beatmap beatmap) throws IOException {
        writeSectionHeader("Events");
        writeList(beatmap.events().lines(), Function.identity());
    }

    private void writeSectionTimingPoints(Beatmap beatmap) throws IOException {
        writeSectionHeader("TimingPoints");
        writeList(
                beatmap.timingPoints(),
                (TimingPoint p) -> String.format("%d,%f,%d,%d,%d,%d,%d,%d",
                        p.offset(),
                        p.millisecondsPerBeat(),
                        p.meter(),
                        p.sampleType(),
                        p.sampleSet(),
                        p.volume(),
                        toNumericBool(p.inherited()),
                        toNumericBool(p.kiaiMode()))
        );
    }

    private void writeSectionColours(Beatmap beatmap) throws IOException {
        writeSectionHeader("Colours");
        for (Map.Entry<Integer, Colour> kv : beatmap.colours().entrySet()) {
            Colour c = kv.getValue();
            writeKeyValue("Combo" + kv.getKey(), String.format("%d,%d,%d", c.red(), c.green(), c.blue()));
        }
    }

    private void writeSectionHitObjects(Beatmap beatmap) throws IOException {
        writeSectionHeader("HitObjects");
        writeList(
                beatmap.hitObjects(),
                (HitObject o) -> {
                    String base = String.format(
                            "%d,%d,%d,%d,%d", o.point().x(), o.point().y(), o.time(), o.rawType(), o.hitSound());
                    String maybeAttributes = null;

                    switch (o.type()) {
                        case CIRCLE:
                            break;
                        case SLIDER:
                            HitObject.SliderAttributes attrs = o.sliderAttributes().get();
                            StringBuilder builder = new StringBuilder();
                            builder.append(attrs.type().asChar());
                            builder.append('|');
                            PIPE_JOINER.appendTo(
                                    builder,
                                    attrs.sliderPoints()
                                            .stream()
                                            .map((HitObject.Point p) -> String.format("%d:%d", p.x(), p.y()))
                                            .iterator());
                            builder.append(',');
                            COMMA_JOINER.appendTo(
                                    builder,
                                    new String[]{
                                            String.valueOf(attrs.repeat()),
                                            String.valueOf(attrs.pixelLength()),
                                            attrs.edgeHitSound().orElse(null),
                                            attrs.edgeAddition().orElse(null)
                                    });
                            maybeAttributes = builder.toString();
                            break;
                        case SPINNER:
                            maybeAttributes = String.valueOf(o.spinnerEndTime().get());
                            break;
                    }

                    return COMMA_JOINER.join(new String[]{base, maybeAttributes, o.addition().orElse(null)});
                });
    }

    // Utilities
    private void newline() throws IOException {
        sink.append(NEWLINE);
    }

    private void delimitSection() throws IOException {
        newline();
    }

    private <V> void writeKeyValue(String key, V value) throws IOException {
        sink.append(key).append(": ").append(String.valueOf(value));
        newline();
    }

    private <S> void writeList(List<S> list, Function<S, String> converter) throws IOException {
        for (S item : list) {
            sink.append(converter.apply(item));
            newline();
        }
    }

    private void writeSectionHeader(String header) throws IOException {
        sink.append("[").append(header).append("]");
        sink.newLine();
    }

    private static int toNumericBool(boolean b) {
        return b ? 1 : 0;
    }
}
