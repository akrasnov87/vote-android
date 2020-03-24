package ru.mobnius.vote.ui.model;

import org.junit.Before;
import org.junit.Test;

import ru.mobnius.vote.data.manager.configuration.ConfigurationSetting;

import static org.junit.Assert.*;

public class FilterItemTest {

    private FilterItem mFilterItem;
    private FilterItem mShortFilterItem;

    @Before
    public void setUp() {
        mFilterItem = new FilterItem("field", ConfigurationSetting.INTEGER, "1");
        mShortFilterItem = new FilterItem("field", "1");
    }

    @Test
    public void getter() {
        assertEquals(mFilterItem.getName(), mShortFilterItem.getName());
        assertEquals(mFilterItem.getType(), ConfigurationSetting.INTEGER);
        assertEquals(mShortFilterItem.getType(), ConfigurationSetting.TEXT);
        assertEquals(mFilterItem.getValue(), mShortFilterItem.getValue());
    }
}