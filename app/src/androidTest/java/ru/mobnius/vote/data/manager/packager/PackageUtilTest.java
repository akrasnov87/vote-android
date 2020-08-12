package ru.mobnius.vote.data.manager.packager;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.nio.ByteBuffer;

import ru.mobnius.vote.SimpleTest;
import ru.mobnius.vote.data.manager.zip.ZipManager;
import ru.mobnius.vote.data.manager.rpc.RPCItem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PackageUtilTest extends SimpleTest {

    @Test
    public void getString() throws IOException {
        String inputString = "HelloWorld!!!";
        byte[] output = ZipManager.compress(inputString).getCompress();
        String result = PackageUtil.getString(output, true);
        assertEquals(inputString, result);
        result = PackageUtil.getString(inputString.getBytes(), true);
        assertEquals(inputString, result);
    }

    @Test
    public void readMetaTest() throws Exception {
        String meta = "{\"dataInfo\":\"full\",\"stringSize\":100,\"transaction\":true,\"version\":\"1.1\"}";
        int size = meta.length();
        MetaSize ms = new MetaSize(size, MetaSize.PROCESSED, "NML");
        byte[] bytes = (ms.toJsonString() + meta).getBytes();
        MetaPackage mp = PackageUtil.readMeta(bytes, false);

        assertTrue(mp.transaction);
        assertEquals(mp.stringSize, 100);
        assertEquals(mp.version, "1.1");
        assertEquals(mp.dataInfo, "full");
    }

    @Test
    public void readBlockTest() throws Exception {
        RPCItem[] to = new RPCItem[2];
        RPCItem item1 = new RPCItem("shell.getServerTime", null);
        to[0] = item1;

        String str = new StringBlock(to).toJsonString();

        MetaPackage meta = new MetaPackage("");
        meta.stringSize = str.length();
        meta.dataInfo = "full";
        meta.transaction = true;
        meta.version = "0.0";
        String metaStr = meta.toJsonString();
        MetaSize ms = new MetaSize(metaStr.length(), MetaSize.PROCESSED, "NML");
        String str2 = ms.toJsonString();
        byte[] bytes = (str2 + metaStr + str).getBytes();
        byte[] allByteArray = new byte[bytes.length];

        ByteBuffer buff = ByteBuffer.wrap(allByteArray);
        buff.put(bytes);

        byte[] combined = buff.array();

        StringBlock block = PackageUtil.readStringBlock(combined, false);
        assertEquals(block.to[0].action, "shell");
        assertEquals(block.to[0].method, "getServerTime");
    }
}
