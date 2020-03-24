package ru.mobnius.vote.data.manager;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileNotFoundException;
import java.io.IOException;

import ru.mobnius.vote.data.GlobalSettings;
import ru.mobnius.vote.data.manager.credentials.BasicCredentials;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class FileManagerTest {
    private BasicCredentials credentials;
    private FileManager fileManager;

    private Context appContext;

    @Before
    public void setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        credentials = new BasicCredentials("inspector", "");
        fileManager = FileManager.createInstance(credentials, appContext);
    }

    @Test
    public void write() throws IOException {
        fileManager.writeBytes("pictures", "pic.txt", "picture 1".getBytes());

        assertTrue(fileManager.exists("pictures", "pic.txt"));

        fileManager.writeBytes("pictures", "pic2.txt", "picture 2".getBytes());

        byte[] bytes = fileManager.readPath("pictures", "pic.txt");
        Assert.assertEquals("picture 1", new String(bytes));

        fileManager.deleteFile("pictures", "pic2.txt");
        assertNull(fileManager.readPath("pictures", "pic2.txt"));

        fileManager.deleteFolder("pictures");
        assertNull(fileManager.readPath("pictures", "pic.txt"));
    }

    @After
    public void tearDown() {
        fileManager.clearUserFolder();
    }
}
