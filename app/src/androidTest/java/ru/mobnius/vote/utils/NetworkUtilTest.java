package ru.mobnius.vote.utils;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NetworkUtilTest {

    private Context appContext;

    @Before
    public void setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void isNetworkAvailable() {
        assertTrue(NetworkUtil.isNetworkAvailable(appContext));
    }
}