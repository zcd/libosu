package com.zerocooldown.libosu.common;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Point {
    public abstract float x();

    public abstract float y();

    public static Point of(float x, float y) {
        return new AutoValue_Point(x, y);
    }

    public static Point of(String x, String y) {
        return of(Float.parseFloat(x), Float.parseFloat(y));
    }
}
