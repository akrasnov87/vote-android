package ru.mobnius.vote.data.manager.synchronization.utils.transfer;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class TransferSpeedTest {

    @Test
    public void toStringTest() {
        String txt = TransferSpeed.getInstance(1024*1024, 2 * 1000).toTransferBaseString();
        assertEquals(txt.replace(',', '.'), "512.00 КБ\\сек.");
    }
}