package com.zerocooldown.libosu.replay.io;

import com.zerocooldown.libosu.common.KeyStroke;
import com.zerocooldown.libosu.replay.LifeBarSample;
import com.zerocooldown.libosu.replay.Moment;
import com.zerocooldown.libosu.testutil.TestCategories;
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import java.util.EnumSet;
import java.util.List;

import static com.zerocooldown.libosu.common.KeyStroke.KEY1;
import static com.zerocooldown.libosu.common.KeyStroke.KEY2;
import static com.zerocooldown.libosu.common.KeyStroke.MOUSE2;

@RunWith(JUnitQuickcheck.class)
@Category(TestCategories.UnitTest.class)
public class DataStringCodecTest {
    static final double EPSILON = 1e-4;

    private static float roundFloat(float value, int decimalPlaces) {
        double offset = Math.pow(10, decimalPlaces);
        return (float) (Math.round(value * offset) / offset);
    }

    @Test
    public void parseMoment() throws Exception {
        Moment moment = DataStringCodec.parseMoment("10|1.5|3.14|15");
        Assert.assertEquals(10, moment.millisSincePrev());
        Assert.assertEquals(1.5, moment.cursor().x(), EPSILON);
        Assert.assertEquals(3.14, moment.cursor().y(), EPSILON);
        Assert.assertTrue(moment.keys().containsAll(EnumSet.of(KEY1, KEY2)));
    }

    @Test
    public void parseMoments_Empty() throws Exception {
        List<Moment> moments = DataStringCodec.toList("", DataStringCodec::parseMoment);
        Assert.assertTrue(moments.isEmpty());
    }

    @Test
    public void parseMoments_SingleMoment() throws Exception {
        List<Moment> moments = DataStringCodec.toList("10|1.5|3.14|15", DataStringCodec::parseMoment);
        Assert.assertEquals(1, moments.size());

        Moment moment = moments.get(0);
        Assert.assertEquals(10, moment.millisSincePrev());
        Assert.assertEquals(1.5, moment.cursor().x(), EPSILON);
        Assert.assertEquals(3.14, moment.cursor().y(), EPSILON);
        Assert.assertTrue(moment.keys().containsAll(EnumSet.of(KEY1, KEY2)));
    }

    @Test
    public void parseMoments_MultipleMoments() throws Exception {
        List<Moment> moments = DataStringCodec.toList("10|1.5|3.14|15,5|120|120.1|2", DataStringCodec::parseMoment);
        Assert.assertEquals(2, moments.size());
        Moment moment;

        moment = moments.get(0);
        Assert.assertEquals(10, moment.millisSincePrev());
        Assert.assertEquals(1.5, moment.cursor().x(), EPSILON);
        Assert.assertEquals(3.14, moment.cursor().y(), EPSILON);
        Assert.assertTrue(moment.keys().containsAll(EnumSet.of(KEY1, KEY2)));

        moment = moments.get(1);
        Assert.assertEquals(5, moment.millisSincePrev());
        Assert.assertEquals(120, moment.cursor().x(), EPSILON);
        Assert.assertEquals(120.1, moment.cursor().y(), EPSILON);
        Assert.assertTrue(moment.keys().containsAll(EnumSet.of(MOUSE2)));
    }

    @Property
    public void momentEncodeDecode(@From(MomentGenerator.class) Moment moment) throws Exception {
        Moment recycled = DataStringCodec.parseMoment(DataStringCodec.encodeMoment(moment));
        Assert.assertEquals(moment.millisSincePrev(), recycled.millisSincePrev());
        Assert.assertTrue(moment.keys().equals(recycled.keys()));
        Assert.assertEquals(moment.cursor().x(), recycled.cursor().x(), EPSILON);
        Assert.assertEquals(moment.cursor().y(), recycled.cursor().y(), EPSILON);
    }

    @Property
    public void lifebarSampleEncodeDecode(@From(LifeBarSampleGenerator.class) LifeBarSample sample) throws Exception {
        LifeBarSample recycled = DataStringCodec.parseLifeBarSample(DataStringCodec.encodeLifeBarSample(sample));
        Assert.assertEquals(sample.offsetMillis(), recycled.offsetMillis());
        Assert.assertEquals(sample.lifeFraction(), recycled.lifeFraction(), EPSILON);
    }

    protected static class MomentGenerator extends Generator<Moment> {
        public MomentGenerator() {
            super(Moment.class);
        }

        @Override
        public Moment generate(SourceOfRandomness random, GenerationStatus status) {
            return Moment.create(
                    random.nextLong(0, Long.MAX_VALUE),
                    roundFloat(random.nextFloat(0, 512), 4),
                    roundFloat(random.nextFloat(0, 384), 4),
                    KeyStroke.fromMask(random.nextInt()));
        }
    }

    protected static class LifeBarSampleGenerator extends Generator<LifeBarSample> {
        public LifeBarSampleGenerator() {
            super(LifeBarSample.class);
        }

        @Override
        public LifeBarSample generate(SourceOfRandomness random, GenerationStatus status) {
            return LifeBarSample.create(random.nextLong(0, Long.MAX_VALUE), roundFloat(random.nextFloat(0, 1), 4));
        }
    }
}