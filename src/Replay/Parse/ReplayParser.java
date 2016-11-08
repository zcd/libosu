package Replay.Parse;

import Constants.BitmaskEnum;
import Constants.KeyStroke;
import Replay.Action;

import java.util.EnumSet;
import java.util.Scanner;

public final class ReplayParser {
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
