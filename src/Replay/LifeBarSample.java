package Replay;

public final class LifeBarSample {
    private final long offsetMillis;
    private final float lifeFraction;

    public LifeBarSample(long offsetMillis, float lifeFraction) {
        this.offsetMillis = offsetMillis;
        this.lifeFraction = lifeFraction;
    }

    public long getOffsetMillis() {
        return offsetMillis;
    }

    public float getLifeFraction() {
        return lifeFraction;
    }
}
