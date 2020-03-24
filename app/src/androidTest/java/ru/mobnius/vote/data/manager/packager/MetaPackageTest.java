package ru.mobnius.vote.data.manager.packager;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MetaPackageTest {

    @Test
    public void toJsonString() {

        MetaAttachment metaAttachment = new MetaAttachment(100, "name.jpg", "name");

        MetaAttachment[] metaAttachments = new MetaAttachment[1];
        metaAttachments[0] = metaAttachment;

        MetaPackage meta = new MetaPackage("");
        meta.stringSize = 100;
        meta.binarySize = 0;
        meta.attachments = metaAttachments;
        meta.dataInfo = "full";
        meta.transaction = true;
        meta.version = "1.1";

        String str = meta.toJsonString();
        assertEquals(str, "{\"attachments\":[{\"key\":\"name\",\"name\":\"name.jpg\",\"size\":100}],\"binarySize\":0,\"dataInfo\":\"full\",\"id\":\"\",\"stringSize\":100,\"transaction\":true,\"version\":\"1.1\"}");
    }
}