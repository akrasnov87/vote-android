package ru.mobnius.vote.data.manager.credentials;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class BasicUserUnitTest {
    @Test
    public void Constructor() {
        BasicCredentials c = new BasicCredentials("","");
        BasicUser u = new BasicUser(c, 1, ".admin.master.user.");
        assertTrue(u.userInRole("user"));
        assertTrue(u.userInRole("master"));
        assertTrue(u.userInRole("admin"));
        assertTrue(!u.userInRole("filer"));

        assertTrue(1 == u.getUserId());
    }
}