package ru.mobnius.vote.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import ru.mobnius.vote.R;
import ru.mobnius.vote.SimpleTest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class BitmapUtilTest extends SimpleTest {
    private Context appContext;

    @Before
    public void setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void cacheBitmap() throws IOException {
        Bitmap icon = BitmapFactory.decodeResource(appContext.getResources(),
                R.raw.preview);

        byte[] bytes = BitmapUtil.cacheBitmap(icon, BitmapUtil.IMAGE_QUALITY, BitmapUtil.QUALITY_120p);
        Assert.assertNotNull(bytes);

        InputStream inStream = appContext.getResources().openRawResource(R.raw.preview);
        byte[] array = StreamUtil.readBytes(inStream);
        assertTrue(array.length > 0);

        byte[] result = BitmapUtil.cacheBitmap(array, BitmapUtil.IMAGE_QUALITY, BitmapUtil.QUALITY_120p);
        assertNotNull(result);
    }
}
