package ru.mobnius.vote.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class IntUtilTest {

    @Test
    public void convertToInt() {
        assertEquals(IntUtil.convertToInt("123"), 123);
    }
}