package ru.mobnius.vote.data;

import org.junit.Test;

import static org.junit.Assert.*;

public class ICallbackTest {

    @Test
    public void callbackTest() {
        MyTestManager manager = new MyTestManager();
        manager.onCall(new ICallback() {
            @Override
            public void onResult(Meta meta) {
                assertTrue(meta.isSuccess());
            }
        });
    }

    private static class MyTestManager {
        void onCall(final ICallback callback) {
            Meta meta = new Meta(Meta.OK, "");
            callback.onResult(meta);
        }
    }
}