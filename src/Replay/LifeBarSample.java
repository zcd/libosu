package Replay;

import com.google.auto.value.AutoValue;

/**
 * A single lifebar tick.
 */
@AutoValue
public abstract class LifeBarSample {
    public abstract long offsetMillis();

    public abstract float lifeFraction();

    public static LifeBarSample create(long offsetMillis, float lifeFraction) {
        return new AutoValue_LifeBarSample(offsetMillis, lifeFraction);
    }
}
