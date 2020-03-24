package ru.mobnius.vote.data.manager.jsonb;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class JsonBItemTest {

    private List<JsonBItem> myJsonBS;
    private JsonBItem contactItem;

    @Before
    public void setUp() {
        contactItem = new JsonBItem();
        contactItem.c_key = "+79278421477";
        contactItem.c_value = "Александр Краснов";

        myJsonBS = new ArrayList<>();
        myJsonBS.add(contactItem);
    }

    @Test
    public void updateCreated() {
        String jsonStr = JsonBUtils.convertToJson(myJsonBS);
        assertEquals(jsonStr, "[{\"b_default\":false,\"c_key\":\"+79278421477\",\"c_value\":\"Александр Краснов\",\"d_created\":null}]");
        contactItem.updateCreated();
        jsonStr = JsonBUtils.convertToJson(myJsonBS);
        assertEquals(jsonStr, "[{\"b_default\":false,\"c_key\":\"+79278421477\",\"c_value\":\"Александр Краснов\",\"d_created\":\""+contactItem.d_created+"\"}]");
    }

    @After
    public void downUp() {
        contactItem.updateCreated();
        contactItem.b_default = true;
        contactItem.c_value = "";
        contactItem.c_key = "";

        String jsonStr = JsonBUtils.convertToJson(myJsonBS);
        assertEquals(jsonStr, "[{\"b_default\":true,\"c_key\":\"\",\"c_value\":\"\",\"d_created\":\""+contactItem.d_created+"\"}]");
    }
}