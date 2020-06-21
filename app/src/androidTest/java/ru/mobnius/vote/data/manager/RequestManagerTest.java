package ru.mobnius.vote.data.manager;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

import ru.mobnius.vote.data.manager.configuration.ConfigurationSettingUtil;
import ru.mobnius.vote.data.manager.credentials.BasicCredentials;
import ru.mobnius.vote.data.manager.rpc.FilterItem;
import ru.mobnius.vote.data.manager.rpc.QueryData;
import ru.mobnius.vote.data.manager.rpc.SingleItemQuery;
import ru.mobnius.vote.data.manager.rpc.RPCResult;
import ru.mobnius.vote.data.manager.rpc.SortItem;

import static org.junit.Assert.*;

public class RequestManagerTest {
    private BasicCredentials basicCredentials;

    @Before
    public void setUp() {
        basicCredentials = new BasicCredentials("1801-01", "1801");
    }

    @Test
    public void rpc() throws IOException {
        RPCResult[] results = RequestManager.rpc(MobniusApplication.getBaseUrl(), basicCredentials.getToken(), ConfigurationSettingUtil.ACTION, ConfigurationSettingUtil.METHOD, new SingleItemQuery("MBL"));

        assertNotNull(results);
        assertTrue(results[0].isSuccess());
    }

    @Test
    public void rpcQuery() throws IOException {
        QueryData queryData = new QueryData();
        queryData.filter = new Object[3];
        queryData.filter[0] = new FilterItem("c_key", "ignore_test12");
        queryData.filter[1] = QueryData.FILTER_OR;
        queryData.filter[2] = new FilterItem("c_key", "ignore_test22");

        queryData.sort = new SortItem[1];
        queryData.sort[0] = new SortItem("c_key");

        RPCResult[] results = RequestManager.rpc(MobniusApplication.getBaseUrl(),
                basicCredentials.getToken(),
                "cd_settings",
                "Query",
                queryData);

        assertNotNull(results);
        assertTrue(results[0].isSuccess());
        assertEquals(results[0].result.total, 0);
    }

    @Test
    public void exists() throws IOException {
        HashMap<String, String> hashMap = RequestManager.exists(MobniusApplication.getBaseUrl());
        assertNotNull(hashMap);
        assertNotNull(hashMap.get(RequestManager.KEY_VERSION));
        assertNotNull(hashMap.get(RequestManager.KEY_DB_VERSION));
        assertNotNull(hashMap.get(RequestManager.KEY_IP));
    }
}