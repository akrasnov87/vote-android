package ru.mobnius.vote.utils;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ru.mobnius.vote.ui.fragment.tools.ContactItem;

import static org.junit.Assert.*;

public class JsonUtilTest {
    private List<ContactItem> mContactItems;
    private String KEY = "key";
    private String VALUE = "value";

    @Before
    public void setUp() {
        mContactItems = new ArrayList<>();
        ContactItem item = new ContactItem();
        item.setKey(KEY);
        item.setValue(VALUE);
        item.setDefault(true);
        ContactItem item1 = new ContactItem();
        mContactItems.add(item);
        mContactItems.add(item1);
    }

    @Test
    public void isEmpty() {
        assertTrue(JsonUtil.isEmpty(JsonUtil.EMPTY));
        assertFalse(JsonUtil.isEmpty("123"));
    }

    @Test
    public void convertToJson() {
        String json = JsonUtil.convertToJson(mContactItems);
        assertEquals(json, "[{\"b_default\":true,\"c_key\":\"key\",\"c_value\":\"value\",\"d_created\":null},{\"b_default\":false,\"c_key\":null,\"c_value\":null,\"d_created\":null}]");
    }

    @Test
    public void convertToContacts() {
        String json = "[{\"b_default\":true,\"c_key\":\"key\",\"c_value\":\"value\",\"d_created\":null},{\"b_default\":false,\"c_key\":null,\"c_value\":null,\"d_created\":null}]";
        List<ContactItem> items = JsonUtil.convertToContacts(json);
        assert items != null;
        assertEquals(2, items.size());
        assertEquals( KEY, items.get(0).c_key);
        assertEquals(VALUE, items.get(0).c_value);
        assertTrue(items.get(0).b_default);
        assertEquals( "null", items.get(1).c_key);
    }
}