package ru.mobnius.vote.data.manager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import ru.mobnius.vote.ManagerGenerate;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.credentials.BasicCredentials;
import ru.mobnius.vote.data.storage.models.Divisions;
import ru.mobnius.vote.data.storage.models.Points;
import ru.mobnius.vote.data.storage.models.RegistrPts;
import ru.mobnius.vote.data.storage.models.Results;
import ru.mobnius.vote.data.storage.models.RouteHistory;
import ru.mobnius.vote.data.storage.models.RouteStatuses;
import ru.mobnius.vote.data.storage.models.RouteTypes;
import ru.mobnius.vote.data.storage.models.Routes;
import ru.mobnius.vote.data.storage.models.SubDivisions;
import ru.mobnius.vote.data.storage.models.UserPoints;
import ru.mobnius.vote.ui.model.PointFilter;
import ru.mobnius.vote.ui.model.PointInfo;

import ru.mobnius.vote.ui.model.RouteInfo;
import ru.mobnius.vote.utils.DateUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DataManagerTest extends ManagerGenerate {
    private DataManager dataManager;
    private FileManager fileManager;

    @Before
    public void setUp() {
        dataManager = DataManager.createInstance(getDaoSession());
        BasicCredentials credentials = new BasicCredentials("inspector", "");
        fileManager = FileManager.createInstance(credentials, getContext());

        RouteTypes routeType = new RouteTypes();
        routeType.id = (long)1;
        routeType.c_name = "тип";
        getDaoSession().getRouteTypesDao().insert(routeType);
    }

    @Test
    public void getCountDonePoints() {
        getDaoSession().getRoutesDao().deleteAll();
        getDaoSession().getPointsDao().deleteAll();
        getDaoSession().getUserPointsDao().deleteAll();

        Routes routes = new Routes();
        routes.id = UUID.randomUUID().toString();
        routes.c_number = "1";
        routes.d_date = DateUtil.convertDateToString(new Date());
        routes.f_type = 1;
        getDaoSession().getRoutesDao().insert(routes);

        Points points = new Points();
        points.id = UUID.randomUUID().toString();
        points.f_registr_pts = UUID.randomUUID().toString();
        points.f_route = routes.id;
        getDaoSession().getPointsDao().insert(points);

        UserPoints userPoints = new UserPoints();
        userPoints.id = UUID.randomUUID().toString();
        userPoints.fn_user = 4;
        userPoints.fn_type = 1;
        userPoints.fn_point = points.id;
        userPoints.fn_route = routes.id;
        getDaoSession().getUserPointsDao().insert(userPoints);

        points = new Points();
        points.id = UUID.randomUUID().toString();
        points.f_registr_pts = UUID.randomUUID().toString();
        points.f_route = routes.id;
        getDaoSession().getPointsDao().insert(points);

        userPoints = new UserPoints();
        userPoints.id = UUID.randomUUID().toString();
        userPoints.fn_user = 4;
        userPoints.fn_type = 1;
        userPoints.fn_point = points.id;
        userPoints.fn_route = routes.id;
        getDaoSession().getUserPointsDao().insert(userPoints);

        points = new Points();
        points.id = UUID.randomUUID().toString();
        points.f_registr_pts = UUID.randomUUID().toString();
        points.f_route = routes.id;
        getDaoSession().getPointsDao().insert(points);

        assertEquals(dataManager.getCountDonePoints(routes.id), 2);

        assertEquals(dataManager.getPoints(routes.id, PointFilter.ALL).size(), 3);
        assertEquals(dataManager.getPoints(routes.id, PointFilter.DONE).size(), 2);
        assertEquals(dataManager.getPoints(routes.id, PointFilter.UNDONE).size(), 1);
        assertEquals(dataManager.getPoints(routes.id, PointFilter.UNDONE).get(0).id, points.id);

        assertEquals(dataManager.getPointItems(routes.id, PointFilter.ALL).size(), 3);
        assertEquals(dataManager.getPointItems(routes.id, PointFilter.DONE).size(), 2);
        assertEquals(dataManager.getPointItems(routes.id, PointFilter.UNDONE).size(), 1);
        assertEquals(dataManager.getPointItems(routes.id, PointFilter.UNDONE).get(0).id, points.id);
    }

    @Test
    public void getRoutes() {
        getDaoSession().getRoutesDao().deleteAll();

        Routes route = new Routes();
        route.id = UUID.randomUUID().toString();
        route.c_number = "1";
        route.d_date = DateUtil.convertDateToString(new Date());
        route.d_date_start = DateUtil.convertDateToString(new GregorianCalendar(2019, 11, 16).getTime());
        route.d_date_end = DateUtil.convertDateToString(new GregorianCalendar(2019, 11, 24).getTime());
        route.f_type = 1;

        getDaoSession().getRoutesDao().insert(route);

        route = new Routes();
        route.id = UUID.randomUUID().toString();
        route.c_number = "2";
        route.d_date = DateUtil.convertDateToString(new Date());
        route.d_date_start = DateUtil.convertDateToString(new GregorianCalendar(3000, 11, 16).getTime());
        route.d_date_end = DateUtil.convertDateToString(new GregorianCalendar(3000, 11, 24).getTime());
        route.f_type = 1;

        getDaoSession().getRoutesDao().insert(route);

        route = new Routes();
        route.id = UUID.randomUUID().toString();
        route.c_number = "3";
        route.d_date = DateUtil.convertDateToString(new Date());
        Calendar cal = Calendar.getInstance();
        route.d_date_start = DateUtil.convertDateToString(new GregorianCalendar(cal.get(Calendar.YEAR), 0, 1).getTime());
        route.d_date_end = DateUtil.convertDateToString(new GregorianCalendar(cal.get(Calendar.YEAR), 11, 31).getTime());
        route.f_type = 1;

        getDaoSession().getRoutesDao().insert(route);

        assertEquals(dataManager.getRoutes(DataManager.RouteFilter.ALL).size(), 3);
        assertEquals(dataManager.getRoutes(DataManager.RouteFilter.ACTIVE).size(), 1);
        assertEquals(dataManager.getRoutes(DataManager.RouteFilter.ACTIVE).get(0).c_number, "3");
        assertEquals(dataManager.getRoutes(DataManager.RouteFilter.PAST).size(), 1);
        assertEquals(dataManager.getRoutes(DataManager.RouteFilter.PAST).get(0).c_number, "1");
        assertEquals(dataManager.getRoutes(DataManager.RouteFilter.FUTURE).size(), 1);
        assertEquals(dataManager.getRoutes(DataManager.RouteFilter.FUTURE).get(0).c_number, "2");

        assertEquals(dataManager.getRouteItems(DataManager.RouteFilter.ALL).size(), 3);
        assertEquals(dataManager.getRouteItems(DataManager.RouteFilter.ACTIVE).size(), 1);
        assertEquals(dataManager.getRouteItems(DataManager.RouteFilter.ACTIVE).get(0).number, "3");
        assertEquals(dataManager.getRouteItems(DataManager.RouteFilter.PAST).size(), 1);
        assertEquals(dataManager.getRouteItems(DataManager.RouteFilter.PAST).get(0).number, "1");
        assertEquals(dataManager.getRouteItems(DataManager.RouteFilter.FUTURE).size(), 1);
        assertEquals(dataManager.getRouteItems(DataManager.RouteFilter.FUTURE).get(0).number, "2");
    }

    @Test
    public void getPointState() {
        getDaoSession().getRoutesDao().deleteAll();
        getDaoSession().getPointsDao().deleteAll();
        getDaoSession().getUserPointsDao().deleteAll();
        getDaoSession().getResultsDao().deleteAll();

        Routes routes = new Routes();
        routes.id = UUID.randomUUID().toString();
        routes.c_number = "1";
        routes.d_date = DateUtil.convertDateToString(new Date());
        routes.f_type = 1;
        getDaoSession().getRoutesDao().insert(routes);

        Points points = new Points();
        String firstPointId = points.id = UUID.randomUUID().toString();
        points.f_registr_pts = UUID.randomUUID().toString();
        points.f_route = routes.id;
        getDaoSession().getPointsDao().insert(points);

        UserPoints userPoints = new UserPoints();
        userPoints.id = UUID.randomUUID().toString();
        userPoints.fn_user = 4;
        userPoints.fn_type = 1;
        userPoints.fn_point = points.id;
        userPoints.fn_route = routes.id;
        userPoints.isSynchronization = true;
        getDaoSession().getUserPointsDao().insert(userPoints);

        Results results = new Results();
        results.id = UUID.randomUUID().toString();
        results.fn_type = 1;
        results.fn_user = 1;
        results.fn_user_point = userPoints.id;
        getDaoSession().getResultsDao().insert(results);

        points = new Points();
        String secondPointId = points.id = UUID.randomUUID().toString();
        points.f_registr_pts = UUID.randomUUID().toString();
        points.f_route = routes.id;
        getDaoSession().getPointsDao().insert(points);

        userPoints = new UserPoints();
        userPoints.id = UUID.randomUUID().toString();
        userPoints.fn_user = 4;
        userPoints.fn_type = 1;
        userPoints.fn_point = points.id;
        userPoints.fn_route = routes.id;
        userPoints.isSynchronization = true;
        getDaoSession().getUserPointsDao().insert(userPoints);

        results = new Results();
        results.id = UUID.randomUUID().toString();
        results.fn_type = 1;
        results.fn_user = 1;
        results.fn_user_point = userPoints.id;
        results.isSynchronization = true;
        getDaoSession().getResultsDao().insert(results);

        points = new Points();
        String threePointId = points.id = UUID.randomUUID().toString();
        points.f_registr_pts = UUID.randomUUID().toString();
        points.f_route = routes.id;
        getDaoSession().getPointsDao().insert(points);

        assertFalse(dataManager.getPointState(threePointId).isDone());
        assertTrue(dataManager.getPointState(threePointId).isSync());

        assertTrue(dataManager.getPointState(secondPointId).isDone());
        assertTrue(dataManager.getPointState(secondPointId).isSync());

        assertTrue(dataManager.getPointState(firstPointId).isDone());
        assertFalse(dataManager.getPointState(firstPointId).isSync());
    }

    @Test
    public void getRouteInfo() {
        getDaoSession().getRoutesDao().deleteAll();
        getDaoSession().getRouteHistoryDao().deleteAll();
        getDaoSession().getRouteTypesDao().deleteAll();
        getDaoSession().getRouteStatusesDao().deleteAll();

        RouteStatuses routeStatuses = new RouteStatuses();
        routeStatuses.id = (long)1;
        routeStatuses.c_name = "Создан";
        getDaoSession().getRouteStatusesDao().insert(routeStatuses);
        routeStatuses = new RouteStatuses();
        routeStatuses.id = (long)2;
        routeStatuses.c_name = "Завершен";
        getDaoSession().getRouteStatusesDao().insert(routeStatuses);

        RouteTypes routeType = new RouteTypes();
        routeType.id = (long)1;
        routeType.c_name = "Обход";

        Routes routes = new Routes();
        routes.id = UUID.randomUUID().toString();
        routes.c_number = "1";
        routes.d_date = "2020-01-15T07:00:00.000";
        routes.d_date_start = "2020-01-15T08:00:00.000";
        routes.d_date_end = "2020-01-16T08:00:00.000";
        routes.c_notice = "Примечание к маршруту";
        routes.f_type = 1;
        getDaoSession().getRoutesDao().insert(routes);

        RouteHistory routeHistory = new RouteHistory();
        routeHistory.id = UUID.randomUUID().toString();
        routeHistory.c_notice = "Начало маршрута";
        routeHistory.fn_status = 1;
        routeHistory.fn_route = routes.id;
        routeHistory.d_date = "2020-01-15T07:00:00.000";
        getDaoSession().getRouteHistoryDao().insert(routeHistory);

        routeHistory = new RouteHistory();
        routeHistory.id = UUID.randomUUID().toString();
        routeHistory.c_notice = "Завершение маршрута";
        routeHistory.fn_status = 2;
        routeHistory.fn_route = routes.id;
        routeHistory.d_date = "2020-01-15T22:00:00.000";
        getDaoSession().getRouteHistoryDao().insert(routeHistory);

        RouteInfo routeInfo = dataManager.getRouteInfo(routes.id);
        assertNotNull(routeInfo);
        assertEquals(routeInfo.getNotice(), "Примечание к маршруту");
        assertFalse(routeInfo.isExtended());
        assertEquals(routeInfo.getHistories().length, 2);
        assertEquals(routeInfo.getHistories()[1].getStatus(), "Завершен");
    }

    @Test
    public void getPointInfo() {
        getDaoSession().getRegistrPtsDao().deleteAll();
        getDaoSession().getDivisionsDao().deleteAll();
        getDaoSession().getSubDivisionsDao().deleteAll();
        getDaoSession().getRoutesDao().deleteAll();
        getDaoSession().getPointsDao().deleteAll();

        Divisions division = new Divisions();
        division.id = (long)1;
        division.c_name = "Отделение";
        getDaoSession().getDivisionsDao().insert(division);

        SubDivisions subDivision = new SubDivisions();
        subDivision.id = (long)1;
        subDivision.c_name = "Участок";
        subDivision.f_division = division.id;
        getDaoSession().getSubDivisionsDao().insert(subDivision);

        RegistrPts registrPts = new RegistrPts();
        registrPts.c_appartament_num = "111-111";
        registrPts.c_house_num = "ПУ-111";
        registrPts.c_address = "адрес";
        registrPts.id = UUID.randomUUID().toString();
        registrPts.c_fio = "Иванов Иван Иванович";
        registrPts.f_division = division.id;
        registrPts.f_subdivision = subDivision.id;
        getDaoSession().getRegistrPtsDao().insert(registrPts);

        Routes route = new Routes();
        route.id = UUID.randomUUID().toString();
        route.c_number = "1";
        getDaoSession().getRoutesDao().insert(route);

        Points point = new Points();
        point.id = UUID.randomUUID().toString();
        point.f_registr_pts = registrPts.id;
        point.f_route = route.id;
        getDaoSession().getPointsDao().insert(point);

        PointInfo info = dataManager.getPointInfo(point.id);
        assertNotNull(info);

        assertEquals(info.getAppartament(), registrPts.c_appartament_num);
        //assertEquals(info.getAddress(), registrPts.c_address);
        assertEquals(info.getFio(), registrPts.c_fio);

        info = dataManager.getPointInfo("sss");
        assertNull(info);
    }

    @Test
    public void setRouteFinish() {
        Authorization.createInstance(getContext(), "");
        Authorization.getInstance().setUser(getBasicUser());

        getDaoSession().getRoutesDao().deleteAll();
        getDaoSession().getRouteHistoryDao().deleteAll();
        getDaoSession().getRouteStatusesDao().deleteAll();

        RouteStatuses routeStatuses = new RouteStatuses();
        routeStatuses.id = (long)1;
        routeStatuses.c_name = "Создан";
        routeStatuses.c_const = "CREATED";
        getDaoSession().getRouteStatusesDao().insert(routeStatuses);

        routeStatuses = new RouteStatuses();
        routeStatuses.id = (long)2;
        routeStatuses.c_name = "Завершен";
        routeStatuses.c_const = "DONED";
        getDaoSession().getRouteStatusesDao().insert(routeStatuses);

        Routes routes = new Routes();
        routes.id = UUID.randomUUID().toString();
        routes.c_number = "1";
        routes.d_date = "2020-01-15T07:00:00.000";
        routes.d_date_start = "2020-01-15T08:00:00.000";
        routes.d_date_end = "2020-01-16T08:00:00.000";
        routes.c_notice = "Примечание к маршруту";
        routes.f_type = 1;
        getDaoSession().getRoutesDao().insert(routes);

        RouteHistory routeHistory = new RouteHistory();
        routeHistory.id = UUID.randomUUID().toString();
        routeHistory.c_notice = "Начало маршрута";
        routeHistory.fn_status = 1;
        routeHistory.fn_route = routes.id;
        routeHistory.d_date = "2020-01-15T07:00:00.000";
        routeHistory.isSynchronization = false;
        getDaoSession().getRouteHistoryDao().insert(routeHistory);

        dataManager.setRouteFinish(routes.id);

        assertTrue(dataManager.isRouteFinish(routes.id));
        assertTrue(dataManager.isRevertRouteFinish(routes.id));
        dataManager.revertRouteFinish(routes.id);
        assertFalse(dataManager.isRouteFinish(routes.id));
    }

    @After
    public void tearDown() {
        getDaoSession().getRouteTypesDao().deleteAll();
    }
}