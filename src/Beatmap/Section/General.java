package Beatmap.Section;

import Constants.GameMode;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class General {
    public static Builder builder() {
        return new AutoValue_General.Builder();
    }

    public abstract String audioFilename();

    public abstract int audioLeadIn();

    public abstract int previewTime();

    public abstract boolean countdown();

    public abstract String sampleSet();

    public abstract float stackLeniency();

    public abstract GameMode mode();

    public abstract boolean letterboxInBreaks();

    public abstract boolean widescreenStoryboard();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder audioFilename(String audioFilename);

        public abstract Builder audioLeadIn(int audioLeadIn);

        public abstract Builder previewTime(int previewTime);

        public abstract Builder countdown(boolean countdown);

        public abstract Builder sampleSet(String sampleSet);

        public abstract Builder stackLeniency(float stackLeniency);

        public abstract Builder mode(GameMode mode);

        public abstract Builder letterboxInBreaks(boolean letterboxInBreaks);

        public abstract Builder widescreenStoryboard(boolean widescreenStoryboard);

        public abstract General build();
    }
}
