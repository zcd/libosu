package com.zerocooldown.libosu.beatmap.datatypes;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Colour {

    public abstract int red();

    public abstract int green();

    public abstract int blue();

    public static Colour create(int red, int green, int blue) {
        return new AutoValue_Colour(red, green, blue);
    }
}
