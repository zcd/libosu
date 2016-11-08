package Util;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.GeneratorConfiguration;
import com.pholser.junit.quickcheck.generator.java.lang.ByteGenerator;
import com.pholser.junit.quickcheck.generator.java.util.ArrayListGenerator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Assume;
import org.junit.Test;

import org.junit.Assert;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Tests for Util.Uleb128
 */
@RunWith(JUnitQuickcheck.class)
public class Uleb128Test {
    // TODO(zcd): fuzz testing?
    @Test
    public void readFrom() throws Exception {
        Uleb128 parsed = Uleb128.fromByteStream(
                new ByteArrayInputStream(new byte[]{(byte) 0xe5, (byte) 0x8e, (byte) 0x26}));
        Assert.assertEquals(624485L, parsed.asLong());
    }

    @Test
    public void readFrom_InvalidEmptyBuffer() throws Exception {
        try {
            Uleb128.fromByteStream(new ByteArrayInputStream(new byte[]{}));
            Assert.fail();
        } catch (IOException expected) {
            Assert.assertThat(expected.getMessage(), CoreMatchers.containsString("Unexpected EOF"));
        }
    }

    @Test
    public void readFrom_ByteArrayOverflowsLongType() throws Exception {
        byte[] yuuuge = new byte[]{
                (byte) 0xff,
                (byte) 0xff,
                (byte) 0xff,
                (byte) 0xff,
                (byte) 0xff,
                (byte) 0xff,
                (byte) 0xff,
                (byte) 0xff,
                (byte) 0xff,
                (byte) 0x01,
        };
        try {
            Uleb128.fromByteStream(new ByteArrayInputStream(yuuuge));
            Assert.fail();
        } catch (ArithmeticException expected) {
            Assert.assertThat(expected.getMessage(), CoreMatchers.containsString("exceeds maximum value"));
        }
    }

    @Test
    public void asBytes() throws Exception {
        Assert.assertArrayEquals(
                new byte[]{(byte) 0xe5, (byte) 0x8e, (byte) 0x26}, Uleb128.fromLong(624485L).asBytes());
    }

    @Property
    public void decodeFromEncode(long value) throws Exception {
        Assume.assumeThat(value, Matchers.greaterThan(0L));
        Assert.assertEquals(
                value, Uleb128.fromByteStream(new ByteArrayInputStream(Uleb128.fromLong(value).asBytes())).asLong());
    }
}