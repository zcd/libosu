package com.zerocooldown.libosu.replay.io;

import com.zerocooldown.libosu.util.Uleb128;
import com.google.common.io.LittleEndianDataOutputStream;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Wrapper class around a {@link LittleEndianDataOutputStream} that specially handles osu! replay data types.
 */
public class ReplayWriter implements Closeable, AutoCloseable, Flushable {
    private final LittleEndianDataOutputStream out;

    public ReplayWriter(OutputStream out) {
        this(new LittleEndianDataOutputStream(out));
    }

    public ReplayWriter(LittleEndianDataOutputStream out) {
        this.out = out;
    }

    public void write(byte[] b) throws IOException {
        out.write(b);
    }

    public void writeByte(byte v) throws IOException {
        out.writeByte(v);
    }

    public void writeShort(short v) throws IOException {
        out.writeShort(v);
    }

    public void writeInt(int v) throws IOException {
        out.writeInt(v);
    }

    public void writeLong(long v) throws IOException {
        out.writeLong(v);
    }

    public void writeUleb128(Uleb128 v) throws IOException {
        write(v.asBytes());
    }

    public void writeString(String s) throws IOException {
        if (s == null) {
            writeByte((byte) 0x00);
            return;
        }

        writeByte((byte) 0x0b);
        writeUleb128(Uleb128.fromLong(s.length()));
        write(s.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void close() throws IOException {
        out.close();
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }
}
