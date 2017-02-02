package com.zerocooldown.libosu.util;

import com.zerocooldown.libosu.testutil.TestCategories;
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

@Category(TestCategories.UnitTest.class)
@RunWith(JUnitQuickcheck.class)
public class Uleb128Test {
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

    @Property
    public void encodeFromDecode(@From(Uleb128Generator.class) byte[] bytes) throws Exception {
        byte[] recycled = Uleb128.fromLong(
                Uleb128.fromByteStream(new ByteArrayInputStream(bytes)).asLong()).asBytes();
        if (!Arrays.equals(bytes, recycled)) {
            System.out.print(">>>");
            prettyPrintBuffer(bytes);
            System.out.print("\n<<<");
            prettyPrintBuffer(recycled);
        }
        Assert.assertArrayEquals(bytes, recycled);
    }

    private void prettyPrintBuffer(byte[] buf) {
        for (byte aBuf : buf) {
            System.out.print(" " + String.format("%8s", Integer.toBinaryString(aBuf & 0xff)).replace(' ', '0'));
        }
    }

    protected static class Uleb128Generator extends Generator<byte[]> {
        public Uleb128Generator() {
            super(byte[].class);
        }

        @Override
        public byte[] generate(SourceOfRandomness random, GenerationStatus status) {
            byte[] randomBuf = random.nextBytes(random.nextInt(0, 8));
            byte[] buf = new byte[randomBuf.length + 1];
            for (int i = 0; i < randomBuf.length; i++) {
                buf[i] = (byte) (randomBuf[i] | 0x80);
            }
            buf[buf.length - 1] = random.nextByte((byte) 1, Byte.MAX_VALUE);
            return buf;
        }
    }
}