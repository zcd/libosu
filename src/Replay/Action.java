package Replay;

import Constants.KeyStroke;
import com.google.common.base.Preconditions;

import java.util.EnumSet;

/**
 * A single event from the osu! replay file's replay data stream.
 */
public final class Action {
    private final long sincePrevActionMillis;
    private final float xPos;
    private final float yPos;
    private final EnumSet<KeyStroke> keys;

    /**
     * Constructor.
     *
     * @param sincePrevActionMillis
     * @param xPos cursor coordinate between 0 and 512
     * @param yPos cursor coordinate between 0 and 384
     * @param keys combination of keys/mouse buttons pressed
     */
    public Action(long sincePrevActionMillis, float xPos, float yPos, EnumSet<KeyStroke> keys) {
        this.sincePrevActionMillis = sincePrevActionMillis;
        this.xPos = xPos;
        this.yPos = yPos;
        this.keys = keys;
    }

    public long getMillisSincePrevAction() {
        return sincePrevActionMillis;
    }

    public float getX() {
        return xPos;
    }

    public float getY() {
        return yPos;
    }

    public EnumSet<KeyStroke> getKeyPresses() {
        return keys;
    }
}
