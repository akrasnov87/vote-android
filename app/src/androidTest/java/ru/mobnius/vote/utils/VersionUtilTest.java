package ru.mobnius.vote.utils;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import ru.mobnius.vote.data.manager.Version;

import static org.junit.Assert.*;

public class VersionUtilTest {
    private Context appContext;

    @Before
    public void setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void getVersionName() {
        String versionName = VersionUtil.getVersionName(appContext);
        assertNotNull(versionName);
        Version version = new Version();
        assertEquals(version.getVersionParts(versionName).length, 4);
    }

    @Test
    public void getShortVersionName() {
        String versionName = VersionUtil.getShortVersionName(appContext);
        assertNotNull(versionName);
        Version version = new Version();
        assertNull(version.getVersionParts(versionName));
    }
}