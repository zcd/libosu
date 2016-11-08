package Replay.Parse;

import Util.Uleb128;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.zip.DataFormatException;

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

    public String nextString() throws IOException, DataFormatException {
        byte indicator = nextByte();
        if (indicator == 0x00) {
            return null;
        } else if (indicator != 0x0b) {
            throw new DataFormatException("Unrecognized indicator byte: " + Integer.toHexString(indicator));
        }

        Uleb128 stringLength = nextULEB128();
        byte[] stringBytes = new byte[Math.toIntExact(stringLength.asLong())];
        nextBytes(stringBytes);
        return new String(stringBytes, StandardCharsets.UTF_8);
    }

    public void nextBytes(byte[] bytes) throws IOException {
        int numBytesRead = replay.read(bytes);
        if (numBytesRead == -1 || numBytesRead < bytes.length) {
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
