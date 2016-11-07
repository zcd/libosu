package Util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * https://en.wikipedia.org/wiki/LEB128#Unsigned_LEB128
 *
 * Adopted from http://llvm.org/docs/doxygen/html/LEB128_8h_source.html
 */
public final class Uleb128 {
    private final static int BITS_LONG = 64;
    private final static int MASK_DATA = 0x7f;
    private final static int MASK_CONTINUE = 0x80;
    private final long value;

    public static Uleb128 fromByteStream(InputStream bytes) throws IOException {
        return new Uleb128(decode(bytes));
    }

    public static Uleb128 fromLong(long value) {
        return new Uleb128(value);
    }

    public long asLong() {
        return value;
    }

    public byte[] asBytes() {
        return encode(value);
    }

    private Uleb128(long value) {
        this.value = value;
    }

    private static long decode(InputStream bytes) throws IOException {
        long value = 0;
        int bitSize = 0;
        int read;

        do {
            if ((read = bytes.read()) == -1) {
                throw new IOException("Premature EOF");
            }

            value += ((long) read & MASK_DATA) << bitSize;
            bitSize += 7;
            if (bitSize >= BITS_LONG) {
                throw new RuntimeException("ULEB128 value exceeds maximum value for long type.");
            }

        } while ((read & MASK_CONTINUE) != 0);
        return value;
    }

    private static byte[] encode(long value) {
        ArrayList<Byte> bytes = new ArrayList<Byte>();
        do {
            byte b = (byte) (value & MASK_DATA);
            value >>= 7;
            if (value != 0) {
                b |= MASK_CONTINUE;
            }
            bytes.add(b);
        } while(value != 0);

        byte[] ret = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            ret[i] = bytes.get(i).byteValue();
        }
        return ret;
    }
}
