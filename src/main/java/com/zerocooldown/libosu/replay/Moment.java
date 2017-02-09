package com.zerocooldown.libosu.replay;

import com.google.auto.value.AutoValue;
import com.zerocooldown.libosu.common.KeyStroke;
import com.zerocooldown.libosu.common.Point;

import java.util.EnumSet;

/**
 * A single event from the osu! replay file's replay data stream.
 */
@AutoValue
public abstract class Moment {
    public static Moment create(long millisSincePrev, float cursorX, float cursorY, EnumSet<KeyStroke> keys) {
        return new AutoValue_Moment(millisSincePrev, Point.of(cursorX, cursorY), keys);
    }

    public abstract long millisSincePrev();

    public abstract Point cursor();

    public abstract EnumSet<KeyStroke> keys();
}
