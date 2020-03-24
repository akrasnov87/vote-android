package ru.mobnius.vote.data.manager.camera;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import ru.mobnius.vote.R;
import ru.mobnius.vote.data.GlobalSettings;
import ru.mobnius.vote.data.manager.FileManager;
import ru.mobnius.vote.data.manager.credentials.BasicCredentials;
import ru.mobnius.vote.utils.StreamUtil;

import static org.junit.Assert.*;

public class CameraUtilTest  {
    private Context appContext;

    @Before
    public void setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void compress() {
        InputStream inStream = appContext.getResources().openRawResource(R.raw.preview);
        byte[] bytes = CameraUtil.compress(inStream, CameraUtil.JPEG_IMAGE_FORMAT, 60);
        assertNotNull(bytes);
    }

    @Test
    public void saveDataFromCamera() throws IOException {
        BasicCredentials credentials = new BasicCredentials("inspector", "");
        FileManager fileManager = FileManager.createInstance(credentials, appContext);
        InputStream inStream = appContext.getResources().openRawResource(R.raw.preview);
        CameraUtil.saveDataFromCamera(fileManager, "test.jpg", StreamUtil.readBytes(inStream));

        assertNotNull(fileManager.readPath(FileManager.TEMP_PICTURES, "test.jpg"));
        assertNotNull(fileManager.readPath(FileManager.CACHES, "test.jpg"));

        fileManager.clearUserFolder();
    }
}