package ru.mobnius.vote.data.manager.synchronization;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import ru.mobnius.vote.ManagerGenerate;
import ru.mobnius.vote.data.GlobalSettings;
import ru.mobnius.vote.data.manager.DbOperationType;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.credentials.BasicCredentials;
import ru.mobnius.vote.data.storage.models.Audits;
import ru.mobnius.vote.data.storage.models.AuditsDao;
import ru.mobnius.vote.data.storage.models.DaoSession;
import ru.mobnius.vote.data.storage.models.Tracking;
import ru.mobnius.vote.data.storage.models.TrackingDao;
import ru.mobnius.vote.utils.AuditUtils;
import ru.mobnius.vote.utils.DateUtil;
import ru.mobnius.vote.utils.SyncUtil;

import static ru.mobnius.vote.utils.SyncUtil.resetTid;

public class BaseSynchronizationTest extends ManagerGenerate {
    private MySynchronization synchronization;

    private ArrayList<Tracking> takings;

    @Before
    public void setUp() {
        synchronization = new MySynchronization(getDaoSession(), getCredentials());
    }

    @After
    public void tearDown() {
        synchronization.getDaoSession().getAuditsDao().deleteAll();
        synchronization.getDaoSession().getTrackingDao().deleteAll();
    }

    void generateData(){
        takings = new ArrayList<>();
        for(int i =0; i < 10; i++) {
            Tracking tracking = new Tracking();
            tracking.fn_user = 1;
            tracking.c_network_status = "LTE";
            tracking.n_latitude = i * 10.0;
            tracking.n_longitude = i * 10.0;
            tracking.d_date = DateUtil.convertDateToString(new Date());
            tracking.setObjectOperationType(DbOperationType.CREATED);
            takings.add(tracking);
            synchronization.getDaoSession().getTrackingDao().insert(tracking);
        }
        for(int i =0; i < 10; i++) {
            Audits audit = new Audits();
            audit.d_date = DateUtil.convertDateToString(new Date());
            audit.fn_user = Long.parseLong(String.valueOf(GlobalSettings.DEFAULT_USER_ID));
            audit.c_type = AuditUtils.ON_AUTH;
            audit.c_data = "Номер " + i;
            audit.tid = UUID.randomUUID().toString();
            audit.setObjectOperationType(DbOperationType.CREATED);
            synchronization.getDaoSession().getAuditsDao().insert(audit);
        }
    }

    @Test
    public void oneTable() throws Exception {
        String tid = UUID.randomUUID().toString();
        synchronization.addEntity(new Entity(TrackingDao.TABLENAME).setTid(tid));

        generateData();

        Object[] records = synchronization.getRecords(TrackingDao.TABLENAME, "").toArray();
        for(Object o : records) {
            Assert.assertNotNull(o);
        }
        Assert.assertEquals(records.length, 10);

        SyncUtil.updateTid(synchronization, TrackingDao.TABLENAME, tid);

        records = synchronization.getRecords(TrackingDao.TABLENAME, tid).toArray();
        Assert.assertEquals(records.length, 10);
        boolean reset = resetTid(synchronization);
        Assert.assertTrue(reset);
        boolean update = SyncUtil.updateTid(synchronization, TrackingDao.TABLENAME, tid);
        Assert.assertTrue(update);
        records = synchronization.getRecords(TrackingDao.TABLENAME, tid).toArray();
        Assert.assertEquals(records.length, 10);

        byte[] bytes = synchronization.generatePackage(tid, TrackingDao.TABLENAME);
        byte[] results = (byte[]) synchronization.sendBytes(tid, bytes);
        String[] array = new String[1];
        array[0] = tid;
        synchronization.processingPackage(array, results);
        records = synchronization.getRecords(TrackingDao.TABLENAME, tid).toArray();
        Assert.assertEquals(records.length, 0);

        synchronization.destroy();
    }

    /**
     * тестирование на добавление дуликатов строк
     */
    @Test
    public void duplicateRecords() throws Exception {
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
        synchronization.getDaoSession().getTrackingDao().deleteAll();

        // тут повторно отправляем
        for(Tracking tracking : takings){
            synchronization.getDaoSession().getTrackingDao().insert(tracking);
        }
        // принудительно указывается, что нужно передать данные в другом режиме
        synchronization.oneOnlyMode = true;
        SyncUtil.updateTid(synchronization, TrackingDao.TABLENAME, tid);
        bytes = synchronization.generatePackage(tid, TrackingDao.TABLENAME);
        results = (byte[]) synchronization.sendBytes(tid, bytes);
        array = new String[1];
        array[0] = tid;
        synchronization.processingPackage(array, results);
        records = synchronization.getRecords(TrackingDao.TABLENAME, tid).toArray();
        Assert.assertEquals(records.length, 0);
    }

    @Test
    public void twoTable() throws Exception {
        String tid = UUID.randomUUID().toString();
        synchronization.addEntity(new Entity(TrackingDao.TABLENAME).setTid(tid));
        synchronization.addEntity(new Entity(AuditsDao.TABLENAME).setTid(tid));

        generateData();

        resetTid(synchronization);
        boolean b = SyncUtil.updateTid(synchronization, tid);
        Assert.assertTrue(b);

        // передача первого пакета
        byte[] bytes = synchronization.generatePackage(tid, TrackingDao.TABLENAME);
        byte[] results = (byte[]) synchronization.sendBytes(tid, bytes);
        String[] array = new String[1];
        array[0] = tid;
        synchronization.processingPackage(array, results);

        // передача второго пакета
        bytes = synchronization.generatePackage(tid, AuditsDao.TABLENAME);
        results = (byte[]) synchronization.sendBytes(tid, bytes);
        array = new String[1];
        array[0] = tid;
        synchronization.processingPackage(array, results);

        Object[] records = synchronization.getRecords(TrackingDao.TABLENAME, "").toArray();
        for(Object o : records) {
            Assert.assertNotNull(o);
        }
        Assert.assertEquals(records.length, 0);
        records = synchronization.getRecords(AuditsDao.TABLENAME, "").toArray();
        Assert.assertEquals(records.length, 0);

        synchronization.destroy();
    }

    class MySynchronization extends ServiceSynchronization {

        private BasicCredentials mCredentials;

        public MySynchronization(DaoSession daoSession, BasicCredentials credentials) {
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
