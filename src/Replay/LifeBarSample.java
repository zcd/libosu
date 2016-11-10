package Replay;

/**
 * A single lifebar tick.
 */
public final class LifeBarSample {
    private final long offsetMillis;
    private final float lifeFraction;

    /**
     * Constructor.
     *
     * @param offsetMillis milliseconds since the start of the song
     * @param lifeFraction fraction of life at current time
     */
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
