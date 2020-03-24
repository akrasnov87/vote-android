package ru.mobnius.vote.ui.fragment.data;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ru.mobnius.vote.ui.model.Meter;

import static org.junit.Assert.*;

public class DocumentUtilTest {
    private List<DocumentUtil.MeterReading> mList;
    private List<DocumentUtil.MeterReading> mOriginalList;
    private String mNotice;

    @Before
    public void setUp() {
        mNotice = "";
        mList = new ArrayList<>();
        mList.add(new DocumentUtil.MeterReading("0", 0.025));
        mList.add(new DocumentUtil.MeterReading("1", 2.36));

        mOriginalList = new ArrayList<>();
        mOriginalList.add(new DocumentUtil.MeterReading("0", 0.025));
        mOriginalList.add(new DocumentUtil.MeterReading("1", 3.63));
    }


    @Test
    public void getMeters() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(DocumentUtil.NAME_NOTICE, mNotice);

        DocumentUtil serializable = new DocumentUtil(mList, map);
        assertEquals(serializable.getMeters()[0].getValue(), 0.025, 0);
        assertEquals(serializable.getMeters()[1].getValue(), 2.36, 0);
    }

    @Test
    public void isChanged() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(DocumentUtil.NAME_NOTICE, mNotice);

        DocumentUtil serializable = new DocumentUtil(mList, map);
        serializable.setCompareDocument(new DocumentUtil(mOriginalList, map));
        assertTrue(serializable.isChanged());
        map = new HashMap<>();
        map.put(DocumentUtil.NAME_NOTICE, "Hello");

        serializable.setCompareDocument(new DocumentUtil(mList, map));
        assertTrue(serializable.isChanged());
    }

    @Test
    public void getInstance() {
        Meter[] meters = new Meter[2];
        Date dt = new Date();
        Date dt2 = new Date();
        meters[0] = new Meter("", 0.25, dt, 0.0, dt2, 1);
        meters[1] = new Meter("", 0.36, dt, 0.0, dt2, 1);

        DocumentUtil serializable = DocumentUtil.getInstance(meters, "");

        assertEquals(2, serializable.getMeters().length);
        assertEquals(serializable.getDate().getTime(), dt.getTime());
        assertEquals(serializable.getPrevDate().getTime(), dt2.getTime());

        serializable = DocumentUtil.getInstance(new Meter[0], "");
        assertEquals(0, serializable.getMeters().length);
        assertNull(serializable.getDate());
        assertNull(serializable.getPrevDate());
    }
}