package ru.mobnius.vote.data.manager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.UUID;

import ru.mobnius.vote.ManagerGenerate;
import ru.mobnius.vote.R;
import ru.mobnius.vote.utils.LocationUtil;
import ru.mobnius.vote.utils.StreamUtil;

import static org.junit.Assert.*;

public class PhotoManagerTest extends ManagerGenerate {
    private byte[] mBytes;

    private String mResultId;
    private PhotoManager mPhotoManager;

    @Before
    public void setUp() throws Exception {

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

        mPhotoManager = new PhotoManager();
    }

    @After
    public void tearDown() {
        getDaoSession().getAttachmentsDao().deleteAll();
        getDaoSession().getFilesDao().deleteAll();
        getDaoSession().getAttachmentTypesDao().deleteAll();
        mPhotoManager.destroy(getFileManager());
        getFileManager().clearUserFolder();
    }

    @Test
    public void addTempPicture() throws Exception {
        generateData();

        assertEquals(mPhotoManager.getImages().length, 3);

        Image three = Image.getInstance("second.png", 1, mResultId, "", mBytes, LocationUtil.getLocation(0,0));
        try {
            mPhotoManager.addTempPicture(three);
            fail();
        }catch (Exception e) {
            assertEquals(mPhotoManager.getImages().length, 3);
        }
    }

    @Test
    public void updatePicture() throws Exception {
        generateData();

        Image image = mPhotoManager.findImage("first.png");
        mPhotoManager.updatePicture(getDataManager(), image, 2, "my");
        image = mPhotoManager.findImage("first.png");
        assertEquals(image.getNotice(), "my");
        assertEquals(image.getType(), 2);

        image = mPhotoManager.findImage("three.png");
        mPhotoManager.updatePicture(getDataManager(), image, 2, "my");
        image = mPhotoManager.findImage("three.png");
        assertEquals(image.getNotice(), "my");
        assertEquals(image.getType(), 2);
    }

    @Test
    public void deletePicture() throws Exception {
        generateData();

        Image image = mPhotoManager.findImage("first.png");
        mPhotoManager.deletePicture(getDataManager(), getFileManager(), image);
        image = mPhotoManager.findImage("first.png");
        assertNull(image);
        assertEquals(mPhotoManager.getImages().length, 2);

        image = mPhotoManager.findImage("three.png");
        mPhotoManager.deletePicture(getDataManager(), getFileManager(), image);
        image = mPhotoManager.findImage("three.png");
        assertNull(image);
        assertEquals(mPhotoManager.getImages().length, 1);
    }

    @Test
    public void savePictures() throws Exception {
        generateData();

        mPhotoManager.savePictures(getDataManager(), getFileManager(), mResultId);
        Image image = mPhotoManager.findImage("first.png");
        assertNotNull(image);
        assertFalse(mPhotoManager.isTempImage(image));
        assertNotNull(getDataManager().getAttachment(image.getId()));

        image = mPhotoManager.findImage("second.png");
        assertNotNull(image);
        assertFalse(mPhotoManager.isTempImage(image));
        assertNotNull(getDataManager().getAttachment(image.getId()));

        assertEquals(mPhotoManager.getImages().length, 3);
    }

    @Test
    public void findImage() throws Exception {
        generateData();

        Image image = mPhotoManager.findImage("second.png");
        assertNotNull(image);

        assertEquals(image.getNotice(), "my");
        assertEquals(image.getType(), 1);
        assertEquals(image.getName(), "second.png");
    }

    @Test
    public void isTempImage() throws Exception {
        generateData();

        Image image = mPhotoManager.findImage("first.png");

        assertTrue(mPhotoManager.isTempImage(image));

        image = mPhotoManager.findImage("three.png");
        assertFalse(mPhotoManager.isTempImage(image));
    }

    private void generateData() throws Exception {
        Image first = Image.getInstance("first.png", 1, mResultId, "", mBytes, LocationUtil.getLocation(0,0));
        getFileManager().writeBytes(FileManager.TEMP_PICTURES, first.getName(), mBytes);
        Image second = Image.getInstance("second.png", 1, mResultId, "my", mBytes, LocationUtil.getLocation(0,0));
        getFileManager().writeBytes(FileManager.TEMP_PICTURES, second.getName(), mBytes);
        Image three = Image.getInstance("three.png", 1, mResultId, "", mBytes, LocationUtil.getLocation(0,0));
        getFileManager().writeBytes(FileManager.TEMP_PICTURES, three.getName(), mBytes);

        getDataManager().saveAttachment(three.getName(), three.getType(), three.getResultId(), three.getNotice(), three.getLocation(), three.getBytes());
        Image[] images = new Image[1];
        images[0] = three;

        mPhotoManager.addPictures(images);

        mPhotoManager.addTempPicture(first);
        mPhotoManager.addTempPicture(second);
    }
}