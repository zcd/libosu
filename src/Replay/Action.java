package Replay;

import Constants.KeyStroke;

import java.util.EnumSet;


public final class Action {
    private final long sincePrevActionMillis;
    private final float xPos;
    private final float yPos;
    private final EnumSet<KeyStroke> keys;

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
