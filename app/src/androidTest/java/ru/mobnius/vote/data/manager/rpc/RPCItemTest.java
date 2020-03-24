package ru.mobnius.vote.data.manager.rpc;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.gson.annotations.Expose;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RPCItemTest {
    private QueryData queryData;
    @Before
    public void setUp() {
        queryData = new QueryData();
        queryData.filter = new Object[5];
        queryData.filter[0] = new FilterItem("name", "test");
        queryData.filter[1] = QueryData.FILTER_OR;
        queryData.filter[2] = new FilterItem("name", "test2");
        queryData.filter[3] = QueryData.FILTER_AND;
        queryData.filter[4] = new FilterItem("name", "test3");

        queryData.sort = new SortItem[1];
        queryData.sort[0] = new SortItem("d_date", SortItem.ASC);
    }

    @Test
    public void toJsonString() {
        AndroidInfo info = new AndroidInfo( "Android");

        RPCItem query = new RPCItem();
        query.action = "setting";
        query.method = "getMobileSettings";
        query.data = new SingleItemQuery[1];
        query.data[0] = new SingleItemQuery(info);

        String str = query.toJsonString();
        assertEquals(str, "{\"action\":\"setting\",\"data\":[{\"params\":[{\"name\":\""+info.name+"\"}]}],\"method\":\"getMobileSettings\",\"tid\":"+query.tid+",\"type\":\"rpc\"}");

        query.data = null;
        str = query.toJsonString();
        assertEquals(str, "{\"action\":\"setting\",\"data\":null,\"method\":\"getMobileSettings\",\"tid\":"+query.tid+",\"type\":\"rpc\"}");

        // TODO: 31/12/2019 перенести в отдельный тест
        query = new RPCItem();
        query.action = "setting";
        query.method = "getMobileSettings";
        query.data = new Object[1];
        query.data[0] = queryData;

        str = query.toJsonString();
        assertEquals(str, "{\"action\":\"setting\",\"data\":[{\"alias\":null,\"filter\":[{\"operator\":\"\\u003d\",\"property\":\"name\",\"value\":\"test\"},\"OR\",{\"operator\":\"\\u003d\",\"property\":\"name\",\"value\":\"test2\"},\"AND\",{\"operator\":\"\\u003d\",\"property\":\"name\",\"value\":\"test3\"}],\"limit\":25,\"page\":1,\"query\":\"\",\"select\":null,\"sort\":[{\"direction\":\"ASC\",\"property\":\"d_date\"}],\"start\":0}],\"method\":\"getMobileSettings\",\"tid\":"+query.tid+",\"type\":\"rpc\"}");
    }

    @Test
    public void queryDataToJsonString() {
        RPCItem rpcItem = new RPCItem("cd_settings", "Query", queryData);
        rpcItem.tid = 0;
        String jsonString = rpcItem.toJsonString();
        assertEquals(jsonString, "{\"action\":\"cd_settings\",\"data\":[{\"alias\":null,\"filter\":[{\"operator\":\"\\u003d\",\"property\":\"name\",\"value\":\"test\"},\"OR\",{\"operator\":\"\\u003d\",\"property\":\"name\",\"value\":\"test2\"},\"AND\",{\"operator\":\"\\u003d\",\"property\":\"name\",\"value\":\"test3\"}],\"limit\":25,\"page\":1,\"query\":\"\",\"select\":null,\"sort\":[{\"direction\":\"ASC\",\"property\":\"d_date\"}],\"start\":0}],\"method\":\"Query\",\"tid\":0,\"type\":\"rpc\"}");
    }

    @Test
    public void addItems() {
        Object[] items = new Object[1];
        items[0] = new AndroidInfo("test");
        RPCItem item = RPCItem.addItems("cd_settings", items);
        item.tid = 0;
        String jsonString = item.toJsonString();
        assertEquals(jsonString, "{\"action\":\"cd_settings\",\"data\":[[{\"name\":\"test\"}]],\"method\":\"Add\",\"tid\":0,\"type\":\"rpc\"}");
    }

    @Test
    public void updateItems() {
        Object[] items = new Object[1];
        items[0] = new AndroidInfo("test");
        RPCItem item = RPCItem.updateItems("cd_settings", items);
        item.tid = 0;
        String jsonString = item.toJsonString();
        assertEquals(jsonString, "{\"action\":\"cd_settings\",\"data\":[[{\"name\":\"test\"}]],\"method\":\"Update\",\"tid\":0,\"type\":\"rpc\"}");
    }

    @Test
    public void deleteItems() {
        Object[] items = new Object[1];
        items[0] = new AndroidInfo("test");
        RPCItem item = RPCItem.deleteItems("cd_settings", items);
        item.tid = 0;
        String jsonString = item.toJsonString();
        assertEquals(jsonString, "{\"action\":\"cd_settings\",\"data\":[[{\"name\":\"test\"}]],\"method\":\"Delete\",\"tid\":0,\"type\":\"rpc\"}");
    }

    @Test
    public void addItem() {
        RPCItem item = RPCItem.addItem("cd_settings", new AndroidInfo("test"));
        item.tid = 0;
        String jsonString = item.toJsonString();
        assertEquals(jsonString,"{\"action\":\"cd_settings\",\"data\":[[{\"name\":\"test\"}]],\"method\":\"AddOrUpdate\",\"tid\":0,\"type\":\"rpc\"}");
    }

    @Test
    public void deleteItem() {
        RPCItem item = RPCItem.deleteItem("cd_settings", new AndroidInfo("test"));
        item.tid = 0;
        String jsonString = item.toJsonString();
        assertEquals(jsonString, "{\"action\":\"cd_settings\",\"data\":[[{\"name\":\"test\"}]],\"method\":\"Delete\",\"tid\":0,\"type\":\"rpc\"}");
    }

    class AndroidInfo {
        public AndroidInfo(String name) {
            this.name = name;
        }

        @Expose
        String name;
    }
}
