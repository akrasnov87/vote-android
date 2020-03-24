package ru.mobnius.vote.data.manager.rpc;

import org.junit.Test;

import static org.junit.Assert.*;

public class QueryDataTest {

    @Test
    public void getInstance() {
        QueryData queryData = new QueryData();
        queryData.filter = new Object[5];
        queryData.filter[0] = new FilterItem("name", "test");
        queryData.filter[1] = QueryData.FILTER_OR;
        queryData.filter[2] = new FilterItem("name", "test2");
        queryData.filter[3] = QueryData.FILTER_AND;
        queryData.filter[4] = new FilterItem("name", "test3");

        queryData.sort = new SortItem[1];
        queryData.sort[0] = new SortItem("d_date", SortItem.ASC);

        assertEquals(queryData.page, 1);
        assertEquals(queryData.query, "");
        assertEquals(queryData.start, 0);
        assertEquals(queryData.limit, 25);
    }
}