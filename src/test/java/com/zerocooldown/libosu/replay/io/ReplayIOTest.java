package com.zerocooldown.libosu.replay.io;

import com.zerocooldown.libosu.common.GameMode;
import com.zerocooldown.libosu.common.Mod;
import com.zerocooldown.libosu.replay.Metadata;
import com.zerocooldown.libosu.replay.Moment;
import com.zerocooldown.libosu.replay.Replay;
import com.zerocooldown.libosu.testutil.TestCategories;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.List;

import static com.zerocooldown.libosu.replay.io.DataStringCodecTest.EPSILON;

@Category({TestCategories.RequiresIO.class, TestCategories.UnitTest.class})
public class ReplayIOTest {

    @Test
    public void read_FreedomDiveHDHR() throws Exception {
        Replay play;
        try (InputStream source = getClass().getClassLoader().getResourceAsStream(
                "replays/cookiezi_freedomdive_hdhr.osr")) {
            play = ReplayIO.read(source);
        }
        Metadata metadata = play.metadata();
        Assert.assertEquals("Cookiezi", metadata.playerName());
        Assert.assertEquals(GameMode.STANDARD, metadata.gameMode());

        Assert.assertTrue(metadata.mods().equals(EnumSet.of(Mod.HIDDEN, Mod.HARD_ROCK)));
        Assert.assertEquals(132408001, metadata.totalScore());
        Assert.assertEquals(2385, metadata.maxCombo());
        Assert.assertTrue(metadata.isPerfect());

        Assert.assertEquals(1978, metadata.num300());
        Assert.assertEquals(5, metadata.num100());
        Assert.assertEquals(0, metadata.num50());
        Assert.assertEquals(247, metadata.numGeki());
        Assert.assertEquals(4, metadata.numKatu());
        Assert.assertEquals(0, metadata.numMiss());
    }

    @Test
    public void read_BlueZenithChoke() throws Exception {
        Replay play;
        try (InputStream source = getClass().getClassLoader().getResourceAsStream(
                "replays/cookiezi_bluezenith_hr.osr")) {
            play = ReplayIO.read(source);
        }
        Metadata metadata = play.metadata();
        Assert.assertEquals("Cookiezi", metadata.playerName());
        Assert.assertEquals(GameMode.STANDARD, metadata.gameMode());

        Assert.assertTrue(metadata.mods().equals(EnumSet.of(Mod.HARD_ROCK)));
        Assert.assertEquals(150551330, metadata.totalScore());
        Assert.assertEquals(2358, metadata.maxCombo());
        Assert.assertFalse(metadata.isPerfect());

        Assert.assertEquals(1965, metadata.num300());
        Assert.assertEquals(7, metadata.num100());
        Assert.assertEquals(0, metadata.num50());
        Assert.assertEquals(223, metadata.numGeki());
        Assert.assertEquals(6, metadata.numKatu());
        Assert.assertEquals(1, metadata.numMiss());  // press F to pay respects
    }

    @Test
    public void read_Maniera() throws Exception {
        Replay play;
        try (InputStream source = getClass().getClassLoader().getResourceAsStream(
                "replays/jhlee0133_maniera.osr")) {
            play = ReplayIO.read(source);
        }
        Metadata metadata = play.metadata();
        Assert.assertEquals("jhlee0133", metadata.playerName());
        Assert.assertEquals(GameMode.MANIA, metadata.gameMode());

        Assert.assertTrue(metadata.mods().equals(EnumSet.of(Mod.NONE)));
        Assert.assertEquals(978993, metadata.totalScore());
        Assert.assertEquals(1577, metadata.maxCombo());
        Assert.assertFalse(metadata.isPerfect());

        Assert.assertEquals(460, metadata.num300());
        Assert.assertEquals(2, metadata.num100());
        Assert.assertEquals(1, metadata.num50());
        Assert.assertEquals(2493, metadata.numGeki());
        Assert.assertEquals(39, metadata.numKatu());
        Assert.assertEquals(2, metadata.numMiss());
    }

    @Test
    public void readWrite_FreedomDiveHDHR() throws Exception {
        Replay play;
        try (InputStream source = getClass().getClassLoader().getResourceAsStream(
                "replays/cookiezi_freedomdive_hdhr.osr")) {
            play = ReplayIO.read(source);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ReplayIO.write(play, out);
        Replay reparsed = ReplayIO.read(new ByteArrayInputStream(out.toByteArray()));

        Assert.assertEquals(play.metadata(), reparsed.metadata());
        assertMomentsSimilar(play.moments(), reparsed.moments());
    }

    private void assertMomentsSimilar(List<Moment> moments1, List<Moment> moments2) {
        Assert.assertEquals(moments1.size(), moments2.size());

        for (int i = 0; i < moments1.size(); i++) {
            Moment m1 = moments1.get(i);
            Moment m2 = moments2.get(i);

            Assert.assertEquals(m1.millisSincePrev(), m2.millisSincePrev());
            Assert.assertEquals(m1.keys(), m2.keys());
            Assert.assertEquals(m1.cursor().x(), m2.cursor().x(), EPSILON);
            Assert.assertEquals(m1.cursor().y(), m2.cursor().y(), EPSILON);
        }
    }
}