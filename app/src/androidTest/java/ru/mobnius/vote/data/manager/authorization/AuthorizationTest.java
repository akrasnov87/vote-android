package ru.mobnius.vote.data.manager.authorization;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ru.mobnius.vote.data.GlobalSettings;
import ru.mobnius.vote.data.ICallback;
import ru.mobnius.vote.data.Meta;
import ru.mobnius.vote.data.manager.credentials.BasicCredentials;
import ru.mobnius.vote.data.manager.credentials.BasicUser;

import static org.junit.Assert.*;

public class AuthorizationTest {
    private Context mAppContext;
    private Authorization mAuthorization;

    @Before
    public void setUp() {
        mAppContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        mAuthorization = Authorization.createInstance(mAppContext, "");
    }

    @After
    public void tearDown() {
        mAuthorization.destroy();
        assertFalse(mAuthorization.isAuthorized());
    }

    @Test
    public void isAutoSignIn() {
        mAuthorization.setUser(new BasicUser(new BasicCredentials(GlobalSettings.DEFAULT_USER_NAME, GlobalSettings.DEFAULT_USER_PASSWORD), 1, ".user.inspector."));
        assertTrue(mAuthorization.isAuthorized());
        assertTrue(mAuthorization.isAutoSignIn());
        mAuthorization.setUser(new BasicUser(new BasicCredentials("temp", "temp0"), 1, ".user.inspector."));
        assertFalse(mAuthorization.isAutoSignIn());
        assertNull(mAuthorization.getLastAuthUser());
        mAuthorization.reset();
        assertFalse(mAuthorization.isAuthorized());
    }

    @Test
    public void isInspector() {
        mAuthorization.setUser(new BasicUser(new BasicCredentials(GlobalSettings.DEFAULT_USER_NAME, GlobalSettings.DEFAULT_USER_PASSWORD), 1, ".user.inspector."));
        assertTrue(mAuthorization.isInspector());
        mAuthorization.setUser(new BasicUser(new BasicCredentials(GlobalSettings.DEFAULT_USER_NAME, GlobalSettings.DEFAULT_USER_PASSWORD), 1, ".user.admin."));
        assertFalse(mAuthorization.isInspector());
    }

    @Test
    public void getLastAuthUser() {
        mAuthorization.setUser(new BasicUser(new BasicCredentials(GlobalSettings.DEFAULT_USER_NAME, GlobalSettings.DEFAULT_USER_PASSWORD), 1, ".user.inspector."));
        BasicUser basicUser = mAuthorization.getLastAuthUser();
        assertEquals(basicUser.getCredentials().login, GlobalSettings.DEFAULT_USER_NAME);
    }
}