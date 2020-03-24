package ru.mobnius.vote.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class LongUtilTest {

    @Test
    public void convertToLong() {
        assertEquals(LongUtil.convertToLong("123"), 123);
    }
}