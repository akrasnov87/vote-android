package ru.mobnius.vote.utils;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class BooleanUtilTest {

    @Test
    public void isBooleanValue() {
        assertTrue(BooleanUtil.isBooleanValue("true"));
        assertTrue(BooleanUtil.isBooleanValue("false"));
        assertFalse(BooleanUtil.isBooleanValue("0"));
    }

    @Test
    public void isBooleanValueTrue() {
        assertTrue(BooleanUtil.isBooleanValueTrue("true"));
        assertFalse(BooleanUtil.isBooleanValueTrue("0"));
    }

    @Test
    public void convertToBoolean() {
        assertTrue(BooleanUtil.convertToBoolean("true"));
        assertFalse(BooleanUtil.convertToBoolean("false"));
    }
}