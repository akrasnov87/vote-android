package ru.mobnius.vote.data.manager.synchronization.utils;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.mobnius.vote.SimpleTest;
import ru.mobnius.vote.data.manager.synchronization.utils.transfer.TransferResult;

@RunWith(AndroidJUnit4.class)
public class TransferZipResultTest extends SimpleTest {
    @Test
    public void readResult() throws JSONException {

        JSONObject object = new JSONObject();
        object.put("tid", "0");
        object.put("code", 200);
        object.put("result", new byte[0]);

        JSONObject meta = new JSONObject();
        meta.put("processed", false);
        object.put("meta", meta);

        JSONObject data = new JSONObject();
        data.put("success", true);
        data.put("msg", "");

        object.put("meta", meta);
        object.put("data", data);

        TransferResult result = TransferResult.readResult(object);

        Assert.assertEquals(result.code, 200);
        Assert.assertFalse(result.meta.processed);
        Assert.assertTrue(result.data.success);
    }
}