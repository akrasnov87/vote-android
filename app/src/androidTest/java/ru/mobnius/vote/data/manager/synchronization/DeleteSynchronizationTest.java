package ru.mobnius.vote.data.manager.synchronization;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import ru.mobnius.vote.ManagerGenerate;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.DbOperationType;
import ru.mobnius.vote.data.manager.FileManager;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.credentials.BasicCredentials;
import ru.mobnius.vote.data.manager.rpc.FilterItem;
import ru.mobnius.vote.data.manager.synchronization.utils.FullServerSidePackage;
import ru.mobnius.vote.data.storage.models.DaoSession;
import ru.mobnius.vote.data.storage.models.Files;
import ru.mobnius.vote.data.storage.models.FilesDao;
import ru.mobnius.vote.utils.DateUtil;
import ru.mobnius.vote.utils.PackageReadUtils;
import ru.mobnius.vote.utils.SyncUtil;

public class DeleteSynchronizationTest extends ManagerGenerate {
    private DataManager dataManager;
    private DeleteSynchronizationTest.MySynchronization synchronization;
    private String fileId = "";

    @Before
    public void setUp() {
        fileId = UUID.randomUUID().toString();
        synchronization = new DeleteSynchronizationTest.MySynchronization(getDaoSession(), getFileManager(), getCredentials(), getBaseUrl());
        dataManager = DataManager.createInstance(getDaoSession());
        synchronization.initEntities();
    }

    @After
    public void tearDown() {
        synchronization.getDaoSession().getFilesDao().deleteAll();
    }

    @Test
    public void deleteFile() throws IOException {
        Files file = new Files();
        file.id = fileId;
        file.c_name = "readme.txt";

        byte[] fileBytes = "Hello World!!!".getBytes();

        FileManager fileManager = synchronization.getFileManager();
        fileManager.writeBytes(FileManager.FILES, file.c_name, fileBytes);

        file.c_mime = "text/plain";
        file.c_extension = ".txt";
        file.folder = FileManager.FILES;
        file.d_date = DateUtil.convertDateToString(new Date());
        file.objectOperationType = DbOperationType.CREATED;

        synchronization.getDaoSession().getFilesDao().insert(file);
        SyncUtil.updateTid(synchronization, FilesDao.TABLENAME, synchronization.fileTid);

        byte[] bytes = synchronization.generatePackage(synchronization.fileTid, (Object) null);
        byte[] results = (byte[]) synchronization.sendBytes(synchronization.fileTid, bytes);
        PackageReadUtils utils = new PackageReadUtils(results, synchronization.isZip());
        synchronization.onProcessingPackage(utils, synchronization.fileTid);
        Object[] array = synchronization.getRecords(FilesDao.TABLENAME, "").toArray();

        Files resultFile = getFile(array, file.c_name);

        Assert.assertNotNull(resultFile);
        utils.destroy();
        synchronization.getDaoSession().getFilesDao().detachAll();

        dataManager.removeFile(file.id);

        SyncUtil.updateTid(synchronization, FilesDao.TABLENAME, synchronization.fileTid);
        synchronization.getDaoSession().getFilesDao().detachAll();
        bytes = synchronization.generatePackage(synchronization.fileTid, (Object) null);
        results = (byte[]) synchronization.sendBytes(synchronization.fileTid, bytes);
        utils = new PackageReadUtils(results, synchronization.isZip());
        synchronization.onProcessingPackage(utils, synchronization.fileTid);
        array = synchronization.getRecords(FilesDao.TABLENAME, "").toArray();

        resultFile = getFile(array, file.c_name);
        Assert.assertNull(resultFile);
    }

    private Files getFile(Object[] array, String fileName) {
        Files resultFile = null;
        for (Object obj : array) {
            if (obj instanceof Files) {
                resultFile = (Files) obj;
                if (resultFile.c_name.equals(fileName)) {
                    break;
                } else {
                    resultFile = null;
                }
            }
        }
        return resultFile;
    }

    public class MySynchronization extends FileTransferWebSocketSynchronization {

        private BasicCredentials mCredentials;
        private String mBaseUrl;

        public MySynchronization(DaoSession daoSession, FileManager fileManager, BasicCredentials credentials, String baseUrl) {
            super(daoSession, "test", fileManager, PreferencesManager.ZIP_CONTENT);
            useAttachments = true;
            oneOnlyMode = true;
            serverSidePackage = new FullServerSidePackage();
            mCredentials = credentials;
            mBaseUrl = baseUrl;
        }

        @Override
        protected Object sendBytes(String tid, byte[] bytes) {
            super.sendBytes(tid, bytes);

            try {
                MultipartUtility multipartUtility = new MultipartUtility(mBaseUrl + "/synchronization/" + PreferencesManager.SYNC_PROTOCOL, mCredentials);
                multipartUtility.addFilePart("synchronization", bytes);
                return multipartUtility.finish();
            } catch (Exception exc) {
                return null;
            }
        }

        @Override
        protected void initEntities() {
            fileTid = UUID.randomUUID().toString();
            addEntity(new EntityAttachment(FilesDao.TABLENAME, true, true)
                    .setSelect("id", "c_name", "d_date", "c_mime", "c_extension", "n_size", "'files' as folder", "ba_data")
                    .setFilter(new FilterItem("id", fileId))
                    .setTid(fileTid)
                    .setParam(null));
        }
    }
}
