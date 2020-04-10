package ru.mobnius.vote.ui.data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import ru.mobnius.vote.ManagerGenerate;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.configuration.ConfigurationSetting;
import ru.mobnius.vote.data.storage.models.RouteHistory;
import ru.mobnius.vote.data.storage.models.RouteStatuses;
import ru.mobnius.vote.data.storage.models.RouteTypes;
import ru.mobnius.vote.data.storage.models.Routes;
import ru.mobnius.vote.ui.model.FilterItem;
import ru.mobnius.vote.ui.model.RouteItem;
import ru.mobnius.vote.utils.DateUtil;

import static org.junit.Assert.*;

public class RouteFilterManagerTest extends ManagerGenerate {
    private String mRouteId = UUID.randomUUID().toString();

    @Before
    public void setUp() {
        RouteTypes routeType = new RouteTypes();
        routeType.id = (long)1;
        routeType.c_name = "Type";
        getDaoSession().getRouteTypesDao().insert(routeType);

        RouteStatuses routeStatuses = new RouteStatuses();
        routeStatuses.id = (long)1;
        routeStatuses.c_name = "Принят";
        getDaoSession().getRouteStatusesDao().insert(routeStatuses);

        RouteHistory routeHistory = new RouteHistory();
        routeHistory.id = UUID.randomUUID().toString();
        routeHistory.fn_route = mRouteId;
        routeHistory.d_date = DateUtil.convertDateToString(new Date());
        routeHistory.fn_status = routeStatuses.id;
        routeHistory.fn_user = 1;
        getDaoSession().getRouteHistoryDao().insert(routeHistory);

        Routes route = new Routes();
        route.id = mRouteId;
        route.d_date = DateUtil.convertDateToString(new Date());
        route.c_number = "111";
        Date dtStart = new Date();
        dtStart.setTime(new Date().getTime() - 24 * 60 * 60 * 1000);

        Date dtEnd = new Date();
        dtEnd.setTime(new Date().getTime() + 24 * 60 * 60 * 1000);

        route.d_date_end = DateUtil.convertDateToString(dtEnd);
        route.d_date_start = DateUtil.convertDateToString(dtStart);

        route.f_type = routeType.id;
        route.n_order = 0;
        getDaoSession().getRoutesDao().insert(route);
    }

    @After
    public void tearDown() {
        getDaoSession().getRouteTypesDao().deleteAll();
        getDaoSession().getRouteStatusesDao().deleteAll();
        getDaoSession().getRouteHistoryDao().deleteAll();
        getDaoSession().getRoutesDao().deleteAll();
    }

    @Test
    public void toFilters() {
        String key = "routes";
        RouteFilterManager routeFilterManager = new RouteFilterManager(key);

        FilterItem filterDateItem = new FilterItem("date", ConfigurationSetting.DATE, DateUtil.convertDateToString(new Date()));
        FilterItem filterIdItem = new FilterItem("id", mRouteId);

        routeFilterManager.addItem(filterIdItem);
        List<RouteItem> routeItemList = getDataManager().getRouteItems(DataManager.RouteFilter.ALL);
        RouteItem[] routeItems = routeFilterManager.toFilters(routeItemList.toArray(new RouteItem[0]));
        assertEquals(routeItems.length, 1);

        routeFilterManager.removeItem(filterIdItem);
        routeFilterManager.addItem(filterDateItem);
        routeItemList = getDataManager().getRouteItems(DataManager.RouteFilter.ALL);
        routeItems = routeFilterManager.toFilters(routeItemList.toArray(new RouteItem[0]));
        assertEquals(routeItems.length, 1);
    }
}