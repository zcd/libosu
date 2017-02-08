package com.zerocooldown.libosu.beatmap.datatypes;

import com.google.auto.value.AutoValue;

import java.util.List;
import java.util.Optional;

// TODO(zcd): implement full parsing for edgeHitSound, edgeAddition, and addition fields.

@AutoValue
public abstract class HitObject {
    public static final int MASK_NEW_COMBO = 4;

    public abstract Point point();

    public abstract int time();

    public abstract int rawType();

    public Type type() {
        return Type.fromBits(rawType());
    }

    public boolean isNewCombo() {
        return (rawType() & HitObject.MASK_NEW_COMBO) != 0;
    }

    public abstract int hitSound();

    public abstract Optional<String> addition();

    public abstract Optional<Float> spinnerEndTime();

    public abstract Optional<SliderAttributes> sliderAttributes();

    public static Builder builder() {
        return new AutoValue_HitObject.Builder();
    }

    public enum Type {
        CIRCLE, SLIDER, SPINNER;

        public static Type fromBits(int i) {
            int masked = i & ~MASK_NEW_COMBO;
            if ((masked & 1) != 0) {
                return CIRCLE;
            } else if ((masked & 2) != 0) {
                return SLIDER;
            } else if ((masked & 8) != 0) {
                return SPINNER;
            }
            throw new IllegalArgumentException("Unrecognized HitObject type: " + i);
        }
    }

    @AutoValue
    public abstract static class SliderAttributes {
        public abstract SliderType type();

        public abstract List<Point> sliderPoints();

        public abstract int repeat();

        public abstract float pixelLength();

        public abstract Optional<String> edgeHitSound();

        public abstract Optional<String> edgeAddition();

        public static Builder builder() {
            return new AutoValue_HitObject_SliderAttributes.Builder();
        }

        public enum SliderType {
            CATMULL, BEZIER, LINEAR, PERFECT_CURVE;

            public static SliderType of(char c) {
                switch (c) {
                    case 'C':
                        return CATMULL;
                    case 'B':
                        return BEZIER;
                    case 'L':
                        return LINEAR;
                    case 'P':
                        return PERFECT_CURVE;
                    default:
                        throw new IllegalArgumentException("Unrecognized Slider type: " + c);
                }
            }
        }

        @AutoValue.Builder
        public abstract static class Builder {
            public abstract Builder type(SliderType type);

            public abstract Builder sliderPoints(List<Point> sliderPoints);

            public abstract Builder repeat(int repeat);

            public abstract Builder pixelLength(float pixelLength);

            public abstract Builder edgeHitSound(String edgeHitSound);

            public abstract Builder edgeAddition(String edgeAddition);

            public abstract SliderAttributes build();
        }
    }

    @AutoValue
    public abstract static class Point {
        public abstract int x();

        public abstract int y();

        public static Point of(int x, int y) {
            return new AutoValue_HitObject_Point(x, y);
        }

        public static Point of(String x, String y) {
            return of(Integer.parseInt(x), Integer.parseInt(y));
        }
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder point(Point point);

        public abstract Builder time(int time);

        public abstract Builder rawType(int rawType);

        public abstract Builder hitSound(int hitSound);

        public abstract Builder addition(String addition);

        public abstract Builder spinnerEndTime(float spinnerEndTime);

        public abstract Builder sliderAttributes(SliderAttributes sliderAttributes);

        public abstract HitObject build();
    }
}
