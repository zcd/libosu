package com.zerocooldown.libosu.constants;

import java.util.EnumSet;

/**
 * A helper interface for Enums that represent bitmask vectors.
 */
public interface BitmaskEnum {
    static <E extends Enum<E> & BitmaskEnum> int toMask(EnumSet<E> collection) {
        //noinspection RedundantCast - http://stackoverflow.com/a/34160332
        return collection.stream().mapToInt((E e) -> ((BitmaskEnum) e).getMask()).reduce(0, (a, b) -> a | b);
    }

    int getMask();
}
