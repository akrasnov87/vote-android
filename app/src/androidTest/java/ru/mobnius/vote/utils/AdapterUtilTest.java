package ru.mobnius.vote.utils;

import android.content.Context;
import android.widget.SimpleAdapter;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.mobnius.vote.Names;

import static org.junit.Assert.*;

public class AdapterUtilTest {

    private SimpleAdapter mSimpleAdapter;
    private Context appContext;
    private ArrayList<Map<String, Object>> mMaps;

    @Before
    public void setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        mMaps = new ArrayList<>(3);
        Map<String, Object> m;
        for(int i = 0; i < 3; i++) {
            m = new HashMap<>();
            m.put(Names.ID, i);
            m.put(Names.NAME, i);

            mMaps.add(m);
        }
        mSimpleAdapter = new SimpleAdapter(appContext, mMaps, android.R.layout.simple_list_item_1, new String[0], new int[0]);
    }

    @Test
    public void getAdapterItemPosition() {
        int position = AdapterUtil.getAdapterItemPosition(mSimpleAdapter, 1);
        assertEquals(position, 1);
    }

    @Test
    public void getAdapterItemPosition1() {
        int position = AdapterUtil.getAdapterItemPosition(mSimpleAdapter, (long)2);
        assertEquals(position, 2);
    }

    @Test
    public void getAdapterItemPosition2() {
        int position = AdapterUtil.getAdapterItemPosition(mSimpleAdapter, "0");
        assertEquals(position, 0);
    }
}