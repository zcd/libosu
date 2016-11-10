package Constants;

/**
 * See <a href="https://osu.ppy.sh/wiki/Osr_(file_format)#Data%20Types">osu! replay file format reference.</a>
 */
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
