package ru.mobnius.vote.data.manager.rpc;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.mobnius.vote.SimpleTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class RPCZipResultTest extends SimpleTest {
    @SuppressWarnings("FieldCanBeLocal")
    private final String NOT_AUTH_RESULT = "{\n" +
            "  \"code\": 401,\n" +
            "  \"host\": \"localhost\",\n " +
            "  \"meta\": {\n" +
            "    \"success\": false\n" +
            "  }\n" +
            "}";

    @SuppressWarnings("FieldCanBeLocal")
    private final String NOT_AUTH_RESULT2 = "{\n" +
            "  \"code\": \"qwe\",\n" +
            "  \"host\": \"localhost\",\n " +
            "  \"meta\": {\n" +
            "    \"success\": false\n" +
            "  }\n" +
            "}";

    @SuppressWarnings("FieldCanBeLocal")
    private final String BAD_REQUEST_RESULT = "[\n" +
            "  {\n" +
            "    \"code\": 400,\n" +
            "    \"host\": \"localhost\",\n " +
            "    \"action\": \"setting\",\n" +
            "    \"method\": \"getMobileSettings\",\n" +
            "    \"meta\": {\n" +
            "      \"success\": false,\n" +
            "      \"msg\": \"Bad request Error: Пользователь не имеет прав на выполнение операции.. Body: {\\\"action\\\":\\\"setting\\\",\\\"data\\\":[{\\\"params\\\":[{\\\"name\\\":\\\"Android\\\"}]}],\\\"method\\\":\\\"getMobileSettings\\\",\\\"tid\\\":1438883042,\\\"type\\\":\\\"rpc\\\"}\"\n" +
            "    },\n" +
            "    \"result\": {\n" +
            "      \"records\": [],\n" +
            "      \"total\": 0\n" +
            "    },\n" +
            "    \"tid\": 1438883042,\n" +
            "    \"type\": \"rpc\"\n" +
            "  }\n" +
            "]";

    @SuppressWarnings("FieldCanBeLocal")
    private final String RESULT = "[\n" +
            "  {\n" +
            "    \"meta\": {\n" +
            "      \"success\": true,\n" +
            "      \"msg\": \"ok\"\n" +
            "    },\n" +
            "    \"code\": 200,\n" +
            "    \"result\": {\n" +
            "      \"records\": [\n" +
            "        \"2019-06-05T08:19:54.800Z\"\n" +
            "      ]\n" +
            "    },\n" +
            "    \"tid\": 1438804277,\n" +
            "    \"type\": \"rpc\",\n" +
            "    \"method\": \"getServerTime\",\n" +
            "    \"action\": \"shell\",\n" +
            "    \"host\": \"localhost\",\n " +
            "    \"rpcTime\": 1\n" +
            "  }\n" +
            "]";

    @Test
    public void processingJSONObject() {
        RPCResult[] results = RPCResult.createInstance(NOT_AUTH_RESULT2);
        assert results != null;
        assertEquals(results[0].code, RPCResult.APPLICATION_ERROR);
    }

    @Test
    public void createInstanceTest() throws JSONException {
        RPCResult[] results = RPCResult.createInstance(NOT_AUTH_RESULT);
        assert results != null;
        assertEquals(results[0].code, RPCResult.NOT_AUTHORIZATION_RESULT);
        assertFalse(results[0].meta.success);

        results = RPCResult.createInstance(BAD_REQUEST_RESULT);
        assert results != null;
        assertFalse(results[0].isSuccess());
        assertEquals(results[0].code, RPCResult.BAD_REQUEST);
        assertFalse(results[0].meta.success);
        assertFalse(results[0].meta.msg.isEmpty());
        assertEquals(results[0].method, "getMobileSettings");
        assertEquals(results[0].action, "setting");
        assertEquals(1438883042, results[0].tid);
        assertEquals(results[0].type, "rpc");
        assertEquals(0, results[0].result.total);
        assertEquals(0, results[0].result.records.length);

        results = RPCResult.createInstance(RESULT);
        assert results != null;
        assertTrue(results[0].isSuccess());
        assertEquals("2019-06-05T08:19:54.800Z", results[0].result.records[0].getString("value"));
        assertEquals(results[0].host, "localhost");
    }
}
