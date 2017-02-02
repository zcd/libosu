package Replay;

import com.google.auto.value.AutoValue;

import java.util.List;

/**
 * Data class for a <a href="https://osu.ppy.sh/wiki/Osr_(file_format)">osu! replay file</a>.
 */
@AutoValue
public abstract class Replay {
    public static Builder builder() {
        return new AutoValue_Replay.Builder();
    }

    public abstract Metadata metadata();

    public abstract List<LifeBarSample> lifebar();

    public abstract List<Moment> moments();

    public abstract long unknown();

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setMetadata(Metadata metadata);

        public abstract Builder setLifebar(List<LifeBarSample> lifebar);

        public abstract Builder setMoments(List<Moment> moments);

        public abstract Builder setUnknown(long unknown);

        // TODO(zcd): https://github.com/google/auto/issues/277- implement child classes for other game modes
        public abstract Replay build();
    }
}
