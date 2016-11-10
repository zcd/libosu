package Replay;

import Constants.GameMode;
import Constants.Mod;

import java.util.EnumSet;
import java.util.List;

/**
 * https://osu.ppy.sh/wiki/Osr_(file_format)
 */
public class Replay {
    private final GameMode gameMode;
    private final int gameVersion;
    private final String beatmapHash;

    private final long timestamp;
    private final String replayHash;
    private final String playerName;
    private final EnumSet<Mod> mods;

    private final int totalScore;
    private final short maxCombo;
    private final boolean isPerfect;

    private final short num300;
    private final short num100;
    private final short num50;
    private final short numGeki;
    private final short numKatu;
    private final short numMiss;

    private final List<LifeBarSample> lifebar;
    private final List<Action> events;

    Replay(
            GameMode gameMode,
            int gameVersion,
            String beatmapHash,
            long timestamp,
            String replayHash,
            String playerName,
            EnumSet<Mod> mods,
            int totalScore,
            short maxCombo,
            boolean isPerfect,
            short num300,
            short num100,
            short num50,
            short numGeki,
            short numKatu,
            short numMiss,
            List<LifeBarSample> lifebar,
            List<Action> events) {
        this.gameMode = gameMode;
        this.gameVersion = gameVersion;
        this.beatmapHash = beatmapHash;
        this.timestamp = timestamp;
        this.replayHash = replayHash;
        this.playerName = playerName;
        this.mods = mods;
        this.totalScore = totalScore;
        this.maxCombo = maxCombo;
        this.isPerfect = isPerfect;
        this.num300 = num300;
        this.num100 = num100;
        this.num50 = num50;
        this.numGeki = numGeki;
        this.numKatu = numKatu;
        this.numMiss = numMiss;
        this.lifebar = lifebar;
        this.events = events;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public int getGameVersion() {
        return gameVersion;
    }

    public String getBeatmapHash() {
        return beatmapHash;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getReplayHash() {
        return replayHash;
    }

    public String getPlayerName() {
        return playerName;
    }

    public EnumSet<Mod> getMods() {
        return mods;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public short getMaxCombo() {
        return maxCombo;
    }

    public boolean isPerfect() {
        return isPerfect;
    }

    public short getNum300() {
        return num300;
    }

    public short getNum100() {
        return num100;
    }

    public short getNum50() {
        return num50;
    }

    public short getNumGeki() {
        return numGeki;
    }

    public short getNumKatu() {
        return numKatu;
    }

    public short getNumMiss() {
        return numMiss;
    }

    public List<LifeBarSample> getLifebar() {
        return lifebar;
    }

    public List<Action> getEvents() {
        return events;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder<T extends Builder> {
        private GameMode gameMode;
        private int gameVersion;
        private String beatmapHash;
        private long timestamp;
        private String replayHash;
        private String playerName;
        private EnumSet<Mod> mods;
        private int totalScore;
        private short maxCombo;
        private boolean isPerfect;
        private short num300;
        private short num100;
        private short num50;
        private short numGeki;
        private short numKatu;
        private short numMiss;
        private List<LifeBarSample> lifebar;
        private List<Action> events;

        public Replay.Builder setGameMode(GameMode gameMode) {
            this.gameMode = gameMode;
            return this;
        }

        public Builder setGameVersion(int gameVersion) {
            this.gameVersion = gameVersion;
            return this;
        }

        public Builder setBeatmapHash(String beatmapHash) {
            this.beatmapHash = beatmapHash;
            return this;
        }

        public Builder setTimestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder setReplayHash(String replayHash) {
            this.replayHash = replayHash;
            return this;
        }

        public Builder setPlayerName(String playerName) {
            this.playerName = playerName;
            return this;
        }

        public Builder setMods(EnumSet<Mod> mods) {
            this.mods = mods;
            return this;
        }

        public Builder setTotalScore(int totalScore) {
            this.totalScore = totalScore;
            return this;
        }

        public Builder setMaxCombo(short maxCombo) {
            this.maxCombo = maxCombo;
            return this;
        }

        public Builder setIsPerfect(boolean isPerfect) {
            this.isPerfect = isPerfect;
            return this;
        }

        public Builder setNum300(short num300) {
            this.num300 = num300;
            return this;
        }

        public Builder setNum100(short num100) {
            this.num100 = num100;
            return this;
        }

        public Builder setNum50(short num50) {
            this.num50 = num50;
            return this;
        }

        public Builder setNumGeki(short numGeki) {
            this.numGeki = numGeki;
            return this;
        }

        public Builder setNumKatu(short numKatu) {
            this.numKatu = numKatu;
            return this;
        }

        public Builder setNumMiss(short numMiss) {
            this.numMiss = numMiss;
            return this;
        }

        public Builder setLifebar(List<LifeBarSample> lifebar) {
            this.lifebar = lifebar;
            return this;
        }

        public Builder setEvents(List<Action> events) {
            this.events = events;
            return this;
        }

        public Replay build() {
            return new Replay(
                    gameMode,
                    gameVersion,
                    beatmapHash,
                    timestamp,
                    replayHash,
                    playerName,
                    mods,
                    totalScore,
                    maxCombo,
                    isPerfect,
                    num300,
                    num100,
                    num50,
                    numGeki,
                    numKatu,
                    numMiss,
                    lifebar,
                    events);
        }

        public static Builder fromReplay(Replay source) {
            return new Builder()
                    .setGameMode(source.getGameMode())
                    .setGameVersion(source.getGameVersion())
                    .setBeatmapHash(source.getBeatmapHash())
                    .setTimestamp(source.getTimestamp())
                    .setReplayHash(source.getReplayHash())
                    .setPlayerName(source.getPlayerName())
                    .setMods(source.getMods())
                    .setTotalScore(source.getTotalScore())
                    .setMaxCombo(source.getMaxCombo())
                    .setIsPerfect(source.isPerfect())
                    .setNum300(source.getNum300())
                    .setNum100(source.getNum100())
                    .setNum50(source.getNum50())
                    .setNumGeki(source.getNumGeki())
                    .setNumKatu(source.getNumKatu())
                    .setNumMiss(source.getNumMiss())
                    .setLifebar(source.getLifebar())
                    .setEvents(source.getEvents());
        }
    }
}
