package Beatmap;

import Beatmap.Section.Colours;
import Beatmap.Section.Difficulty;
import Beatmap.Section.Editor;
import Beatmap.Section.Events;
import Beatmap.Section.General;
import Beatmap.Section.HitObjects;
import Beatmap.Section.Metadata;
import Beatmap.Section.TimingPoints;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Beatmap {
    public abstract General general();

    public abstract Editor editor();

    public abstract Metadata metadata();

    public abstract Difficulty difficulty();

    public abstract Events events();

    public abstract TimingPoints timingPoints();

    public abstract Colours colours();

    public abstract HitObjects hitObjects();

    public static Builder builder() {
        return new AutoValue_Beatmap.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder general(General general);

        public abstract Builder editor(Editor editor);

        public abstract Builder metadata(Metadata metadata);

        public abstract Builder difficulty(Difficulty difficulty);

        public abstract Builder events(Events events);

        public abstract Builder timingPoints(TimingPoints timingPoints);

        public abstract Builder colours(Colours colours);

        public abstract Builder hitObjects(HitObjects hitObjects);

        public abstract Beatmap build();
    }
}
