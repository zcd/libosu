package com.zerocooldown.libosu.common;

import java.util.EnumSet;

/**
 * See <a href="https://osu.ppy.sh/wiki/Osr_(file_format)#Data%20Types">osu! replay file format reference.</a>
 */
public enum KeyStroke implements BitmaskEnum {
    /* 0001 */ MOUSE1(1),
    /* 0010 */ MOUSE2(2),
    /* 0101 */ KEY1(5),
    /* 1010 */ KEY2(10);

    private final int mask;

    KeyStroke(int mask) {
        this.mask = mask;
    }

    public static EnumSet<KeyStroke> fromMask(int mask) {
        EnumSet<KeyStroke> keys = EnumSet.noneOf(KeyStroke.class);
        if ((mask & KEY1.getMask()) == KEY1.getMask()) {
            keys.add(KEY1);
        } else if ((mask & MOUSE1.getMask()) == MOUSE1.getMask()) {
            keys.add(MOUSE1);
        }
        if ((mask & KEY2.getMask()) == KEY2.getMask()) {
            keys.add(KEY2);
        } else if ((mask & MOUSE2.getMask()) == MOUSE2.getMask()) {
            keys.add(MOUSE2);
        }
        return keys;
    }

    public int getMask() {
        return mask;
    }
}
