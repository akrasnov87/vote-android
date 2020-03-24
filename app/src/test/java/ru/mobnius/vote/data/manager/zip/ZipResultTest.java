package ru.mobnius.vote.data.manager.zip;

import org.junit.Test;

import static org.junit.Assert.*;

public class ZipResultTest {

    @Test
    public void getResult() {
        byte[] bytes = "Hello".getBytes();
        ZipResult zipResult = new ZipResult(bytes);
        ZipResult newZipResult = zipResult.getResult("H".getBytes());
        assertEquals(new String(newZipResult.getCompress()), "H");
        assertTrue(newZipResult.getK() > 0);
    }
}