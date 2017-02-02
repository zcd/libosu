package com.zerocooldown.libosu.replay;

import com.zerocooldown.libosu.constants.KeyStroke;
import com.google.auto.value.AutoValue;

import java.util.EnumSet;

/**
 * A single event from the osu! replay file's replay data stream.
 */
@AutoValue
public abstract class Moment {
    public static Moment create(long millisSincePrev, float cursorX, float cursorY, EnumSet<KeyStroke> keys) {
        return new AutoValue_Moment(millisSincePrev, cursorX, cursorY, keys);
    }

    public abstract long millisSincePrev();

    public abstract float cursorX();

    public abstract float cursorY();

    public abstract EnumSet<KeyStroke> keys();
}
