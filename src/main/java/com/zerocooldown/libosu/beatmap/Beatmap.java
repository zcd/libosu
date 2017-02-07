package com.zerocooldown.libosu.beatmap;

import com.google.auto.value.AutoValue;
import com.zerocooldown.libosu.beatmap.datatypes.Colour;
import com.zerocooldown.libosu.beatmap.datatypes.HitObject;
import com.zerocooldown.libosu.beatmap.datatypes.TimingPoint;
import com.zerocooldown.libosu.beatmap.section.Difficulty;
import com.zerocooldown.libosu.beatmap.section.Editor;
import com.zerocooldown.libosu.beatmap.section.Events;
import com.zerocooldown.libosu.beatmap.section.General;
import com.zerocooldown.libosu.beatmap.section.Metadata;

import java.util.List;
import java.util.Map;

/**
 * Data class for a <a href="https://osu.ppy.sh/wiki/Osu_%28file_format%29">osu! beatmap file</a>.
 */
@AutoValue
public abstract class Beatmap {
    public abstract String osuFormatVersion();

    public abstract General general();

    public abstract Editor editor();

    public abstract Metadata metadata();

    public abstract Difficulty difficulty();

    public abstract Events events();

    public abstract List<TimingPoint> timingPoints();

    public abstract Map<Integer, Colour> colours();

    public abstract List<HitObject> hitObjects();

    public static Builder builder() {
        return new AutoValue_Beatmap.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder osuFormatVersion(String osuFormatVersion);

        public abstract Builder general(General general);

        public abstract Builder editor(Editor editor);

        public abstract Builder metadata(Metadata metadata);

        public abstract Builder difficulty(Difficulty difficulty);

        public abstract Builder events(Events events);

        public abstract Builder timingPoints(List<TimingPoint> timingPoints);

        public abstract Builder colours(Map<Integer, Colour> colours);

        public abstract Builder hitObjects(List<HitObject> hitObjects);

        public abstract Beatmap build();
    }
}
