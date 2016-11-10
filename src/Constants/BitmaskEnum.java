package Constants;

import java.util.EnumSet;
import java.util.stream.Collectors;

/**
 * A helper interface for Enums that represent bitmask vectors.
 */
public interface BitmaskEnum {
    long getMask();

    static <E extends Enum<E> & BitmaskEnum> EnumSet<E> fromMask(Class<E> enumType, long mask) {
        EnumSet<E> values = EnumSet.noneOf(enumType);
        values.addAll(
                EnumSet.allOf(enumType).stream().filter(e -> (e.getMask() & mask) != 0).collect(Collectors.toList()));
        return values;
    }
}
