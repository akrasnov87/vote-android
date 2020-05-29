package ru.mobnius.vote.data.manager.packager;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ru.mobnius.vote.data.manager.rpc.SingleItemQuery;
import ru.mobnius.vote.data.manager.rpc.RPCItem;
import ru.mobnius.vote.data.manager.synchronization.BaseSynchronization;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class StringBlockTest {

    @Test
    public void toJsonString(){
        RPCItem[] to = new RPCItem[2];
        to[0] = new RPCItem("shell.getServerTime", null);
        to[1] = new RPCItem("setting.getMobileSettings", null);

        RPCItem[] from = new RPCItem[1];
        SingleItemQuery singleItemQuery = new SingleItemQuery((Object) null);

        from[0] = new RPCItem("shell.getServerTime", singleItemQuery);

        StringBlock block = new StringBlock(to);
        String str = block.toJsonString();
        assertEquals(str, "{\"from\":[],\"to\":[{\"action\":\"shell\",\"data\":[[null]],\"method\":\"getServerTime\",\"tid\":"+to[0].tid +",\"type\":\"rpc\"},{\"action\":\"setting\",\"data\":[[null]],\"method\":\"getMobileSettings\",\"tid\":"+to[1].tid+",\"type\":\"rpc\"}]}");

        block = new StringBlock(to, from);
        str = block.toJsonString();
        assertEquals(str, "{\"from\":[{\"action\":\"shell\",\"data\":[[{\"filter\":null,\"limit\":"+ BaseSynchronization.MAX_COUNT_IN_QUERY +",\"params\":[null]}]],\"method\":\"getServerTime\",\"tid\":"+from[0].tid+",\"type\":\"rpc\"}],\"to\":[{\"action\":\"shell\",\"data\":[[null]],\"method\":\"getServerTime\",\"tid\":"+to[0].tid+",\"type\":\"rpc\"},{\"action\":\"setting\",\"data\":[[null]],\"method\":\"getMobileSettings\",\"tid\":"+to[1].tid+",\"type\":\"rpc\"}]}");
    }
}
