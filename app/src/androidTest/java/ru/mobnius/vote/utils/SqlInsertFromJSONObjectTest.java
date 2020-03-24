package ru.mobnius.vote.utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.mobnius.vote.DbGenerate;
import ru.mobnius.vote.data.storage.models.DaoSession;
import ru.mobnius.vote.data.storage.models.TrackingDao;

public class SqlInsertFromJSONObjectTest extends DbGenerate {

    private DaoSession mDaoSession;

    @Before
    public void setUp() {
        mDaoSession = getDaoSession();
    }

    @Test
    public void sqlInsertTest() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(TrackingDao.Properties.C_network_status.columnName, "LTE");
        jsonObject.put(TrackingDao.Properties.D_date.columnName, "2019-11-14");
        jsonObject.put(TrackingDao.Properties.Fn_user.columnName, 1);
        jsonObject.put(TrackingDao.Properties.Id.columnName, 100);
        jsonObject.put(TrackingDao.Properties.N_latitude.columnName, 0.0);
        jsonObject.put(TrackingDao.Properties.N_longitude.columnName, 0.0);
        jsonObject.put("no_column", "");

        TrackingDao trackingDao = mDaoSession.getTrackingDao();

        SqlInsertFromJSONObject insert = new SqlInsertFromJSONObject(jsonObject, "users", trackingDao);
        String query = insert.convertToQuery(false);
        Assert.assertEquals(query, "INSERT INTO users(C_NETWORK_STATUS,D_DATE,FN_USER,ID,N_LATITUDE,N_LONGITUDE) VALUES(?,?,?,?,?,?)");
        Object[] values = insert.getValues(jsonObject, false);
        Assert.assertEquals(values[1], "2019-11-14");
        Assert.assertEquals(values[0], "LTE");
        query = insert.convertToQuery(true);

        Assert.assertEquals(query, "INSERT INTO users(C_NETWORK_STATUS,D_DATE,FN_USER,ID,N_LATITUDE,N_LONGITUDE,OBJECT_OPERATION_TYPE,IS_DELETE,IS_SYNCHRONIZATION,TID,BLOCK_TID) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
        values = insert.getValues(jsonObject, true);
        Assert.assertEquals(values[1], "2019-11-14");
        Assert.assertEquals(values[0], "LTE");
        Assert.assertEquals(values[3], 100);
    }
}
