package Util;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Tests for Util.Uleb128
 */
public class Uleb128Test {
    // TODO(zcd): fuzz testing?
    @Test
    public void readFrom() throws Exception {
        Uleb128 parsed = Uleb128.fromByteStream(
                new ByteArrayInputStream(new byte[] {(byte) 0xe5, (byte) 0x8e, (byte) 0x26}));
        Assert.assertEquals(624485L, parsed.asLong());
    }

    @Test
    public void readFrom_InvalidEmptyBuffer() throws Exception {
        try {
            Uleb128.fromByteStream(new ByteArrayInputStream(new byte[] {}));
            Assert.fail();
        } catch (IOException expected) {
            Assert.assertThat(expected.getMessage(), CoreMatchers.containsString("Unexpected EOF"));
        }
    }

    @Test
    public void readFrom_ByteArrayOverflowsLongType() throws Exception {
        byte[] yuuuge = new byte[] {
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
                new byte[] {(byte) 0xe5, (byte) 0x8e, (byte) 0x26}, Uleb128.fromLong(624485L).asBytes());
    }
}