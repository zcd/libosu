package com.zerocooldown.libosu.beatmap.datatypes;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class TimingPoint {
    public static Builder builder() {
        return new AutoValue_TimingPoint.Builder();
    }

    public abstract int offset();

    public abstract float millisecondsPerBeat();

    public abstract int meter();

    public abstract int sampleType();

    public abstract int sampleSet();

    public abstract int volume();

    public abstract boolean inherited();

    public abstract boolean kiaiMode();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder offset(int offset);

        public abstract Builder millisecondsPerBeat(float millisecondsPerBeat);

        public abstract Builder meter(int meter);

        public abstract Builder sampleType(int sampleType);

        public abstract Builder sampleSet(int sampleSet);

        public abstract Builder volume(int volume);

        public abstract Builder inherited(boolean inherited);

        public abstract Builder kiaiMode(boolean kiaiMode);

        public abstract TimingPoint build();
    }
}
