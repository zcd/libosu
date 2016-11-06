package Util;

import java.util.ArrayList;

/**
 * Adopted from http://llvm.org/docs/doxygen/html/LEB128_8h_source.html
 */
public final class LEB128 {
    public static class OverflowException extends RuntimeException {
        public OverflowException(String s) {
            super(s);
        }
    }

    private static final int MAX_BYTES_LONG = 8;

    public static long decodeULEB128(byte[] bytes) {
        if (bytes.length > MAX_BYTES_LONG) {
            throw new OverflowException("Value does not fit in a long.");
        }

        long value = 0;
        int shift = 0;
        for (byte b : bytes) {
            value |= ((b & 0x7f) << shift);
            shift += 7;
        }

        return value;
    }

    public static byte[] encodeULEB128(long value) {
        ArrayList<Byte> bytes = new ArrayList<Byte>();
        do {
            byte b = (byte) (value & 0x7f);
            value >>= 7;
            if (value != 0) {
                b |= 0x80; // Mark this byte to show that more bytes will follow.
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
