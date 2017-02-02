package Beatmap.Section;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Difficulty {
    public static Builder builder() {
        return new AutoValue_Difficulty.Builder();
    }

    public abstract float hpDrainRate();

    public abstract float circleSize();

    public abstract float overallDifficulty();

    public abstract float approachRate();

    public abstract float sliderMultiplier();

    public abstract float sliderTickRate();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder hpDrainRate(float hpDrainRate);

        public abstract Builder circleSize(float circleSize);

        public abstract Builder overallDifficulty(float overallDifficulty);

        public abstract Builder approachRate(float approachRate);

        public abstract Builder sliderMultiplier(float sliderMultiplier);

        public abstract Builder sliderTickRate(float sliderTickRate);

        public abstract Difficulty build();
    }
}
