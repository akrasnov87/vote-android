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
import ru.mobnius.vote.data.storage.models.DaoSession;
import ru.mobnius.vote.data.storage.models.SubDivisionsDao;
import ru.mobnius.vote.utils.LocationUtil;
import ru.mobnius.vote.utils.PackageReadUtils;
import ru.mobnius.vote.utils.SyncUtil;

import static org.junit.Assert.assertEquals;

public class ManualSynchronizationTest extends ManagerGenerate {
    private ManualSynchronizationTest.MySynchronization synchronization;

    @Before
    public void setUp() {
        synchronization = new ManualSynchronizationTest.MySynchronization(getDaoSession(), getFileManager(), getCredentials());
        synchronization.initEntities();

        // без этого фильтрации не будет работать
        // TODO 09/01/2020 нужно добавить проверку в метод start у синхронизации на передачу идентификатора пользователя, что null не было
        synchronization.getEntity(getDaoSession().getAttachmentsDao().getTablename()).setParam(null);
        synchronization.getEntity(getDaoSession().getFilesDao().getTablename()).setParam(null);
    }

    @After
    public void tearDown() {
        synchronization.getDaoSession().getFilesDao().deleteAll();
        synchronization.getDaoSession().getAttachmentsDao().deleteAll();
    }

    @Test
    public void from() throws IOException {
        byte[] bytes = synchronization.generatePackage(synchronization.dictionaryTid, (Object) null);
        byte[] results = (byte[]) synchronization.sendBytes(synchronization.dictionaryTid, bytes);
        PackageReadUtils utils = new PackageReadUtils(results, synchronization.isZip());
        synchronization.onProcessingPackage(utils, synchronization.dictionaryTid);
        Object[] array = synchronization.getRecords(SubDivisionsDao.TABLENAME, "").toArray();
        Assert.assertTrue(array.length > 0);
        synchronization.destroy();
        utils.destroy();
    }

    @Test
    public void attachments() throws IOException {
        FileManager fileManager = synchronization.getFileManager();
        try {
            fileManager.deleteFolder(FileManager.FILES);
        }catch (FileNotFoundException e){

        }

        // создаем записи о вложениях
        DaoSession session = synchronization.getDaoSession();

        for(int i = 0; i < 2; i++) {
            byte[] bytes = ("file number " + i).getBytes();
            getDataManager().saveAttachment("file" + i + ".tmp", 1, UUID.randomUUID().toString(), "", LocationUtil.getLocation(0, 0), bytes);
        }

        boolean updateTid = SyncUtil.updateTid(synchronization, session.getAttachmentsDao().getTablename(), synchronization.fileTid);
        Assert.assertTrue(updateTid);

        updateTid = SyncUtil.updateTid(synchronization, session.getFilesDao().getTablename(), synchronization.fileTid);
        Assert.assertTrue(updateTid);

        byte[] bytes = synchronization.generatePackage(synchronization.fileTid, (Object) null);
        byte[] results = (byte[]) synchronization.sendBytes(synchronization.fileTid, bytes);

        try {
            fileManager.deleteFolder(FileManager.FILES);
        }catch (FileNotFoundException e){

        }

        synchronization.getDaoSession().getAttachmentsDao().deleteAll();
        synchronization.getDaoSession().getFilesDao().deleteAll();

        PackageReadUtils utils = new PackageReadUtils(results, synchronization.isZip());
        synchronization.onProcessingPackage(utils, synchronization.fileTid);
        byte[] fileBytes = fileManager.readPath(FileManager.FILES, "file0.tmp");
        Assert.assertNotNull(fileBytes);
        assertEquals(new String(fileBytes), "file number 0");

        List records = synchronization.getRecords(session.getAttachmentsDao().getTablename(), "");
        Assert.assertTrue(records.size() >= 2);

        records = synchronization.getRecords(session.getFilesDao().getTablename(), "");
        Assert.assertTrue(records.size() >= 2);

        synchronization.destroy();
        try {
            fileManager.deleteFolder(FileManager.FILES);
        }catch (FileNotFoundException e){

        }
        utils.destroy();
    }

    public class MySynchronization extends ManualSynchronization {
        private BasicCredentials mCredentials;
        public MySynchronization(DaoSession daoSession, FileManager fileManager, BasicCredentials credentials) {
            super(daoSession, fileManager, PreferencesManager.ZIP_CONTENT);
            dictionaryTid = UUID.randomUUID().toString();
            addEntity(new EntityDictionary(SubDivisionsDao.TABLENAME, false, true).setTid(dictionaryTid));
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
