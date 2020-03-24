package ru.mobnius.vote.data.manager.synchronization.utils;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
public class SocketStatusReaderTest {
    @Test
    public void getInstanceTest(){
        SocketStatusReader reader = SocketStatusReader.getInstance("[DONE]12;hello world;12568");
        assertEquals(reader.getName(), "DONE");
        assertEquals(reader.getParams().length, 3);
        Object[] params = reader.getParams();
        assertEquals(params[0], "12");
        assertEquals(params[1], "hello world");
        assertEquals(params[2], "12568");

        reader = SocketStatusReader.getInstance("test");
        assertNull(reader);
    }
}