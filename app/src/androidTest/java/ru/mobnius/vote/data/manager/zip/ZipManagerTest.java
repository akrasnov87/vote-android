package ru.mobnius.vote.data.manager.zip;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.zip.DataFormatException;


import ru.mobnius.vote.data.manager.zip.ZipManager;

@RunWith(AndroidJUnit4.class)
public class ZipManagerTest {
    @Test
    public void compressText() throws IOException, DataFormatException {
        String compressText = "Hello World";
        byte[] data = ZipManager.compress(compressText).getCompress();
        String txt = new String(ZipManager.decompress(data));
        Assert.assertEquals(txt, compressText);
    }
}