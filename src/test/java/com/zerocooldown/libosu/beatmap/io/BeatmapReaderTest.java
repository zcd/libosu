package com.zerocooldown.libosu.beatmap.io;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.zerocooldown.libosu.beatmap.Beatmap;
import com.zerocooldown.libosu.beatmap.datatypes.Colour;
import com.zerocooldown.libosu.beatmap.datatypes.TimingPoint;
import com.zerocooldown.libosu.beatmap.section.Difficulty;
import com.zerocooldown.libosu.beatmap.section.Editor;
import com.zerocooldown.libosu.beatmap.section.General;
import com.zerocooldown.libosu.beatmap.section.Metadata;
import com.zerocooldown.libosu.constants.GameMode;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class BeatmapReaderTest {
    @Test
    public void read_HiganTorrent() throws Exception {
        Beatmap beatmap = BeatmapReader.read(getClass().getClassLoader().getResourceAsStream(
                "beatmaps/Halozy - Genryuu Kaiko (Hollow Wings) [Higan Torrent].osu"));

        Assert.assertEquals("osu file format v14", beatmap.osuFormatVersion());

        // General
        Assert.assertEquals(
                General.builder()
                        .audioFilename("Gennryuu Kaiko.mp3")
                        .audioLeadIn(0)
                        .previewTime(1099)
                        .countdown(false)
                        .sampleSet("Soft")
                        .stackLeniency(0.2f)
                        .mode(GameMode.STANDARD)
                        .letterboxInBreaks(false)
                        /* .epilepsyWarning(true) TODO(zcd): this looks unlisted on the spec as of 2017-02-02 */
                        .widescreenStoryboard(true)
                        .build(),
                beatmap.general());

        // Editor
        Assert.assertEquals(
                Editor.builder()
                        .bookmarks(ImmutableList.of())
                        .distanceSpacing(0.6f)
                        .beatDivisor(4)
                        .gridSize(4)
                        .timelineZoom(1.8f)
                        .build(),
                beatmap.editor());

        // Metadata
        Assert.assertEquals(
                Metadata.builder()
                        .title("Genryuu Kaiko")
                        .titleUnicode(Optional.of("源流懐古"))
                        .artist("Halozy")
                        .artistUnicode(Optional.of("Halozy"))
                        .creator("Hollow Wings")
                        .version("Higan Torrent")
                        .source("東方Project")
                        .tags(ImmutableList.<String>builder()
                                .add("Touhou").add("Cosmic").add("Armonica").add("ほたる").add("hotaru")
                                .add("すみじゅん").add("Sumijun").add("くまりす").add("kumarisu").add("東方花映塚")
                                .add("彼岸帰航").add("Higan").add("Retour").add("~").add("Riverside").add("View")
                                .add("小野塚").add("小町").add("おのづか").add("こまち").add("Onozuka").add("Komachi")
                                .add("th09").add("c79").add("HW").add("ShallICompareThee").add("SICT").add("zun")
                                .build())
                        .beatmapID(433005)
                        .beatmapSetID(180138)
                        .build(),
                beatmap.metadata());

        // Difficulty
        Assert.assertEquals(
                Difficulty.builder()
                        .hpDrainRate(6f)
                        .circleSize(4f)
                        .overallDifficulty(8f)
                        .approachRate(9.6f)
                        .sliderMultiplier(2.8f)
                        .sliderTickRate(1f)
                        .build(),
                beatmap.difficulty());

        // Events
        // This is untested!

        // Timing Points
        Assert.assertEquals(120 - 52 + 1, beatmap.timingPoints().size());
        Assert.assertEquals(
                TimingPoint.builder()
                        .offset(-1155)
                        .millisecondsPerBeat(346.820809248555f)
                        .meter(4)
                        .sampleType(2)
                        .sampleSet(1)
                        .volume(70)
                        .inherited(true)
                        .kiaiMode(false)
                        .build(),
                beatmap.timingPoints().get(0));
        Assert.assertEquals(
                TimingPoint.builder()
                        .offset(244394)
                        .millisecondsPerBeat(-66.6666666666667f)
                        .meter(4)
                        .sampleType(2)
                        .sampleSet(1)
                        .volume(100)
                        .inherited(false)
                        .kiaiMode(true)
                        .build(),
                beatmap.timingPoints().get(99 - 52));


        // Colours
        Assert.assertThat(
                beatmap.colours(),
                Matchers.equalTo(ImmutableMap.of(
                        1, Colour.create(255, 0, 0),
                        2, Colour.create(136, 106, 255),
                        3, Colour.create(255, 149, 181),
                        4, Colour.create(194, 102, 75),
                        5, Colour.create(237, 159, 143))));

        // Hit Objects
        // TODO(zcd): implement actual tests once HitObject parse is complete
        Assert.assertEquals(2022 - 131 + 1, beatmap.hitObjects().size());
    }

}