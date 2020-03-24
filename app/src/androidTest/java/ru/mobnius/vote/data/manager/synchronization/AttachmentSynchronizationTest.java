package ru.mobnius.vote.data.manager.synchronization;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import ru.mobnius.vote.ManagerGenerate;
import ru.mobnius.vote.data.manager.FileManager;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.credentials.BasicCredentials;
import ru.mobnius.vote.data.storage.models.Attachments;
import ru.mobnius.vote.data.storage.models.AttachmentsDao;
import ru.mobnius.vote.data.storage.models.DaoSession;
import ru.mobnius.vote.data.storage.models.Files;
import ru.mobnius.vote.data.storage.models.FilesDao;
import ru.mobnius.vote.data.storage.models.SubDivisionsDao;
import ru.mobnius.vote.utils.LocationUtil;
import ru.mobnius.vote.utils.PackageReadUtils;
import ru.mobnius.vote.utils.SyncUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AttachmentSynchronizationTest extends ManagerGenerate {
    private AttachmentSynchronizationTest.MySynchronization synchronization;

    @Before
    public void setUp() {
        synchronization = new AttachmentSynchronizationTest.MySynchronization(getDaoSession(), getFileManager(), getCredentials());
    }

    @After
    public void tearDown() {
        synchronization.getDaoSession().getFilesDao().deleteAll();
        synchronization.getDaoSession().getAttachmentsDao().deleteAll();
        getFileManager().clearUserFolder();
    }

    @Test
    public void query() throws IOException {
        byte[] bytes = synchronization.generatePackage(synchronization.fileTid, (Object) null);
        byte[] results = (byte[]) synchronization.sendBytes(synchronization.fileTid, bytes);
        PackageReadUtils utils = new PackageReadUtils(results, synchronization.isZip());
        synchronization.onProcessingPackage(utils, synchronization.fileTid);

        Object[] array = synchronization.getRecords(AttachmentsDao.TABLENAME, "").toArray();
        for(Object o : array) {
            Attachments attachment = (Attachments)o;
            assertTrue(getFileManager().exists(FileManager.ATTACHMENTS, attachment.c_name));
        }
    }

    public class MySynchronization extends ManualSynchronization {
        private BasicCredentials mCredentials;
        public MySynchronization(DaoSession daoSession, FileManager fileManager, BasicCredentials credentials) {
            super(daoSession, fileManager, PreferencesManager.ZIP_CONTENT);
            fileTid = UUID.randomUUID().toString();
            addEntity(new EntityAttachment(FilesDao.TABLENAME, true, true).setParam(null).setUseCFunction().setTid(fileTid));
            addEntity(new EntityAttachment(AttachmentsDao.TABLENAME, true, true).setParam(null).setUseCFunction().setTid(fileTid));
            mCredentials = credentials;
        }

        @Override
        protected Object sendBytes(String tid, byte[] bytes) {
            super.sendBytes(tid, bytes);

            try {
                MultipartUtility multipartUtility = new MultipartUtility(ManagerGenerate.getBaseUrl() + "/synchronization/" + PreferencesManager.SYNC_PROTOCOL, mCredentials);
                multipartUtility.addFilePart("synchronization", bytes);
                return multipartUtility.finish();
            }catch (Exception exc){
                return  null;
            }
        }

        @Override
        public long getUserID() {
            return -1;
        }
    }
}
