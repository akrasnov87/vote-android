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
        synchronization = new MySynchronization(getDaoSession(), getFileManager(), getCredentials());
        synchronization.initEntities();
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

    public static class MySynchronization extends ManualSynchronization {
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
