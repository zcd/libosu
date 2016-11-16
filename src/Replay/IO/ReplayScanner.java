package Replay.IO;

import Util.Uleb128;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

/**
 * {@link java.util.Scanner}-like class which emits Java analogues to .osr data-types.
 * <p>
 * Attempting to retrieve data on EOF is an error in all cases.
 */
public final class ReplayScanner implements Closeable, AutoCloseable {
    private final InputStream replay;

    public ReplayScanner(InputStream stream) {
        this.replay = stream;
    }

    public byte nextByte() throws IOException {
        int read;
        if ((read = replay.read()) == -1) {
            throw new IOException("Unexpected EOF");
        }
        return (byte) read;
    }

    public short nextShort() throws IOException {
        return wrapBytes(2).getShort();
    }

    public int nextInteger() throws IOException {
        return wrapBytes(4).getInt();
    }

    public long nextLong() throws IOException {
        return wrapBytes(8).getLong();
    }

    public Uleb128 nextULEB128() throws IOException {
        return Uleb128.fromByteStream(replay);
    }

    /**
     * Reads a string from the underlying stream.
     *
     * @return The next available string in the stream. If the stream begins with a null-byte, this method returns
     *         {@code null}.
     * @throws IOException
     */
    public String nextString() throws IOException {
        byte indicator = nextByte();
        if (indicator == 0x00) {
            return null;
        } else if (indicator != 0x0b) {
            throw new IOException("Unrecognized indicator byte: " + Integer.toHexString(indicator));
        }

        Uleb128 stringLength = nextULEB128();
        byte[] stringBytes = new byte[Math.toIntExact(stringLength.asLong())];
        nextBytes(stringBytes);
        return new String(stringBytes, StandardCharsets.UTF_8);
    }

    /**
     * Attempts to fill in the input byte array with data read from the underlying stream.
     *
     * @param buf
     * @throws IOException if not enough bytes are available to fill the input buffer.
     */
    public void nextBytes(byte[] buf) throws IOException {
        int numBytesRead = replay.read(buf);
        if (numBytesRead == -1 || numBytesRead < buf.length) {
            throw new IOException("Unexpected EOF");
        }
    }

    @Override
    public void close() throws IOException {
        replay.close();
    }

    private ByteBuffer wrapBytes(int numBytes) throws IOException {
        byte[] bytes = new byte[numBytes];
        nextBytes(bytes);
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
    }
}
