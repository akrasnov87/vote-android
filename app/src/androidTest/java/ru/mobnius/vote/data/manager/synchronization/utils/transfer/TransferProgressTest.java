package ru.mobnius.vote.data.manager.synchronization.utils.transfer;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.mobnius.vote.SimpleTest;

@RunWith(AndroidJUnit4.class)
public class TransferProgressTest extends SimpleTest {

    @Test
    public void toStringTest() {
        TransferProgress transferProgress = TransferProgress.getInstance(70,
                TransferSpeed.getInstance(1024*1024, 10*1000),
                TransferData.getInstance(512, 1024),
                68*1000);
        String txt = transferProgress.toTransferString();
        Assert.assertEquals(txt.replace(',', '.'), "~00:01:08(102.40 КБ\\сек.)");
    }
}