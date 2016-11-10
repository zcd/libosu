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
        Preconditions.checkArgument(0 <= xPos && xPos <= 512, "Cursor x-coordinate ∉[0,512]");
        Preconditions.checkArgument(0 <= yPos && yPos <= 384, "Cursor y-coordinate ∉[0,512]");

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
