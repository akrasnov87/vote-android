package ru.mobnius.vote.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class ClaimsUtilTest {
    @Test
    public void isExistTest() {
        ClaimsUtil util = new ClaimsUtil(".admin.user.inspector.");
        assertTrue(util.isExists("user"));
        assertFalse(util.isExists("master"));
    }
}