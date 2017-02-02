package com.zerocooldown.libosu.beatmap.section;

import com.zerocooldown.libosu.beatmap.datatypes.HitObject;
import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class HitObjects {
    public abstract List<HitObject> hitObjects();

    public static HitObjects create(List<HitObject> hitObjects) {
        return new AutoValue_HitObjects(hitObjects);
    }
}
