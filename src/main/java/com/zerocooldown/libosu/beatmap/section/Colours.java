package com.zerocooldown.libosu.beatmap.section;

import com.zerocooldown.libosu.beatmap.datatypes.Colour;
import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class Colours {

    public abstract List<Colour> colors();

    public static Colours create(List<Colour> colors) {
        return new AutoValue_Colours(colors);
    }
}
