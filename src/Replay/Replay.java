package Replay;

import Constants.GameMode;
import Constants.Mod;
import com.google.auto.value.AutoValue;

import java.util.EnumSet;
import java.util.List;

/**
 * Data class for a <a href="https://osu.ppy.sh/wiki/Osr_(file_format)">osu! replay file</a>.
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

    public abstract Builder toBuilder();

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

        // TODO(zcd): https://github.com/google/auto/issues/277- implement child classes for other game modes
        public abstract Replay build();
    }
}
