package Replay.Parse;

import TestUtil.TestUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;

@SuppressWarnings({"PointlessArithmeticExpression", "PointlessBitwiseExpression"})
public class ReplayScannerTest {
    @Test
    public void next_EmptyBytes() throws Exception {
        InputStream testBytes = buildTestByteStream(new int[]{});
        try (ReplayScanner scanner = new ReplayScanner(testBytes)) {
            TestUtil.assertThrows(scanner::nextByte, IOException.class,
                    TestUtil.exceptionMessageMatches("Unexpected EOF"));
        }
        try (ReplayScanner scanner = new ReplayScanner(testBytes)) {
            TestUtil.assertThrows(scanner::nextShort, IOException.class,
                    TestUtil.exceptionMessageMatches("Unexpected EOF"));
        }
        try (ReplayScanner scanner = new ReplayScanner(testBytes)) {
            TestUtil.assertThrows(scanner::nextInteger, IOException.class,
                    TestUtil.exceptionMessageMatches("Unexpected EOF"));
        }
        try (ReplayScanner scanner = new ReplayScanner(testBytes)) {
            TestUtil.assertThrows(scanner::nextLong, IOException.class,
                    TestUtil.exceptionMessageMatches("Unexpected EOF"));
        }
        try (ReplayScanner scanner = new ReplayScanner(testBytes)) {
            TestUtil.assertThrows(scanner::nextString, IOException.class,
                    TestUtil.exceptionMessageMatches("Unexpected EOF"));
        }
        try (ReplayScanner scanner = new ReplayScanner(testBytes)) {
            TestUtil.assertThrows(scanner::nextULEB128, IOException.class,
                    TestUtil.exceptionMessageMatches("Unexpected EOF"));
        }
    }

    @Test
    public void nextByte() throws Exception {
        InputStream testBytes = buildTestByteStream(new int[]{0x01});
        try (ReplayScanner scanner = new ReplayScanner(testBytes)) {
            Assert.assertEquals(0x01, scanner.nextByte());
        }
    }

    @Test
    public void nextByte_EarlyTermination() throws Exception {
        InputStream testBytes = buildTestByteStream(new int[]{0x01});
        try (ReplayScanner scanner = new ReplayScanner(testBytes)) {
            Assert.assertEquals(0x01, scanner.nextByte());
            TestUtil.assertThrows(scanner::nextByte, IOException.class,
                    TestUtil.exceptionMessageMatches("Unexpected EOF"));
        }
    }

    @Test
    public void nextShort() throws Exception {
        InputStream testBytes = buildTestByteStream(new int[]{0x01, 0x02});
        try (ReplayScanner scanner = new ReplayScanner(testBytes)) {
            Assert.assertEquals((0x02 << 8) + 0x01, scanner.nextShort());
        }
    }

    @Test
    public void nextShort_EarlyTermination() throws Exception {
        try (ReplayScanner scanner = new ReplayScanner(
                buildTestByteStream(new int[]{0x01, 0x02})
        )) {
            Assert.assertEquals((0x02 << 8) + 0x01, scanner.nextShort());
            TestUtil.assertThrows(scanner::nextShort, IOException.class,
                    TestUtil.exceptionMessageMatches("Unexpected EOF"));
        }

        try (ReplayScanner scanner = new ReplayScanner(
                buildTestByteStream(new int[]{0x01, 0x02, 0x03})
        )) {
            Assert.assertEquals((0x02 << 8) + 0x01, scanner.nextShort());
            TestUtil.assertThrows(scanner::nextShort, IOException.class,
                    TestUtil.exceptionMessageMatches("Unexpected EOF"));
        }
    }

    @Test
    public void nextInteger() throws Exception {
        InputStream testBytes = buildTestByteStream(new int[]{0x01, 0x02, 0x03, 0x04});
        try (ReplayScanner scanner = new ReplayScanner(testBytes)) {
            Assert.assertEquals(
                    (0x04 << 8 * 3) + (0x03 << 8 * 2) + (0x02 << 8 * 1) + (0x01 << 8 * 0),
                    scanner.nextInteger());
        }
    }

    @Test
    public void nextInteger_EarlyTermination() throws Exception {
        try (ReplayScanner scanner = new ReplayScanner(
                buildTestByteStream(new int[]{0x01, 0x02, 0x03, 0x04, 0x05})
        )) {
            Assert.assertEquals(
                    (0x04 << 8 * 3) + (0x03 << 8 * 2) + (0x02 << 8 * 1) + (0x01 << 8 * 0),
                    scanner.nextInteger());
            TestUtil.assertThrows(scanner::nextInteger, IOException.class,
                    TestUtil.exceptionMessageMatches("Unexpected EOF"));
        }

        try (ReplayScanner scanner = new ReplayScanner(
                buildTestByteStream(new int[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06})
        )) {
            Assert.assertEquals(
                    (0x04 << 8 * 3) + (0x03 << 8 * 2) + (0x02 << 8 * 1) + (0x01 << 8 * 0),
                    scanner.nextInteger());
            TestUtil.assertThrows(scanner::nextInteger, IOException.class,
                    TestUtil.exceptionMessageMatches("Unexpected EOF"));
        }

        try (ReplayScanner scanner = new ReplayScanner(
                buildTestByteStream(new int[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07})
        )) {
            Assert.assertEquals(
                    (0x04 << 8 * 3) + (0x03 << 8 * 2) + (0x02 << 8 * 1) + (0x01 << 8 * 0),
                    scanner.nextInteger());
            TestUtil.assertThrows(scanner::nextInteger, IOException.class,
                    TestUtil.exceptionMessageMatches("Unexpected EOF"));
        }
    }

    @Test
    public void nextLong() throws Exception {
        InputStream testBytes = buildTestByteStream(new int[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08});
        try (ReplayScanner scanner = new ReplayScanner(testBytes)) {
            Assert.assertEquals(
                    (0x08L << (8 * 7)) + (0x07L << (8 * 6)) + (0x06L << (8 * 5)) + (0x05L << (8 * 4)) +
                            (0x04L << (8 * 3)) + (0x03L << (8 * 2)) + (0x02L << (8 * 1)) + (0x01L << (8 * 0)),
                    scanner.nextLong());
        }
    }

    @Test
    public void nextLong_EarlyTermination() throws Exception {
        try (ReplayScanner scanner = new ReplayScanner(
                buildTestByteStream(new int[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09})
        )) {
            Assert.assertEquals(
                    (0x08L << (8 * 7)) + (0x07L << (8 * 6)) + (0x06L << (8 * 5)) + (0x05L << (8 * 4)) +
                            (0x04L << (8 * 3)) + (0x03L << (8 * 2)) + (0x02L << (8 * 1)) + (0x01L << (8 * 0)),
                    scanner.nextLong());
            TestUtil.assertThrows(scanner::nextLong, IOException.class,
                    TestUtil.exceptionMessageMatches("Unexpected EOF"));
        }

        try (ReplayScanner scanner = new ReplayScanner(
                buildTestByteStream(new int[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x10})
        )) {
            Assert.assertEquals(
                    (0x08L << (8 * 7)) + (0x07L << (8 * 6)) + (0x06L << (8 * 5)) + (0x05L << (8 * 4)) +
                            (0x04L << (8 * 3)) + (0x03L << (8 * 2)) + (0x02L << (8 * 1)) + (0x01L << (8 * 0)),
                    scanner.nextLong());
            TestUtil.assertThrows(scanner::nextLong, IOException.class,
                    TestUtil.exceptionMessageMatches("Unexpected EOF"));
        }

        try (ReplayScanner scanner = new ReplayScanner(
                buildTestByteStream(new int[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x10, 0x11})
        )) {
            Assert.assertEquals(
                    (0x08L << (8 * 7)) + (0x07L << (8 * 6)) + (0x06L << (8 * 5)) + (0x05L << (8 * 4)) +
                            (0x04L << (8 * 3)) + (0x03L << (8 * 2)) + (0x02L << (8 * 1)) + (0x01L << (8 * 0)),
                    scanner.nextLong());
            TestUtil.assertThrows(scanner::nextLong, IOException.class,
                    TestUtil.exceptionMessageMatches("Unexpected EOF"));
        }

        try (ReplayScanner scanner = new ReplayScanner(
                buildTestByteStream(new int[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x10, 0x11, 0x12})
        )) {
            Assert.assertEquals(
                    (0x08L << (8 * 7)) + (0x07L << (8 * 6)) + (0x06L << (8 * 5)) + (0x05L << (8 * 4)) +
                            (0x04L << (8 * 3)) + (0x03L << (8 * 2)) + (0x02L << (8 * 1)) + (0x01L << (8 * 0)),
                    scanner.nextLong());
            TestUtil.assertThrows(scanner::nextLong, IOException.class,
                    TestUtil.exceptionMessageMatches("Unexpected EOF"));
        }

        try (ReplayScanner scanner = new ReplayScanner(
                buildTestByteStream(new int[]{
                        0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x10, 0x11, 0x12, 0x13})
        )) {
            Assert.assertEquals(
                    (0x08L << (8 * 7)) + (0x07L << (8 * 6)) + (0x06L << (8 * 5)) + (0x05L << (8 * 4)) +
                            (0x04L << (8 * 3)) + (0x03L << (8 * 2)) + (0x02L << (8 * 1)) + (0x01L << (8 * 0)),
                    scanner.nextLong());
            TestUtil.assertThrows(scanner::nextLong, IOException.class,
                    TestUtil.exceptionMessageMatches("Unexpected EOF"));
        }
    }

    @Test
    public void nextUleb128() throws Exception {
        //      0x2      0x1
        //        1000000001  Raw binary
        //    00001000000001  Padded to len%7==0
        //  0000100  0000001  Split into groups
        // 00000100 10000001  Add continuation bits
        //      0x4     0x81
        long value = (0x2 << 8 * 1) + (0x1 << 8 * 0);
        int[] bytes = {0x81, 0x4};

        try (ReplayScanner scanner = new ReplayScanner(buildTestByteStream(bytes))) {
            Assert.assertEquals(value, scanner.nextULEB128().asLong());
        }
    }

    @Test
    public void nextUleb128_EarlyTermination() throws Exception {
        long value = (0x2 << 8 * 1) + (0x1 << 8 * 0);
        int[] bytes = {0x81, 0x4, 0xff};

        try (ReplayScanner scanner = new ReplayScanner(buildTestByteStream(bytes))) {
            Assert.assertEquals(value, scanner.nextULEB128().asLong());
            TestUtil.assertThrows(scanner::nextULEB128, IOException.class,
                    TestUtil.exceptionMessageMatches("Unexpected EOF"));
        }
    }

    @Test
    public void nextString_NullString() throws Exception {
        int[] bytes = {0x0, 0x4};

        try (ReplayScanner scanner = new ReplayScanner(buildTestByteStream(bytes))) {
            Assert.assertNull(scanner.nextString());
            Assert.assertEquals((byte) 0x4, scanner.nextByte());
        }
    }

    @Test
    public void nextString_EmptyString() throws Exception {
        // String is present.
        //              |    String has byte length of zero.
        //              |     |     Next data value.
        //              |     |     |
        //              V     V     V
        int[] bytes = {0x0b, 0x00, 0x04};

        try (ReplayScanner scanner = new ReplayScanner(buildTestByteStream(bytes))) {
            Assert.assertEquals("", scanner.nextString());
            Assert.assertEquals((byte) 0x4, scanner.nextByte());
        }
    }

    @Test
    public void nextString() throws Exception {
        int[] bytes = {0x0b, 0x0b, 0x68, 0x65, 0x6c, 0x6c, 0x6f, 0x20, 0x77, 0x6f, 0x72, 0x6c, 0x64};

        try (ReplayScanner scanner = new ReplayScanner(buildTestByteStream(bytes))) {
            Assert.assertEquals("hello world", scanner.nextString());
        }
    }

    @Test
    public void nextString_MalformedData() throws Exception {
        try (ReplayScanner scanner = new ReplayScanner(buildTestByteStream(new int[]{0xde, 0xad, 0xbe, 0xef}))) {
            TestUtil.assertThrows(scanner::nextString, DataFormatException.class,
                    TestUtil.exceptionMessageMatches("Unrecognized indicator byte: .*"));
        }
    }

    @Test
    public void nextString_EarlyTermination() throws Exception {
        try (ReplayScanner scanner = new ReplayScanner(buildTestByteStream(new int[]{0x0b}))) {
            TestUtil.assertThrows(scanner::nextString, IOException.class,
                    TestUtil.exceptionMessageMatches("Unexpected EOF"));
        }

        try (ReplayScanner scanner = new ReplayScanner(buildTestByteStream(new int[]{0x0b, 0x00, 0x0b, 0x4, 0x2}))) {
            Assert.assertEquals("", scanner.nextString());
            TestUtil.assertThrows(scanner::nextString, IOException.class,
                    TestUtil.exceptionMessageMatches("Unexpected EOF"));
        }
    }

    @Test
    public void nextBytes() throws Exception {
        byte[] buf = new byte[6];

        try (ReplayScanner scanner = new ReplayScanner(
                buildTestByteStream(new int[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06}))) {
            scanner.nextBytes(buf);
            Assert.assertArrayEquals(
                    new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06}, buf);
        }
    }

    @Test
    public void nextBytes_EarlyTermination() throws Exception {
        byte[] buf = new byte[6];

        try (ReplayScanner scanner = new ReplayScanner(buildTestByteStream(new int[]{0x0b}))) {
            TestUtil.assertThrows(() -> scanner.nextBytes(buf), IOException.class,
                    TestUtil.exceptionMessageMatches("Unexpected EOF"));
        }
    }

    private ByteArrayInputStream buildTestByteStream(int[] byteArray) {
        byte[] bytes = new byte[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            bytes[i] = (byte) byteArray[i];
        }

        return new ByteArrayInputStream(bytes);
    }

}