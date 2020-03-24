package ru.mobnius.vote.utils;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HardwareUtilTest {

    private Context appContext;

    @Before
    public void setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void getIMEI() {
        String id = HardwareUtil.getIMEI(appContext);
        assertNotNull(id);
    }

    @Test
    public void getBatteryPercentage() {
        assertNotNull(HardwareUtil.getBatteryPercentage(appContext));
    }
}