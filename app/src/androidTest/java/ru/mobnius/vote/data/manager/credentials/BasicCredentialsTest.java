package ru.mobnius.vote.data.manager.credentials;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ru.mobnius.vote.SimpleTest;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class BasicCredentialsTest extends SimpleTest {

    private final BasicCredentials credentials;
    public BasicCredentialsTest(){
        credentials = new BasicCredentials("root", "root0");
    }

    @Test
    public void getTokenTest() {
        String validToken = "Token cm9vdDpyb290MA==";
        String returnValue =  credentials.getToken();
        assertEquals(validToken, returnValue);
    }

    @Test
    public void DecodeTest(){
        String validToken = "Token cm9vdDpyb290MA==";
        BasicCredentials credentials = BasicCredentials.decode(validToken);
        assertEquals("root", credentials.login);
        assertEquals("root0", credentials.password);
    }
}
