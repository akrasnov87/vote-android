package ru.mobnius.vote.data.manager.synchronization.utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ru.mobnius.vote.DbGenerate;
import ru.mobnius.vote.data.GlobalSettings;
import ru.mobnius.vote.data.manager.DbOperationType;
import ru.mobnius.vote.data.manager.credentials.BasicCredentials;
import ru.mobnius.vote.data.manager.FileManager;
import ru.mobnius.vote.data.manager.packager.FileBinary;
import ru.mobnius.vote.data.manager.packager.BinaryBlock;
import ru.mobnius.vote.data.manager.rpc.RPCRecords;
import ru.mobnius.vote.data.manager.rpc.RPCResult;
import ru.mobnius.vote.data.manager.rpc.RPCResultMeta;
import ru.mobnius.vote.data.storage.models.AttachmentsDao;
import ru.mobnius.vote.data.storage.models.Audits;
import ru.mobnius.vote.data.storage.models.AuditsDao;
import ru.mobnius.vote.data.storage.models.Tracking;
import ru.mobnius.vote.data.storage.models.TrackingDao;
import ru.mobnius.vote.utils.AuditUtils;
import ru.mobnius.vote.utils.DateUtil;

public class ServerSidePackageUtilTest extends DbGenerate {

    @Before
    public void setUp() {
        getDaoSession().getAuditsDao().deleteAll();
        getDaoSession().getTrackingDao().deleteAll();
        getDaoSession().getAttachmentsDao().deleteAll();
    }

    @Test
    public void to() throws JSONException {
        String tid = UUID.randomUUID().toString();
        int blockTid = 1;
        String date = DateUtil.convertDateToString(new Date());

        Audits audit = new Audits();
        audit.fn_user = Long.parseLong(String.valueOf(GlobalSettings.DEFAULT_USER_ID));
        audit.c_type = AuditUtils.ON_AUTH;
        audit.d_date = date;
        audit.objectOperationType = DbOperationType.CREATED;
        audit.tid = tid;
        audit.blockTid = String.valueOf(blockTid);

        getDaoSession().getAuditsDao().insert(audit);
        audit = new Audits();
        audit.fn_user = Long.parseLong(String.valueOf(GlobalSettings.DEFAULT_USER_ID));
        audit.c_type = AuditUtils.ON_AUTH;
        audit.d_date = date;
        getDaoSession().getAuditsDao().insert(audit);

        RPCResult result = new RPCResult();
        RPCResultMeta resultMeta = new RPCResultMeta();
        resultMeta.success = true;
        result.meta = resultMeta;
        result.tid = blockTid;
        result.action = AuditsDao.TABLENAME;
        result.method = "Add";
        RPCRecords records = new RPCRecords();
        records.total = 1;
        JSONObject[] objects = new JSONObject[1];

        JSONObject object = new JSONObject();
        object.put(AuditsDao.Properties.Fn_user.name, GlobalSettings.DEFAULT_USER_ID);
        object.put(AuditsDao.Properties.D_date.name, DateUtil.convertDateToString(new Date()));
        object.put(AuditsDao.Properties.C_type.name, AuditUtils.ON_AUTH);
        object.put(AuditsDao.Properties.Tid.name, tid);
        object.put(AuditsDao.Properties.ObjectOperationType.name, DbOperationType.CREATED);

        objects[0] = object;
        records.records = objects;
        result.result = records;

        MyServerSidePackageUtil sidePackage = new MyServerSidePackageUtil();
        PackageResult packageResult = sidePackage.to(getDaoSession(), result, tid);
        Assert.assertTrue(packageResult.success);

        List<Audits> audits = getDaoSession().getAuditsDao().queryBuilder().where(AuditsDao.Properties.IsSynchronization.eq(true)).list();
        Assert.assertEquals(audits.size(), 1);

        result.meta.success = false;
        result.meta.msg = "Test";
        packageResult = sidePackage.to(getDaoSession(), result, tid);
        Assert.assertTrue(!packageResult.success);
    }

    @Test
    public void from() throws JSONException {
        String tid = UUID.randomUUID().toString();
        int blockTid = 0;
        String date = DateUtil.convertDateToString(new Date());

        Audits audit = new Audits();
        audit.fn_user = Long.parseLong(String.valueOf(GlobalSettings.DEFAULT_USER_ID));
        audit.c_type = AuditUtils.ON_AUTH;
        audit.d_date = date;
        audit.objectOperationType = DbOperationType.CREATED;
        audit.tid = tid;
        audit.blockTid = String.valueOf(blockTid);

        getDaoSession().getAuditsDao().insert(audit);
        audit = new Audits();
        audit.fn_user = Long.parseLong(String.valueOf(GlobalSettings.DEFAULT_USER_ID));
        audit.c_type = AuditUtils.ON_AUTH;
        audit.d_date = date;
        getDaoSession().getAuditsDao().insert(audit);

        RPCResult result = new RPCResult();
        RPCResultMeta resultMeta = new RPCResultMeta();
        resultMeta.success = true;
        result.meta = resultMeta;
        result.tid = blockTid;
        result.action = AuditsDao.TABLENAME;
        result.method = "Query";
        RPCRecords records = new RPCRecords();
        records.total = 1;
        JSONObject[] objects = new JSONObject[1];

        JSONObject object = new JSONObject();
        //object.put("id", audit.getId());
        object.put(AuditsDao.Properties.Fn_user.name, GlobalSettings.DEFAULT_USER_ID);
        object.put(AuditsDao.Properties.D_date.name, DateUtil.convertDateToString(new Date()));
        object.put(AuditsDao.Properties.C_type.name, AuditUtils.ON_AUTH);
        object.put(AuditsDao.Properties.Tid.name, tid);

        objects[0] = object;
        records.records = objects;
        result.result = records;

        MyServerSidePackageUtil sidePackage = new MyServerSidePackageUtil();
        sidePackage.setDeleteRecordBeforeAppend(true);
        PackageResult packageResult = sidePackage.from(getDaoSession(), result, tid, true);
        Assert.assertTrue(packageResult.success);
    }

    @Test
    public void fromDuplicate() throws JSONException {
        String tid = UUID.randomUUID().toString();
        int blockTid = 0;

        Tracking tracking2 = new Tracking();
        tracking2.d_date = DateUtil.convertDateToString(new Date());
        tracking2.n_longitude = 10;
        tracking2.n_latitude = 10;
        tracking2.c_network_status = "LTE";
        tracking2.fn_user = 1;
        tracking2.setObjectOperationType(DbOperationType.CREATED);
        tracking2.tid = tid;
        tracking2.blockTid = String.valueOf(blockTid);

        getDaoSession().getTrackingDao().insert(tracking2);
        Tracking tracking  = new Tracking();
        tracking.d_date = DateUtil.convertDateToString(new Date());
        tracking.n_longitude = 20;
        tracking.n_latitude = 20;
        tracking.c_network_status = "LTE";
        tracking.fn_user = 1;
        getDaoSession().getTrackingDao().insert(tracking);

        RPCResult result = new RPCResult();
        RPCResultMeta resultMeta = new RPCResultMeta();
        resultMeta.success = true;
        result.meta = resultMeta;
        result.tid = blockTid;
        result.action = TrackingDao.TABLENAME;
        result.method = "Query";
        RPCRecords records = new RPCRecords();
        records.total = 1;
        JSONObject[] objects = new JSONObject[1];

        JSONObject object = new JSONObject();
        object.put(TrackingDao.Properties.Id.name, tracking2.getId());
        object.put(TrackingDao.Properties.Fn_user.name, 2);
        object.put(TrackingDao.Properties.D_date.name, DateUtil.convertDateToString(new Date()));
        object.put(TrackingDao.Properties.N_longitude.name, 10);
        object.put(TrackingDao.Properties.N_latitude.name, 10);

        objects[0] = object;
        records.records = objects;
        result.result = records;

        MyServerSidePackageUtil sidePackage = new MyServerSidePackageUtil();
        sidePackage.setDeleteRecordBeforeAppend(false);
        PackageResult packageResult = sidePackage.from(getDaoSession(), result, tid, true);
        Assert.assertTrue(packageResult.success);
        List<Tracking> trackings = getDaoSession().getTrackingDao().queryBuilder().list();
        Assert.assertEquals(trackings.size(), 2);
        trackings = getDaoSession().getTrackingDao().queryBuilder().where(TrackingDao.Properties.Id.eq(tracking2.getId())).list();
        Tracking t = trackings.toArray(new Tracking[0])[0];
        Assert.assertEquals(t.fn_user, tracking2.fn_user);
    }

    @Test
    public void attachmentsFromTest() throws IOException, JSONException {
        String LINK = UUID.randomUUID().toString();
        String tid = UUID.randomUUID().toString();
        int blockTid = 0;

        BasicCredentials credentials = new BasicCredentials(GlobalSettings.DEFAULT_USER_NAME, GlobalSettings.DEFAULT_USER_PASSWORD);
        FileManager fileManager = FileManager.createInstance(credentials, getContext());
        try {
            fileManager.deleteFolder(FileManager.ATTACHMENTS);
        }catch (FileNotFoundException e){

        }

        fileManager.writeBytes(FileManager.ATTACHMENTS, "file1.tmp", "attachmentsToTest".getBytes());

        Attachments attachment = new Attachments();
        attachment.id = LINK;
        attachment.folder = FileManager.ATTACHMENTS;
        attachment.d_date = DateUtil.convertDateToString(new Date());
        attachment.c_name = "file1.tmp";
        attachment.objectOperationType = DbOperationType.CREATED;
        attachment.tid = tid;
        attachment.fn_type = 1;
        attachment.blockTid = String.valueOf(blockTid);

        getDaoSession().getAttachmentsDao().insert(attachment);

        RPCResult result = new RPCResult();
        RPCResultMeta resultMeta = new RPCResultMeta();
        resultMeta.success = true;
        result.meta = resultMeta;
        result.tid = blockTid;
        result.action = AttachmentsDao.TABLENAME;
        result.method = "Query";
        RPCRecords records = new RPCRecords();
        records.total = 1;
        JSONObject[] objects = new JSONObject[1];

        JSONObject object = new JSONObject();
        object.put(AttachmentsDao.Properties.Id.name, LINK);
        object.put(AttachmentsDao.Properties.Fn_type.name, 1);
        object.put(AttachmentsDao.Properties.C_name.name, "file1.tmp");
        object.put(AttachmentsDao.Properties.D_date.name, DateUtil.convertDateToString(new Date()));
        object.put(AttachmentsDao.Properties.Folder.name, FileManager.ATTACHMENTS);

        objects[0] = object;
        records.records = objects;
        result.result = records;

        BinaryBlock binaryBlock = new BinaryBlock();
        binaryBlock.add("file1.tmp", LINK, "test".getBytes());
        FileBinary[] files = binaryBlock.getFiles();

        MyServerSidePackageUtil sidePackage = new MyServerSidePackageUtil();
        sidePackage.attachmentBy(fileManager);
        sidePackage.setFileBinary(files);
        PackageResult packageResult = sidePackage.from(getDaoSession(), result, tid, true, true);
        Assert.assertTrue(packageResult.message, packageResult.success);
        String txt = new String(fileManager.readPath(FileManager.ATTACHMENTS, "file1.tmp"));
        Assert.assertEquals(txt, "test");
    }

    class MyServerSidePackageUtil extends ServerSidePackage {

    }
}