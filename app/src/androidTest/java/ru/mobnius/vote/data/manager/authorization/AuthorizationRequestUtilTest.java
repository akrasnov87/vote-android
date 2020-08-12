package ru.mobnius.vote.data.manager.authorization;

import org.junit.Test;

import ru.mobnius.vote.ManagerGenerate;
import ru.mobnius.vote.SimpleTest;

import static org.junit.Assert.*;


public class AuthorizationRequestUtilTest extends SimpleTest {
    @Test
    public void requestTest() {
        AuthorizationRequestUtil util = new AuthorizationRequestUtil(ManagerGenerate.getBaseUrl());
        AuthorizationMeta meta = util.request(ManagerGenerate.getCredentials().login, ManagerGenerate.getCredentials().password);
        assertTrue(meta.isSuccess());
        meta = util.request("inspector", "inspector");
        assertFalse(meta.isSuccess());
    }

    @Test
    public void SuccessReadTest() {
        String successResult = "{\"token\": \"cm9vdDpyb290MA==\",\"user\": {\"userId\": 1,\"claims\": \".master.admin.filer.\"}}";
        AuthorizationRequestUtil util = new AuthorizationRequestUtil("");
        AuthorizationMeta meta = util.convertResponseToMeta(successResult);
        assertEquals("cm9vdDpyb290MA==", meta.getToken());
        assertEquals(1, meta.getUserId().intValue());
        assertEquals(".master.admin.filer.", meta.getClaims());
        assertEquals(200, meta.getStatus());
        assertTrue(meta.isSuccess());
    }

    @Test
    public void FailReadTest() {
        String failResult = "{\"code\": 401,\"meta\": {\"success\": false,\"msg\": \"Пользователь не авторизован.\"}}";

        AuthorizationRequestUtil util = new AuthorizationRequestUtil("");
        AuthorizationMeta meta = util.convertResponseToMeta(failResult);

        assertFalse(meta.getMessage().isEmpty());
        assertEquals(401, meta.getStatus());
        assertFalse(meta.isSuccess());
    }
}