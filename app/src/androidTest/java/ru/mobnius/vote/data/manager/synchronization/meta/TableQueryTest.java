package ru.mobnius.vote.data.manager.synchronization.meta;

import com.google.gson.Gson;

import org.junit.Test;

import ru.mobnius.vote.data.manager.rpc.FilterItem;
import ru.mobnius.vote.data.manager.rpc.RPCItem;
import ru.mobnius.vote.data.manager.synchronization.BaseSynchronization;

import static org.junit.Assert.*;

public class TableQueryTest {

    @Test
    public void toRPCQuery() {
        TableQuery tableQuery = new TableQuery("cd_settings", "");
        FilterItem[] filterItems = new FilterItem[1];
        filterItems[0] = new FilterItem("name", "my");
        RPCItem rpcItem = tableQuery.toRPCQuery(1, filterItems);
        rpcItem.tid = 0;
        String jsonString = toJsonString(rpcItem);
        assertEquals(jsonString, "{\"action\":\"cd_settings\",\"data\":[{\"filter\":[{\"operator\":\"\\u003d\",\"property\":\"name\",\"value\":\"my\"}],\"limit\":1,\"page\":1,\"query\":\"\",\"select\":\"\",\"start\":0}],\"method\":\"Query\",\"tid\":0,\"type\":\"rpc\"}");

        tableQuery = new TableQuery("cd_settings", "users", "name,tid");
        rpcItem = tableQuery.toRPCQuery(1, null);
        rpcItem.tid = 0;
        jsonString = toJsonString(rpcItem);
        assertEquals(jsonString, "{\"action\":\"cd_settings\",\"data\":[{\"alias\":\"users\",\"limit\":1,\"page\":1,\"query\":\"\",\"select\":\"name,tid\",\"start\":0}],\"method\":\"Query\",\"tid\":0,\"type\":\"rpc\"}");
    }

    @Test
    public void toRPCSelect() {
        TableQuery tableQuery = new TableQuery("cd_settings", "");
        RPCItem rpcItem = tableQuery.toRPCSelect(new MyObject());
        rpcItem.tid = 0;
        String jsonString = toJsonString(rpcItem);
        assertEquals(jsonString, "{\"action\":\"cd_settings\",\"data\":[{\"limit\":"+BaseSynchronization.MAX_COUNT_IN_QUERY+",\"params\":[{\"name\":\"test\"}]}],\"method\":\"Select\",\"tid\":0,\"type\":\"rpc\"}");
    }

    private String toJsonString(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    static class MyObject {
        final String name;
        MyObject() {
            this.name = "test";
        }
    }
}