package ru.mobnius.vote.data.manager;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import ru.mobnius.vote.ManagerGenerate;

import static org.junit.Assert.*;

public class NotificationManagerTest extends ManagerGenerate {

    private NotificationManager mNotificationManager;

    @Before
    public void setUp() throws Exception {
        mNotificationManager = new NotificationManager(getBasicUser().getCredentials().getToken());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getNewMessageCountTest() throws IOException, JSONException {
        int count = mNotificationManager.getNewMessageCount();
        assertTrue(count > 0);
    }

    @Test
    public void changeStatusTest() throws IOException {
        assertTrue(mNotificationManager.changeStatus(new String[] {"3"}));
    }

    @Test
    public void sendedTest() throws IOException {
        assertTrue(mNotificationManager.sended());
    }
}