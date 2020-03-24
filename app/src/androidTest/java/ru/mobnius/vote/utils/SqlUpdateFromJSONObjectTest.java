package ru.mobnius.vote.utils;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SqlUpdateFromJSONObjectTest {

    @Test
    public void sql() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 1);
        jsonObject.put("name", "Имя");
        jsonObject.put("age", 31);

        SqlUpdateFromJSONObject insert = new SqlUpdateFromJSONObject(jsonObject, "users", "id");
        String query = insert.convertToQuery(false);

        Assert.assertEquals(query, "UPDATE users set name  = ?, age  = ? where id = ?");
        Object[] values = insert.getValues(jsonObject, false);
        Assert.assertEquals(values[1] , 31);
        Assert.assertEquals(values[0] , "Имя");
        Assert.assertEquals(values[2] , 1);

        query = insert.convertToQuery(true);
        Assert.assertEquals(query, "UPDATE users set name  = ?, age  = ? where id = ? and (OBJECT_OPERATION_TYPE = ? OR OBJECT_OPERATION_TYPE = ?)");
        values = insert.getValues(jsonObject, true);
        Assert.assertEquals(values[3] , null);
        Assert.assertEquals(values[4] , "");
    }
}
