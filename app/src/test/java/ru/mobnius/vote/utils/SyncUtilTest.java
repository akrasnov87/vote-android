package ru.mobnius.vote.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class SyncUtilTest {

    @Test
    public void getResetTidParams() {
        Object[] params = SyncUtil.getResetTidParams();
        assertEquals(params.length, 2);
        assertNull(params[0]);
        assertNull(params[1]);
    }

    @Test
    public void getResetTidSqlQuery() {
    }

    @Test
    public void resetTid() {
    }

    @Test
    public void updateTid() {
    }

    @Test
    public void updateBlockTid() {
    }

    @Test
    public void updateTid1() {
    }
}