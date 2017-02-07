package com.zerocooldown.libosu.beatmap.section;

import com.google.auto.value.AutoValue;

import java.util.List;
import java.util.Optional;

@AutoValue
public abstract class Metadata {
    public static Builder builder() {
        return new AutoValue_Metadata.Builder();
    }

    public abstract String title();

    public abstract Optional<String> titleUnicode();

    public abstract String artist();

    public abstract Optional<String> artistUnicode();

    public abstract String creator();

    public abstract String version();

    public abstract String source();

    public abstract List<String> tags();

    public abstract int beatmapID();

    public abstract int beatmapSetID();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder title(String title);

        public abstract Builder titleUnicode(String titleUnicode);

        public abstract Builder artist(String artist);

        public abstract Builder artistUnicode(String artistUnicode);

        public abstract Builder creator(String creator);

        public abstract Builder version(String version);

        public abstract Builder source(String source);

        public abstract Builder tags(List<String> tags);

        public abstract Builder beatmapID(int beatmapID);

        public abstract Builder beatmapSetID(int beatmapSetID);

        public abstract Metadata build();
    }
}
