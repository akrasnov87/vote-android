package ru.mobnius.vote.utils;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ru.mobnius.vote.SimpleTest;
import ru.mobnius.vote.ui.fragment.tools.ContactItem;

import static org.junit.Assert.*;

public class JsonUtilTest extends SimpleTest {
    private List<ContactItem> mContactItems;
    private final String KEY = "key";
    private final String VALUE = "value";

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
        assertEquals(json, "[{\"b_default\":true,\"c_key\":\"key\",\"c_value\":\"value\",\"d_created\":null},{\"b_default\":false,\"c_key\":\"\",\"c_value\":\"\",\"d_created\":null}]");
    }

    @Test
    public void convertToContacts() {
        String json = "[{\"b_default\":true,\"c_key\":\"key\",\"c_value\":\"value\",\"d_created\":null},{\"b_default\":false,\"c_key\":\"\",\"c_value\":\"\",\"d_created\":null}]";
        List<ContactItem> items = JsonUtil.convertToContacts(json);
        assert items != null;
        assertEquals(2, items.size());
        assertEquals( KEY, items.get(0).c_key);
        assertEquals(VALUE, items.get(0).c_value);
        assertTrue(items.get(0).b_default);
        assertEquals( "", items.get(1).c_key);
    }
}