package com.zerocooldown.libosu.beatmap.io;

import com.google.common.collect.ImmutableList;
import com.zerocooldown.libosu.beatmap.Beatmap;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

public class BeatmapWriterTest {
    @Test
    public void write_IsReReadable() throws Exception {
        List<String> beatmapPaths = ImmutableList.of(
                "beatmaps/aran - Graces of Heaven (spro) [Debug].osu",
                "beatmaps/aran - Graces of Heaven (spro) [Original].osu",
                "beatmaps/Camellia - LET'S JUMP (RikiH_) [Year 2316].osu",
                "beatmaps/gmtn. (witch's slave) - furioso melodia (Alumetorz) [Wrath].osu",
                "beatmaps/Halozy - Genryuu Kaiko (Hollow Wings) [Higan Torrent].osu",
                "beatmaps/Halozy - Kikoku Doukoku Jigokuraku (Hollow Wings) [Notch Hell].osu",
                "beatmaps/Nekomata Master+ - Funny shuffle (fanzhen0019) [RiP iidx].osu");
        for (String path : beatmapPaths) {
            assertParsedEqualsReparsed(path);
        }
    }

    public void assertParsedEqualsReparsed(String path) throws Exception {
        Beatmap beatmap;
        try (InputStream source = getClass().getClassLoader().getResourceAsStream(path)) {
            beatmap = BeatmapReader.read(source);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BeatmapWriter.write(beatmap, out);
        Beatmap reparsed = BeatmapReader.read(new ByteArrayInputStream(out.toByteArray()));
        Assert.assertEquals(beatmap, reparsed);
    }

}