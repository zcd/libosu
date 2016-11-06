package Constants;

public enum GameMode {
    STANDARD((byte) 0x0),
    TAIKO((byte) 0x1),
    CATCH_THE_BEAT((byte) 0x2),
    MANIA((byte) 0x3);

    private final byte value;

    GameMode(byte value) {
        this.value = value;
    }
}
