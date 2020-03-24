package ru.mobnius.vote.ui.model;

import android.location.Location;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.UUID;

import ru.mobnius.vote.ManagerGenerate;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.storage.models.AttachmentTypes;
import ru.mobnius.vote.data.storage.models.Attachments;
import ru.mobnius.vote.utils.LocationUtil;
import ru.mobnius.vote.utils.StreamUtil;

import static org.junit.Assert.*;

public class ImageTest extends ManagerGenerate {

    private byte[] mBytes;
    private String mResultId;

    @Before
    public void setUp() throws IOException {
        InputStream inStream = getContext().getResources().openRawResource(R.raw.preview);
        mBytes = StreamUtil.readBytes(inStream);

        AttachmentTypes attachmentType = new AttachmentTypes();
        attachmentType.id = (long)1;
        attachmentType.c_name = "Тип";
        getDaoSession().getAttachmentTypesDao().insert(attachmentType);

        attachmentType = new AttachmentTypes();
        attachmentType.id = (long)2;
        attachmentType.c_name = "Тип 2";
        getDaoSession().getAttachmentTypesDao().insert(attachmentType);

        mResultId = UUID.randomUUID().toString();
    }

    @Test
    public void writeLocation() {
        Image image = new Image();
        image.setLocation(LocationUtil.getLocation(2,2));
        assertEquals(image.getLocation().getLatitude(), 2, 0);
        assertEquals(image.getLocation().getLongitude(), 2, 0);
        assertNotNull(image.getDate());
    }

    @Test
    public void getInstance() throws IOException, ParseException {
        Attachments attachment = getDataManager().saveAttachment("preview.png", 1, mResultId, "", LocationUtil.getLocation(0,0), mBytes);
        Image image = Image.getInstance(getDataManager(), attachment);
        assertNotNull(image);
        assertEquals(image.getName(), attachment.c_name);
        assertEquals(image.getResultId(), attachment.fn_result);
        assertEquals(image.getType(), attachment.fn_type);
        assertEquals(image.getNotice(), attachment.c_notice);
        assertEquals(image.getLocation().getLongitude(), 0, 0);
        assertEquals(image.getLocation().getLatitude(), 0, 0);
        //assertNotNull(image.getBytes());
        assertNotNull(image.getThumbs());

        image = Image.getInstance("preview.png", 1, mResultId, "", mBytes, LocationUtil.getLocation(0,0));
        assertNotNull(image.getThumbs());
    }

    @Test
    public void update() throws IOException, ParseException {
        Attachments attachment = getDataManager().saveAttachment("preview.png", 1, mResultId, "", LocationUtil.getLocation(0,0), mBytes);
        Image image = Image.getInstance(getDataManager(), attachment);
        image.update(getDataManager(), 2, "Hello");
        image = Image.getInstance(getDataManager(), attachment);
        assertNotNull(image);
        assertEquals(image.getType(), 2);
        assertEquals(image.getNotice(), "Hello");
    }

    @Test
    public void remove() throws IOException, ParseException {
        Attachments attachment = getDataManager().saveAttachment("preview.png", 1, mResultId, "", LocationUtil.getLocation(0,0), mBytes);
        Image image = Image.getInstance(getDataManager(), attachment);
        image.remove(getDataManager());
        attachment = getDataManager().getAttachment(attachment.id);
        assertNull(attachment);
    }

    @After
    public void tearDown() {
        getDaoSession().getAttachmentsDao().deleteAll();
        getDaoSession().getFilesDao().deleteAll();
        getDaoSession().getAttachmentTypesDao().deleteAll();
    }
}