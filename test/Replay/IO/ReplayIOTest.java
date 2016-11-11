package Replay.IO;

import Constants.GameMode;
import Constants.Mod;
import TestUtil.TestCategories;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.EnumSet;

@Category({TestCategories.RequiresIO.class, TestCategories.UnitTest.class})
public class ReplayIOTest {

    @Test
    public void read_FreedomDiveHDHR() throws Exception {
        Replay.Replay play = ReplayIO.read(getClass().getClassLoader().getResourceAsStream(
                "replays/cookiezi_freedomdive_hdhr.osr"));
        Assert.assertEquals("Cookiezi", play.playerName());
        Assert.assertEquals(GameMode.STANDARD, play.gameMode());

        Assert.assertTrue(play.mods().equals(EnumSet.of(Mod.HIDDEN, Mod.HARD_ROCK)));
        Assert.assertEquals(132408001, play.totalScore());
        Assert.assertEquals(2385, play.maxCombo());
        Assert.assertTrue(play.isPerfect());

        Assert.assertEquals(1978, play.num300());
        Assert.assertEquals(5, play.num100());
        Assert.assertEquals(0, play.num50());
        Assert.assertEquals(247, play.numGeki());
        Assert.assertEquals(4, play.numKatu());
        Assert.assertEquals(0, play.numMiss());
    }

    @Test
    public void read_BlueZenithChoke() throws Exception {
        Replay.Replay play = ReplayIO.read(getClass().getClassLoader().getResourceAsStream(
                "replays/cookiezi_bluezenith_hr.osr"));
        Assert.assertEquals("Cookiezi", play.playerName());
        Assert.assertEquals(GameMode.STANDARD, play.gameMode());

        Assert.assertTrue(play.mods().equals(EnumSet.of(Mod.HARD_ROCK)));
        Assert.assertEquals(150551330, play.totalScore());
        Assert.assertEquals(2358, play.maxCombo());
        Assert.assertFalse(play.isPerfect());

        Assert.assertEquals(1965, play.num300());
        Assert.assertEquals(7, play.num100());
        Assert.assertEquals(0, play.num50());
        Assert.assertEquals(223, play.numGeki());
        Assert.assertEquals(6, play.numKatu());
        Assert.assertEquals(1, play.numMiss());  // press F to pay respects
    }

    @Test
    public void read_Maniera() throws Exception {
        Replay.Replay play = ReplayIO.read(getClass().getClassLoader().getResourceAsStream(
                "replays/jhlee0133_maniera.osr"));
        Assert.assertEquals("jhlee0133", play.playerName());
        Assert.assertEquals(GameMode.MANIA, play.gameMode());

        Assert.assertTrue(play.mods().equals(EnumSet.of(Mod.NONE)));
        Assert.assertEquals(978993, play.totalScore());
        Assert.assertEquals(1577, play.maxCombo());
        Assert.assertFalse(play.isPerfect());

        Assert.assertEquals(460, play.num300());
        Assert.assertEquals(2, play.num100());
        Assert.assertEquals(1, play.num50());
        Assert.assertEquals(2493, play.numGeki());
        Assert.assertEquals(39, play.numKatu());
        Assert.assertEquals(2, play.numMiss());
    }
}