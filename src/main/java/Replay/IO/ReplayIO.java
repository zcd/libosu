package Replay.IO;

import Constants.BitmaskEnum;
import Constants.GameMode;
import Constants.Mod;
import Replay.Metadata;
import Replay.Replay;
import lzma.sdk.lzma.Decoder;
import lzma.streams.LzmaInputStream;
import lzma.streams.LzmaOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class ReplayIO {
    /**
     * Parses a {@link Replay} instance from the input data.
     *
     * @param source an {@link InputStream} containing an entire osu! Replay file.
     * @return a parsed {@link Replay} instance.
     * @throws IOException if an exception occurs when reading the stream.
     */
    public static Replay read(InputStream source) throws IOException {
        ReplayScanner scanner = new ReplayScanner(source);
        GameMode mode;
        byte modeByte = scanner.nextByte();
        switch (modeByte) {
            case 0:
                mode = GameMode.STANDARD;
                break;
            case 1:
                mode = GameMode.TAIKO;
                break;
            case 2:
                mode = GameMode.CATCH_THE_BEAT;
                break;
            case 3:
                mode = GameMode.MANIA;
                break;
            default:
                throw new IllegalArgumentException("Unrecognized game mode byte: " + modeByte);
        }
        return parse(mode, scanner);
    }

    /**
     * Writes a {@link Replay} into the sink.
     *
     * @param source {@link Replay} instance to write.
     * @param sink   output stream to write to.
     * @throws IOException if an error occurs on writing to the stream.
     */
    public static void write(Replay source, OutputStream sink) throws IOException {
        ReplayWriter writer = new ReplayWriter(sink);

        Metadata metadata = source.metadata();
        writer.writeByte((byte) metadata.gameMode().ordinal());
        writer.writeInt(metadata.gameVersion());
        writer.writeString(metadata.beatmapHash());
        writer.writeString(metadata.playerName());
        writer.writeString(metadata.replayHash());
        writer.writeShort(metadata.num300());
        writer.writeShort(metadata.num100());
        writer.writeShort(metadata.num50());
        writer.writeShort(metadata.numGeki());
        writer.writeShort(metadata.numKatu());
        writer.writeShort(metadata.numMiss());
        writer.writeInt(metadata.totalScore());
        writer.writeShort(metadata.maxCombo());
        writer.writeByte((byte) (metadata.isPerfect() ? 1 : 0));
        writer.writeInt(BitmaskEnum.toMask(metadata.mods()));

        writer.writeString(DataStringCodec.toEncodedString(source.lifebar(), DataStringCodec::encodeLifeBarSample));
        writer.writeLong(metadata.timestamp());

        byte[] replayData = compress(DataStringCodec.toEncodedString(source.moments(), DataStringCodec::encodeMoment));
        writer.writeInt(replayData.length);
        writer.write(replayData);
        writer.writeLong(source.unknown());
    }

    private static byte[] compress(String s) throws IOException {
        ByteArrayOutputStream outBuf = new ByteArrayOutputStream();
        try (OutputStream zip = new LzmaOutputStream.Builder(outBuf).build()) {
            zip.write(s.getBytes());
        }
        return outBuf.toByteArray();
    }

    private static Replay parse(GameMode mode, ReplayScanner scanner) throws IOException {
        Replay.Builder replay = Replay.builder();
        Metadata.Builder metadata = Metadata.builder();

        metadata.setGameMode(mode);
        metadata.setGameVersion(scanner.nextInteger());
        metadata.setBeatmapHash(scanner.nextString());
        metadata.setPlayerName(scanner.nextString());
        metadata.setReplayHash(scanner.nextString());  // Possible to validate the replay file?
        metadata.setNum300(scanner.nextShort());
        metadata.setNum100(scanner.nextShort());
        metadata.setNum50(scanner.nextShort());
        metadata.setNumGeki(scanner.nextShort());
        metadata.setNumKatu(scanner.nextShort());
        metadata.setNumMiss(scanner.nextShort());
        metadata.setTotalScore(scanner.nextInteger());
        metadata.setMaxCombo(scanner.nextShort());
        metadata.setIsPerfect(scanner.nextByte() == 1);
        metadata.setMods(Mod.fromMask(scanner.nextInteger()));

        replay.setLifebar(DataStringCodec.toList(scanner.nextString(), DataStringCodec::parseLifeBarSample));
        metadata.setTimestamp(scanner.nextLong());

        byte[] replayBytes = new byte[scanner.nextInteger()];
        scanner.nextBytes(replayBytes);
        InputStream decompressed = new LzmaInputStream(new ByteArrayInputStream(replayBytes), new Decoder());
        replay.setMoments(DataStringCodec.toList(decompressed, DataStringCodec::parseMoment));
        replay.setUnknown(scanner.nextLong());

        return replay.setMetadata(metadata.build()).build();
    }

}
