package com.zerocooldown.libosu.replay;

import com.google.auto.value.AutoValue;

/**
 * A single lifebar tick.
 */
@AutoValue
public abstract class LifeBarSample {
    public static LifeBarSample create(long offsetMillis, float lifeFraction) {
        return new AutoValue_LifeBarSample(offsetMillis, lifeFraction);
    }

    public abstract long offsetMillis();

    public abstract float lifeFraction();
}
