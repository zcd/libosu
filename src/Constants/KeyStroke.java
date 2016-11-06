package Constants;

import java.security.Key;
import java.util.EnumSet;

public enum KeyStroke {
    MOUSE1(1),
    MOUSE2(2),
    KEY1(5),
    KEY2(10);

    private final int value;

    KeyStroke(int value) {
        this.value = value;
    }

    public static EnumSet<KeyStroke> fromInt(int bitwise) {
        EnumSet<KeyStroke> keys = EnumSet.noneOf(KeyStroke.class);
        if ((bitwise & MOUSE1.getValue()) != 0) {
            keys.add(MOUSE1);
        }
        if ((bitwise & MOUSE2.getValue()) != 0) {
            keys.add(MOUSE2);
        }
        if ((bitwise & KEY1.getValue()) != 0) {
            keys.add(KEY1);
        }
        if ((bitwise & KEY2.getValue()) != 0) {
            keys.add(KEY2);
        }

        return keys;
    }

    public int getValue() {
        return value;
    }
}
