package ru.mobnius.vote.data.manager.jsonb;


import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.mobnius.vote.utils.DateUtil;

@RunWith(AndroidJUnit4.class)
public class JsonBUtilsTest {
    private List<JsonBItem> mList;

    @Before
    public void setUp(){
        mList = new ArrayList<>();
        JsonBItem jsonBItem1 = new JsonBItem();
        jsonBItem1.c_key = "911";
        jsonBItem1.c_value = "служба спасения";
        jsonBItem1.b_default = false;
        jsonBItem1.d_created = DateUtil.convertDateToUserString(new Date());
        mList.add(jsonBItem1);

        JsonBItem jsonBItem2 = new JsonBItem();
        jsonBItem2.c_key = "112";
        jsonBItem2.c_value = "служба спасения";
        jsonBItem2.b_default = true;
        jsonBItem2.d_created = DateUtil.convertDateToUserString(new Date());
        mList.add(jsonBItem2);
    }

    @Test
    public void convertToJson() {
        String toJson = JsonBUtils.convertToJson(mList);

        List<JsonBItem> list = JsonBUtils.convertToList(toJson);
        assert list != null;
        Assert.assertEquals(list. size(), 2);

        JsonBItem jsonBItem = list.get(1);
        Assert.assertEquals(jsonBItem.c_key, "112");

        Assert.assertNull(JsonBUtils.convertToList("[{\"name\":\"911\"}]"));
    }

    @Test
    public void toUserString() {
        String str = JsonBUtils.toUserString(mList);
        Assert.assertEquals("911 - служба спасения, 112 - служба спасения", str);
    }
}
