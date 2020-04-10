package ru.mobnius.vote.data.manager.packager;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.nio.ByteBuffer;

import ru.mobnius.vote.data.manager.zip.ZipManager;
import ru.mobnius.vote.data.manager.rpc.RPCItem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PackageUtilTest {

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
        String meta = "{\"attachments\":[],\"binarySize\":0,\"dataInfo\":\"full\",\"stringSize\":100,\"transaction\":true,\"version\":\"1.1\"}";
        int size = meta.length();
        MetaSize ms = new MetaSize(size, MetaSize.PROCESSED, "NML");
        byte[] bytes = (ms.toJsonString() + meta).getBytes();
        MetaPackage mp = PackageUtil.readMeta(bytes, false);

        assertTrue(mp.transaction);
        assertEquals(mp.attachments.length, 0);
        assertEquals(mp.binarySize, 0);
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

        BinaryBlock binaryBlock = new BinaryBlock();
        binaryBlock.add("file1", "file1", "Hello World!!!".getBytes());
        binaryBlock.add("file2", "file2", "Hello World 2!!!".getBytes());
        binaryBlock.add("file3", "file3", "Hello World 3!!!".getBytes());

        MetaPackage meta = new MetaPackage("");
        meta.stringSize = str.length();
        meta.binarySize = binaryBlock.toBytes().length;
        meta.attachments = binaryBlock.getAttachments();
        meta.dataInfo = "full";
        meta.transaction = true;
        meta.version = "0.0";
        String metaStr = meta.toJsonString();
        MetaSize ms = new MetaSize(metaStr.length(), MetaSize.PROCESSED, "NML");
        String str2 = ms.toJsonString();
        byte[] bytes = (str2 + metaStr + str).getBytes();
        byte[] allByteArray = new byte[bytes.length + binaryBlock.toBytes().length];

        ByteBuffer buff = ByteBuffer.wrap(allByteArray);
        buff.put(bytes);
        buff.put(binaryBlock.toBytes());

        byte[] combined = buff.array();

        StringBlock block = PackageUtil.readStringBlock(combined, false);
        assertEquals(block.to[0].action, "shell");
        assertEquals(block.to[0].method, "getServerTime");

        BinaryBlock packageBinaryBlock = PackageUtil.readBinaryBlock(combined, false);
        FileBinary[] fileBinaries = packageBinaryBlock.getFiles();
        assertEquals(fileBinaries.length, 3);
        assertEquals(fileBinaries[0].name, "file1");
        assertEquals(new String(fileBinaries[1].bytes), "Hello World 2!!!");
    }

}
