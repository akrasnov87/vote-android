package ru.mobnius.vote.data.manager;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import ru.mobnius.vote.SimpleTest;

import static org.junit.Assert.*;

public class MultiUsersTest extends SimpleTest {
    private MultiUsers mMultiUsers;

    @Before
    public void setUp() {
        Context mAppContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        mMultiUsers = new MultiUsers(mAppContext);
    }

    @Test
    public void getDatabaseName() {
        assertEquals("inspector.db", mMultiUsers.getDatabaseName("inspector"));
    }

    @Test
    public void getPreferenceName() {
        SharedPreferences sharedPreferences = mMultiUsers.getPreferenceName("inspector");
        assertNotNull(sharedPreferences);
    }

    @Test
    public void getCatalog() {
        File dir = mMultiUsers.getCatalog("inspector", "temp");
        assertNotNull(dir);
        String path = dir.getPath();
        assertTrue(path.indexOf("/inspector/temp") > 0);
    }
}