package Replay.IO;

import Constants.GameMode;
import Constants.Mod;
import Replay.Replay;
import org.apache.commons.compress.compressors.lzma.LZMACompressorInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DataFormatException;

public final class ReplayIO {
    /**
     * Parses a {@link Replay} instance from the input data.
     *
     * @param source an {@link InputStream} containing an entire osu! Replay file.
     * @return
     * @throws IOException
     * @throws DataFormatException
     */
    public static Replay read(InputStream source) throws IOException, DataFormatException {
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
        return parse(Replay.builder().setGameMode(mode), scanner);
    }


    public static void write(Replay source, OutputStream sink) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    private static Replay parse(Replay.Builder builder, ReplayScanner scanner) throws IOException, DataFormatException {
        // Game Metadata
        builder.setGameVersion(scanner.nextInteger());
        builder.setBeatmapHash(scanner.nextString());

        // Replay Metadata
        builder.setPlayerName(scanner.nextString());
        builder.setReplayHash(scanner.nextString());  // Possible to validate the replay file?
        builder.setNum300(scanner.nextShort());
        builder.setNum100(scanner.nextShort());
        builder.setNum50(scanner.nextShort());
        builder.setNumGeki(scanner.nextShort());
        builder.setNumKatu(scanner.nextShort());
        builder.setNumMiss(scanner.nextShort());
        builder.setTotalScore(scanner.nextInteger());
        builder.setMaxCombo(scanner.nextShort());
        builder.setIsPerfect(scanner.nextByte() == 1);
        builder.setMods(Mod.fromMask(scanner.nextInteger()));

        // Replay data
        builder.setLifebar(DataStringCodec.toList(scanner.nextString(), DataStringCodec::parseLifeBarSample));
        builder.setTimestamp(scanner.nextLong());

        byte[] replayBytes = new byte[scanner.nextInteger()];
        scanner.nextBytes(replayBytes);
        InputStream decompressed = new LZMACompressorInputStream(new ByteArrayInputStream(replayBytes));
        builder.setMoments(DataStringCodec.toList(decompressed, DataStringCodec::parseMoment));

        builder.setUnknown(scanner.nextLong());
        return builder.build();
    }

}
