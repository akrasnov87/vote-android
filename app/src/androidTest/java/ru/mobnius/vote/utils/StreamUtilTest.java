package ru.mobnius.vote.utils;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import ru.mobnius.vote.R;
import ru.mobnius.vote.SimpleTest;

import static org.junit.Assert.*;

public class StreamUtilTest extends SimpleTest {
    private Context appContext;

    @Before
    public void setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void readBytes() throws IOException {
        InputStream inStream = appContext.getResources().openRawResource(R.raw.preview);
        byte[] array = StreamUtil.readBytes(inStream);
        assertTrue(array.length > 0);
    }
}