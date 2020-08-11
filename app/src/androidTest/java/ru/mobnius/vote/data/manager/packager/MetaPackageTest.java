package ru.mobnius.vote.data.manager.packager;

import org.junit.Test;

import ru.mobnius.vote.SimpleTest;

import static org.junit.Assert.*;

public class MetaPackageTest extends SimpleTest {

    @Test
    public void toJsonString() {

        MetaPackage meta = new MetaPackage("");
        meta.stringSize = 100;
        meta.dataInfo = "full";
        meta.transaction = true;
        meta.version = "1.1";

        String str = meta.toJsonString();
        assertEquals(str, "{\"attachments\":null,\"binarySize\":0,\"dataInfo\":\"full\",\"id\":\"\",\"stringSize\":100,\"transaction\":true,\"version\":\"1.1\"}");
    }
}