package com.zerocooldown.libosu.beatmap.io;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.zerocooldown.libosu.beatmap.Beatmap;
import com.zerocooldown.libosu.beatmap.datatypes.Colour;
import com.zerocooldown.libosu.beatmap.datatypes.HitObject;
import com.zerocooldown.libosu.beatmap.datatypes.TimingPoint;
import com.zerocooldown.libosu.beatmap.section.Difficulty;
import com.zerocooldown.libosu.beatmap.section.Editor;
import com.zerocooldown.libosu.beatmap.section.General;
import com.zerocooldown.libosu.beatmap.section.Metadata;
import com.zerocooldown.libosu.constants.GameMode;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;

public class BeatmapReaderTest {
    @Test
    public void read_HiganTorrent() throws Exception {
        Beatmap beatmap;
        try (InputStream source = getClass().getClassLoader().getResourceAsStream(
                "beatmaps/Halozy - Genryuu Kaiko (Hollow Wings) [Higan Torrent].osu")) {
            beatmap = BeatmapReader.read(source);
        }

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
                        .titleUnicode("源流懐古")
                        .artist("Halozy")
                        .artistUnicode("Halozy")
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
        Assert.assertEquals(2022 - 131 + 1, beatmap.hitObjects().size());

        // 76,270,1619,6,0,B|106:271|128:253|128:253|144:272|178:280|178:280|201:258|241:250|276:264,1,210,6|0,0:1|0:0,0:0:0:0:
        HitObject obj = beatmap.hitObjects().get(0);
        Assert.assertEquals(
                HitObject.builder()
                        .point(HitObject.Point.of(76, 270))
                        .time(1619)
                        .rawType(6)
                        .hitSound(0)
                        .sliderAttributes(HitObject.SliderAttributes.builder()
                                .type(HitObject.SliderAttributes.SliderType.BEZIER)
                                .sliderPoints(ImmutableList.<HitObject.Point>builder()
                                        .add(HitObject.Point.of(106, 271)).add(HitObject.Point.of(128, 253))
                                        .add(HitObject.Point.of(128, 253)).add(HitObject.Point.of(144, 272))
                                        .add(HitObject.Point.of(178, 280)).add(HitObject.Point.of(178, 280))
                                        .add(HitObject.Point.of(201, 258)).add(HitObject.Point.of(241, 250))
                                        .add(HitObject.Point.of(276, 264))
                                        .build())
                                .repeat(1)
                                .pixelLength(210)
                                .edgeHitSound("6|0")
                                .edgeAddition("0:1|0:0")
                                .build())
                        .addition("0:0:0:0:")
                        .build(),
                obj);
        Assert.assertEquals(HitObject.Type.SLIDER, obj.type());
        Assert.assertTrue(obj.isNewCombo());

        // 189,161,30579,2,0,P|210:120|203:78,1,70
        obj = beatmap.hitObjects().get(247 - 131);
        Assert.assertEquals(
                HitObject.builder()
                        .point(HitObject.Point.of(189, 161))
                        .time(30579)
                        .rawType(2)
                        .hitSound(0)
                        .sliderAttributes(HitObject.SliderAttributes.builder()
                                .type(HitObject.SliderAttributes.SliderType.PERFECT_CURVE)
                                .sliderPoints(ImmutableList.of(
                                        HitObject.Point.of(210, 120), HitObject.Point.of(203, 78)))
                                .repeat(1)
                                .pixelLength(70)
                                .build())
                        .build(),
                obj);
        Assert.assertEquals(HitObject.Type.SLIDER, obj.type());
        Assert.assertFalse(obj.isNewCombo());

        // 25,313,90752,1,8,0:0:0:0:
        obj = beatmap.hitObjects().get(620 - 131);
        Assert.assertEquals(
                HitObject.builder()
                        .point(HitObject.Point.of(25, 313))
                        .time(90752)
                        .rawType(1)
                        .hitSound(8)
                        .addition("0:0:0:0:")
                        .build(),
                obj);
        Assert.assertEquals(HitObject.Type.CIRCLE, obj.type());
        Assert.assertFalse(obj.isNewCombo());

        // 116,282,91792,5,4,0:0:0:0:
        obj = beatmap.hitObjects().get(631 - 131);
        Assert.assertEquals(
                HitObject.builder()
                        .point(HitObject.Point.of(116, 282))
                        .time(91792)
                        .rawType(5)
                        .hitSound(4)
                        .addition("0:0:0:0:")
                        .build(),
                obj);
        Assert.assertEquals(HitObject.Type.CIRCLE, obj.type());
        Assert.assertTrue(obj.isNewCombo());

        // 127,253,222891,2,0,L|136:207,4,43.75,2|0|0|0|4,0:3|0:0|0:0|0:0|0:3,0:0:0:0:
        obj = beatmap.hitObjects().get(1393 - 131);
        Assert.assertEquals(
                HitObject.builder()
                        .point(HitObject.Point.of(127, 253))
                        .time(222891)
                        .rawType(2)
                        .hitSound(0)
                        .sliderAttributes(HitObject.SliderAttributes.builder()
                                .type(HitObject.SliderAttributes.SliderType.LINEAR)
                                .sliderPoints(ImmutableList.of(HitObject.Point.of(136, 207)))
                                .repeat(4)
                                .pixelLength(43.75f)
                                .edgeHitSound("2|0|0|0|4")
                                .edgeAddition("0:3|0:0|0:0|0:0|0:3")
                                .build())
                        .addition("0:0:0:0:")
                        .build(),
                obj);
        Assert.assertEquals(HitObject.Type.SLIDER, obj.type());
        Assert.assertFalse(obj.isNewCombo());

        // 256,192,301359,12,0,306128,3:0:0:0:
        obj = beatmap.hitObjects().get(2022 - 131);
        Assert.assertEquals(
                HitObject.builder()
                        .point(HitObject.Point.of(256, 192))
                        .time(301359)
                        .rawType(12)
                        .hitSound(0)
                        .spinnerEndTime(306128)
                        .addition("3:0:0:0:")
                        .build(),
                obj);
        Assert.assertEquals(HitObject.Type.SPINNER, obj.type());
        Assert.assertTrue(obj.isNewCombo());
    }

}