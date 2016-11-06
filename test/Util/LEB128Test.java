package Util;

import org.junit.Test;

import static org.junit.Assert.*;

public class LEB128Test {
    @Test
    public void decodeULEB128() throws Exception {
        assertEquals(10L, LEB128.decodeULEB128(LEB128.encodeULEB128(10L)));
    }

}