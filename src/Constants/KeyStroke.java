package Constants;

import java.util.EnumSet;

public enum KeyStroke implements BitmaskEnum {
    MOUSE1(1),
    MOUSE2(2),
    KEY1(5),
    KEY2(10);

    private final int mask;

    KeyStroke(int mask) {
        this.mask = mask;
    }

    public long getMask() {
        return mask;
    }
}
