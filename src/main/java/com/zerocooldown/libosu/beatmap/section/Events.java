package com.zerocooldown.libosu.beatmap.section;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class Events {
    public abstract List<String> lines();

    public static Events create(List<String> lines) {
        return new AutoValue_Events(lines);
    }
}
