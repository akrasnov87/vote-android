package ru.mobnius.vote.utils;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import ru.mobnius.vote.SimpleTest;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.packager.MetaPackage;
import ru.mobnius.vote.data.manager.packager.MetaSize;
import ru.mobnius.vote.data.manager.rpc.SingleItemQuery;
import ru.mobnius.vote.data.manager.rpc.RPCItem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
public class PackageUtilUtilsTest extends SimpleTest {

    @Test
    public void lifeCycleTest() throws Exception {
        PackageCreateUtils packageCreateUtils = new PackageCreateUtils(PreferencesManager.ZIP_CONTENT);

        RPCItem item1 = new RPCItem("shell.getServerTime", null);
        Info info = new Info();
        info.name = "Test";
        SingleItemQuery query = new SingleItemQuery(info);

        RPCItem item2 = new RPCItem("shell.getItems", query);

        RPCItem fromItem1 = new RPCItem("users.Query", null);

        byte[] bytes = packageCreateUtils.addTo(item1).addTo(item2).addFrom(fromItem1).generatePackage(UUID.randomUUID().toString());

        PackageReadUtils packageReadUtils = new PackageReadUtils(bytes, PreferencesManager.ZIP_CONTENT);
        MetaSize metaSize = packageReadUtils.getMetaSize();
        assertEquals(metaSize.status, 0);

        MetaPackage metaPackage = packageReadUtils.getMeta();
        assertFalse(metaPackage.id.isEmpty());

        RPCItem[] to = packageReadUtils.getTo();
        assertEquals(to.length, 2);
        assertEquals(to[0].method, "getServerTime");

        RPCItem[] from = packageReadUtils.getFrom();
        assertEquals(from.length, 1);
        assertEquals(from[0].method, "Query");

        packageReadUtils.destroy();
        packageCreateUtils.destroy();
    }

    static class Info{
        String name;
    }
}
