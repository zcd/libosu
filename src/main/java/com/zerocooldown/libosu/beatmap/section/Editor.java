package com.zerocooldown.libosu.beatmap.section;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class Editor {
    public static Builder builder() {
        return new AutoValue_Editor.Builder();
    }

    public abstract List<Integer> bookmarks();

    public abstract float distanceSpacing();

    public abstract int beatDivisor();

    public abstract int gridSize();

    public abstract float timelineZoom();

    @AutoValue.Builder
    public static abstract class Builder {

        public abstract Builder bookmarks(List<Integer> bookmarks);

        public abstract Builder distanceSpacing(float distanceSpacing);

        public abstract Builder beatDivisor(int beatDivisor);

        public abstract Builder gridSize(int gridSize);

        public abstract Builder timelineZoom(float timelineZoom);

        public abstract Editor build();
    }
}
