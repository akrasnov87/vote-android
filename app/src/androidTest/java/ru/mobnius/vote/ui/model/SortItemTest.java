package ru.mobnius.vote.ui.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SortItemTest {

    private SortItem mSortItem;
    private SortItem mShortSortItem;

    @Before
    public void setUp() {
        mSortItem = new SortItem("field", SortItem.DESC);
        mShortSortItem = new SortItem("field");
    }

    @Test
    public void getter() {
        assertEquals(mSortItem.getName(), mShortSortItem.getName());
        assertEquals(mSortItem.getType(), SortItem.DESC);
        assertEquals(mShortSortItem.getType(), SortItem.ASC);
    }
}