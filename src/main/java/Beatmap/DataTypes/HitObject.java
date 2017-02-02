package Beatmap.DataTypes;

import Constants.BitmaskEnum;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class HitObject {
    private static final int MASK_NEW_COMBO = 4;

    public static Builder builder() {
        return new AutoValue_HitObject.Builder();
    }

    public abstract int x();

    public abstract int y();

    public abstract int time();

    public abstract Type type();

    public boolean isNewCombo() {
        return (type().getMask() & MASK_NEW_COMBO) != 0;
    }

    public abstract int hitSound();

    public abstract Addition addition();

    public enum Type implements BitmaskEnum {
        CIRCLE(1), SLIDER(2), SPINNER(8);

        private final int mask;

        Type(int mask) {
            this.mask = mask;
        }

        @Override
        public int getMask() {
            return mask;
        }
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder x(int x);

        public abstract Builder y(int y);

        public abstract Builder time(int time);

        public abstract Builder type(Type type);

        public abstract Builder hitSound(int hitSound);

        public abstract Builder addition(Addition addition);

        public abstract HitObject build();
    }

    public abstract class Addition {
    }
}
