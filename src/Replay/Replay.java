package Replay;

import Constants.GameMode;
import Constants.Mod;
import com.google.auto.value.AutoValue;

import java.util.EnumSet;
import java.util.List;

/**
 * Data class for a osu! Replay file.
 *
 * https://osu.ppy.sh/wiki/Osr_(file_format)
 */
@AutoValue
public abstract class Replay {
    public abstract GameMode gameMode();

    public abstract int gameVersion();

    public abstract String beatmapHash();

    public abstract long timestamp();

    public abstract String replayHash();

    public abstract String playerName();

    public abstract EnumSet<Mod> mods();

    public abstract int totalScore();

    public abstract short maxCombo();

    public abstract boolean isPerfect();

    public abstract short num300();

    public abstract short num100();

    public abstract short num50();

    public abstract short numGeki();

    public abstract short numKatu();

    public abstract short numMiss();

    public abstract List<LifeBarSample> lifebar();

    public abstract List<Action> events();

    public static Builder builder() {
        return new AutoValue_Replay.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setGameMode(GameMode gameMode);

        public abstract Builder setGameVersion(int gameVersion);

        public abstract Builder setBeatmapHash(String beatmapHash);

        public abstract Builder setTimestamp(long timestamp);

        public abstract Builder setReplayHash(String replayHash);

        public abstract Builder setPlayerName(String playerName);

        public abstract Builder setMods(EnumSet<Mod> mods);

        public abstract Builder setTotalScore(int totalScore);

        public abstract Builder setMaxCombo(short maxCombo);

        public abstract Builder setIsPerfect(boolean isPerfect);

        public abstract Builder setNum300(short num300);

        public abstract Builder setNum100(short num100);

        public abstract Builder setNum50(short num50);

        public abstract Builder setNumGeki(short numGeki);

        public abstract Builder setNumKatu(short numKatu);

        public abstract Builder setNumMiss(short numMiss);

        public abstract Builder setLifebar(List<LifeBarSample> lifebar);

        public abstract Builder setEvents(List<Action> events);

        public abstract Replay build();

        public static Builder fromReplay(Replay source) {
            return new AutoValue_Replay.Builder()
                    .setGameMode(source.gameMode())
                    .setGameVersion(source.gameVersion())
                    .setBeatmapHash(source.beatmapHash())
                    .setTimestamp(source.timestamp())
                    .setReplayHash(source.replayHash())
                    .setPlayerName(source.playerName())
                    .setMods(source.mods())
                    .setTotalScore(source.totalScore())
                    .setMaxCombo(source.maxCombo())
                    .setIsPerfect(source.isPerfect())
                    .setNum300(source.num300())
                    .setNum100(source.num100())
                    .setNum50(source.num50())
                    .setNumGeki(source.numGeki())
                    .setNumKatu(source.numKatu())
                    .setNumMiss(source.numMiss())
                    .setLifebar(source.lifebar())
                    .setEvents(source.events());
        }
    }
}
