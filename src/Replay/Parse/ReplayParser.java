package Replay.Parse;

import Constants.BitmaskEnum;
import Constants.KeyStroke;
import Replay.Action;
import Replay.LifeBarSample;

import java.util.EnumSet;
import java.util.Scanner;

public final class ReplayParser {
    static LifeBarSample parseLifeBarSample(String input) {
        return DataParsing.toTuple(input, (Scanner scanner) -> {
            long u = scanner.nextLong();
            float v = scanner.nextFloat();
            return new LifeBarSample(u, v);
        });
    }

    static Action parseAction(String input) {
        return DataParsing.toTuple(input, (Scanner scanner) -> {
            long millis = scanner.nextLong();
            float x = scanner.nextFloat();
            float y = scanner.nextFloat();
            EnumSet<KeyStroke> keys = BitmaskEnum.fromMask(KeyStroke.class, scanner.nextInt());

            return new Action(millis, x, y, keys);
        });
    }
}
