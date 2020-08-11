package ru.mobnius.vote.data.manager.synchronization.utils.transfer;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.mobnius.vote.SimpleTest;

@RunWith(AndroidJUnit4.class)
public class TransferDataTest extends SimpleTest {

    @Test
    public void toStringTest() {
        TransferData data = TransferData.getInstance(512, 1024);
        String txt = data.toTransferString();
        Assert.assertEquals(txt.replace(',', '.'), "512 байт/1.00 КБ");
    }
}