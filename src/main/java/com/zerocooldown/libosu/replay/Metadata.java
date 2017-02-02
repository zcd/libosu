package com.zerocooldown.libosu.replay;

import com.zerocooldown.libosu.constants.GameMode;
import com.zerocooldown.libosu.constants.Mod;
import com.google.auto.value.AutoValue;

import java.util.EnumSet;

@AutoValue
public abstract class Metadata {
    public static Builder builder() {
        return new AutoValue_Metadata.Builder();
    }

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

        public abstract Metadata build();
    }
}
