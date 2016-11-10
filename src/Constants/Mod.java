package Constants;

/**
 * See <a href="https://osu.ppy.sh/wiki/Osr_(file_format)#Data%20Types">osu! replay file format reference.</a>
 */
public enum Mod implements BitmaskEnum {
    NONE(0),
    NO_FAIL(1 << 0),
    EASY(1 << 1),
    NO_VIDEO(1 << 2),
    HIDDEN(1 << 3),
    HARD_ROCK(1 << 4),
    SUDDEN_DEATH(1 << 5),
    DOUBLE_TIME(1 << 6),
    RELAX(1 << 7),
    HALF_TIME(1 << 8),
    NIGHTCORE(1 << 9),
    FLASHLIGHT(1 << 10),
    AUTOPLAY(1 << 11),
    SPUN_OUT(1 << 12),
    RELAX2(1 << 13), // Autopilot
    PERFECT(1 << 14),
    KEY_4(1 << 15),
    KEY_5(1 << 16),
    KEY_6(1 << 17),
    KEY_7(1 << 18),
    KEY_8(1 << 19),
    KEY_MOD(KEY_4.getMask() | KEY_5.getMask() | KEY_6.getMask() | KEY_7.getMask() | KEY_8.getMask()),
    FADE_IN(1 << 20),
    RANDOM(1 << 21),
    LAST_MOD(1 << 22), // Cinema
    TARGET_PRACTICE(1 << 23),
    KEY_9(1 << 24),
    COOP(1 << 25),
    KEY_1(1 << 26),
    KEY_2(1 << 27),
    KEY_3(1 << 28);

    private final long mask;

    Mod(long mask) {
        this.mask = mask;
    }

    public long getMask() {
        return mask;
    }
}
