package ru.mobnius.vote.data.manager.synchronization;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import ru.mobnius.vote.ManagerGenerate;
import ru.mobnius.vote.data.manager.DbOperationType;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.credentials.BasicCredentials;
import ru.mobnius.vote.data.storage.models.DaoSession;
import ru.mobnius.vote.data.storage.models.Tracking;
import ru.mobnius.vote.data.storage.models.TrackingDao;
import ru.mobnius.vote.utils.DateUtil;
import ru.mobnius.vote.utils.SyncUtil;

public class ServiceSynchronizationTest extends ManagerGenerate {
    /**
     * массив с тестовыми данными
     */
    private ArrayList<Tracking> tracking;

    private ServiceSynchronizationTest.MySynchronization synchronization;

    @Before
    public void setUp(){
        synchronization = new MySynchronization(getDaoSession(), getCredentials());
    }

    @After
    public void tearDown() {
        synchronization.getDaoSession().getTrackingDao().deleteAll();
    }

    private void generateData(){
        tracking = new ArrayList<>();
        for(int i =0; i < 10; i++) {
            Tracking tracking = new Tracking();
            tracking.d_date = DateUtil.convertDateToString(new Date());
            tracking.n_longitude = i* 10;
            tracking.n_latitude = i* 10;
            tracking.c_network_status = "LTE";
            tracking.fn_user = 1;
            tracking.setObjectOperationType(DbOperationType.CREATED);
            this.tracking.add(tracking);
            synchronization.getDaoSession().getTrackingDao().insert(tracking);
        }
    }

    /**
     * тестирование полного цикла
     */
    @Test
    public void fullCycle() throws Exception {
        String tid = UUID.randomUUID().toString();
        synchronization.addEntity(new Entity(TrackingDao.TABLENAME).setTid(tid));

        generateData();

        SyncUtil.updateTid(synchronization, TrackingDao.TABLENAME, tid);
        byte[] bytes = synchronization.generatePackage(tid, TrackingDao.TABLENAME);
        byte[] results = (byte[]) synchronization.sendBytes(tid, bytes);
        String[] array = new String[1];
        array[0] = tid;
        synchronization.processingPackage(array, results);
        Object[] records = synchronization.getRecords(TrackingDao.TABLENAME, tid).toArray();
        for(Object o : records) {
            Assert.assertNotNull(o);
        }
        Assert.assertEquals(records.length, 0);

        Assert.assertNotEquals(synchronization.getFinishStatus(), FinishStatus.FAIL);

        // тут повторно отправляем
        for(Tracking tracking : tracking){
            synchronization.getDaoSession().getTrackingDao().insert(tracking);
        }

        SyncUtil.updateTid(synchronization, TrackingDao.TABLENAME, tid);
        bytes = synchronization.generatePackage(tid, TrackingDao.TABLENAME);
        results = (byte[]) synchronization.sendBytes(tid, bytes);
        array = new String[1];
        array[0] = tid;
        synchronization.processingPackage(array, results);
        records = synchronization.getRecords(TrackingDao.TABLENAME, tid).toArray();
        Assert.assertEquals(records.length, 10);

        Assert.assertEquals(synchronization.getFinishStatus(), FinishStatus.FAIL);

        if(synchronization.getFinishStatus() == FinishStatus.FAIL){
            synchronization.oneOnlyMode = true;
            synchronization.changeFinishStatus(FinishStatus.NONE);
            SyncUtil.updateTid(synchronization, TrackingDao.TABLENAME, tid);
            bytes = synchronization.generatePackage(tid, TrackingDao.TABLENAME);
            results = (byte[]) synchronization.sendBytes(tid, bytes);
            array = new String[1];
            array[0] = tid;
            synchronization.processingPackage(array, results);
            records = synchronization.getRecords(TrackingDao.TABLENAME, tid).toArray();
            Assert.assertEquals(records.length, 0);
        }

        Assert.assertNotEquals(synchronization.getFinishStatus(), FinishStatus.FAIL);

        synchronization.destroy();
    }

    static class MySynchronization extends ServiceSynchronization {

        private final BasicCredentials mCredentials;

        MySynchronization(DaoSession daoSession, BasicCredentials credentials) {
            super(daoSession, PreferencesManager.ZIP_CONTENT);
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
    }
}