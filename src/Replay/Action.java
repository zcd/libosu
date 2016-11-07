package Replay;

import Constants.BitmaskEnum;
import Constants.KeyStroke;
import com.google.common.collect.ImmutableList;

import javax.management.ImmutableDescriptor;
import java.util.*;

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

    public static List<Action> parseActions(String input) {
        ImmutableList.Builder<Action> builder = ImmutableList.builder();
        try (Scanner scanner = new Scanner(input).useDelimiter(",")) {
            while (scanner.hasNext()) {
                builder.add(parseSingleAction(scanner.next()));
            }
            return builder.build();
        }
    }

    public static Action parseSingleAction(String input) {
        try (Scanner scanner = new Scanner(input).useDelimiter("[|]")) {
            long millis = scanner.nextLong();
            float x = scanner.nextFloat();
            float y = scanner.nextFloat();
            EnumSet<KeyStroke> keys = BitmaskEnum.fromMask(KeyStroke.class, scanner.nextInt());

            return new Action(millis, x, y, keys);
        }
    }
}
