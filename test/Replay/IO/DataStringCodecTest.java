package Replay.IO;

import Replay.Action;
import TestUtil.TestCategories;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.EnumSet;
import java.util.List;

import static Constants.KeyStroke.KEY1;
import static Constants.KeyStroke.KEY2;
import static Constants.KeyStroke.MOUSE1;
import static Constants.KeyStroke.MOUSE2;

@Category(TestCategories.UnitTest.class)
public class DataStringCodecTest {
    private static final double EPSILON = 0.001;

    @Test
    public void parseAction() throws Exception {
        Action action = DataStringCodec.parseAction("10|1.5|3.14|15");
        Assert.assertEquals(10, action.getMillisSincePrevAction());
        Assert.assertEquals(1.5, action.getX(), EPSILON);
        Assert.assertEquals(3.14, action.getY(), EPSILON);
        Assert.assertTrue(action.getKeyPresses().containsAll(EnumSet.of(MOUSE1, MOUSE2, KEY1, KEY2)));
    }

    @Test
    public void parseActions_Empty() throws Exception {
        List<Action> actions = DataStringCodec.toList("", DataStringCodec::parseAction);
        Assert.assertTrue(actions.isEmpty());
    }

    @Test
    public void parseActions_SingleAction() throws Exception {
        List<Action> actions = DataStringCodec.toList("10|1.5|3.14|15", DataStringCodec::parseAction);
        Assert.assertEquals(1, actions.size());

        Action action = actions.get(0);
        Assert.assertEquals(10, action.getMillisSincePrevAction());
        Assert.assertEquals(1.5, action.getX(), EPSILON);
        Assert.assertEquals(3.14, action.getY(), EPSILON);
        Assert.assertTrue(action.getKeyPresses().containsAll(EnumSet.of(MOUSE1, MOUSE2, KEY1, KEY2)));
    }

    @Test
    public void parseActions_MultipleActions() throws Exception {
        List<Action> actions = DataStringCodec.toList("10|1.5|3.14|15,5|120|120.1|10", DataStringCodec::parseAction);
        Assert.assertEquals(2, actions.size());
        Action action;

        action = actions.get(0);
        Assert.assertEquals(10, action.getMillisSincePrevAction());
        Assert.assertEquals(1.5, action.getX(), EPSILON);
        Assert.assertEquals(3.14, action.getY(), EPSILON);
        Assert.assertTrue(action.getKeyPresses().containsAll(EnumSet.of(MOUSE1, MOUSE2, KEY1, KEY2)));

        action = actions.get(1);
        Assert.assertEquals(5, action.getMillisSincePrevAction());
        Assert.assertEquals(120, action.getX(), EPSILON);
        Assert.assertEquals(120.1, action.getY(), EPSILON);
        Assert.assertTrue(action.getKeyPresses().containsAll(EnumSet.of(MOUSE2, KEY2)));
    }
}