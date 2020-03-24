package ru.mobnius.vote.data.manager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import ru.mobnius.vote.DbGenerate;
import ru.mobnius.vote.ManagerGenerate;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.credentials.BasicCredentials;
import ru.mobnius.vote.data.manager.credentials.BasicUser;
import ru.mobnius.vote.data.storage.models.Attachments;
import ru.mobnius.vote.data.storage.models.Divisions;
import ru.mobnius.vote.data.storage.models.Files;
import ru.mobnius.vote.data.storage.models.InputMeterReadings;
import ru.mobnius.vote.data.storage.models.InputMeterReadingsDao;
import ru.mobnius.vote.data.storage.models.OutputMeterReadings;
import ru.mobnius.vote.data.storage.models.PointTypes;
import ru.mobnius.vote.data.storage.models.Points;
import ru.mobnius.vote.data.storage.models.RegistrPts;
import ru.mobnius.vote.data.storage.models.ResultTypes;
import ru.mobnius.vote.data.storage.models.Results;
import ru.mobnius.vote.data.storage.models.RouteHistory;
import ru.mobnius.vote.data.storage.models.RouteStatuses;
import ru.mobnius.vote.data.storage.models.RouteTypes;
import ru.mobnius.vote.data.storage.models.Routes;
import ru.mobnius.vote.data.storage.models.SubDivisions;
import ru.mobnius.vote.data.storage.models.TimeZones;
import ru.mobnius.vote.data.storage.models.UserPoints;
import ru.mobnius.vote.ui.model.PointFilter;
import ru.mobnius.vote.ui.model.PointInfo;
import ru.mobnius.vote.ui.model.PointResult;

import ru.mobnius.vote.ui.model.RouteInfo;
import ru.mobnius.vote.utils.DateUtil;
import ru.mobnius.vote.utils.LocationUtil;

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
    public void saveFile() throws IOException {

        String str = "Hello";
        byte[] bytes = str.getBytes();

        Files file = dataManager.saveFile("test.txt", bytes);

        Files dbFile = dataManager.getFile(file.id);

        assertEquals(file.c_name, dbFile.c_name);

        byte[] fileBytes = dataManager.getFileBytes(file.id);

        assertEquals(new String(fileBytes), str);
    }

    @Test
    public void saveAttachment() throws IOException {
        String str = "Hello";
        byte[] bytes = str.getBytes();

        Attachments file = dataManager.saveAttachment("test.txt", 1, "", "", LocationUtil.getLocation(0, 0), bytes);

        Attachments dbFile = getDaoSession().getAttachmentsDao().load(file.id);
        assertEquals(file.c_name, dbFile.c_name);

        byte[] fileBytes = dataManager.getFileAttachment(file.id);

        assertEquals(new String(fileBytes), str);

        Attachments attachments = dataManager.getAttachment(file.id);
        assertNotNull(attachments);

        String fileId = attachments.fn_file;
        dataManager.removeAttachment(attachments.id);

        assertNull(dataManager.getAttachment(attachments.id));
        assertNull(dataManager.getFileBytes(fileId));
        assertNull(FileManager.getInstance().readPath(attachments.folder, attachments.c_name));
    }

    @Test
    public void updateAttachment() throws IOException {
        String str = "Hello";
        byte[] bytes = str.getBytes();

        Attachments file = dataManager.saveAttachment("test.txt", 1, "", "", LocationUtil.getLocation(0, 0), bytes);
        assertEquals(file.fn_type, 1);
        assertEquals(file.c_notice, "");

        Attachments attachments = dataManager.updateAttachment(file.id, 2, "notice");
        assertNotNull(attachments);

        assertEquals(attachments.c_notice, "notice");
        assertEquals(attachments.fn_type, 2);
    }

    @Test
    public void file() throws IOException {
        Files files = dataManager.saveFile("test.txt", "Hello".getBytes());
        assertNotNull(files);
        assertNotNull(dataManager.getFile(files.id));
        assertNotNull(dataManager.getFileBytes(files.id));

        dataManager.removeFile(files.id);
        assertNull(dataManager.getFile(files.id));
        assertNull(dataManager.getFileBytes(files.id));
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
        getDaoSession().getAttachmentsDao().deleteAll();
        getDaoSession().getFilesDao().deleteAll();

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

        Files files = new Files();
        files.id = UUID.randomUUID().toString();
        getDaoSession().getFilesDao().insert(files);

        Attachments attachments = new Attachments();
        attachments.id = UUID.randomUUID().toString();
        attachments.fn_file = files.id;
        attachments.fn_result = results.id;
        attachments.fn_type = 1;
        getDaoSession().getAttachmentsDao().insert(attachments);

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

        files = new Files();
        files.id = UUID.randomUUID().toString();
        files.isSynchronization = true;
        getDaoSession().getFilesDao().insert(files);

        attachments = new Attachments();
        attachments.id = UUID.randomUUID().toString();
        attachments.fn_file = files.id;
        attachments.fn_result = results.id;
        attachments.fn_type = 1;
        attachments.isSynchronization = true;
        getDaoSession().getAttachmentsDao().insert(attachments);

        points = new Points();
        String threePointId = points.id = UUID.randomUUID().toString();
        points.f_registr_pts = UUID.randomUUID().toString();
        points.f_route = routes.id;
        getDaoSession().getPointsDao().insert(points);

        assertFalse(dataManager.getPointState(threePointId).isDone());
        assertFalse(dataManager.getPointState(threePointId).isSync());

        assertTrue(dataManager.getPointState(secondPointId).isDone());
        assertTrue(dataManager.getPointState(secondPointId).isSync());

        assertTrue(dataManager.getPointState(firstPointId).isDone());
        assertFalse(dataManager.getPointState(firstPointId).isSync());
    }

    @Test
    public void getPointResults() {
        getDaoSession().getResultTypesDao().deleteAll();
        getDaoSession().getRoutesDao().deleteAll();
        getDaoSession().getPointsDao().deleteAll();
        getDaoSession().getUserPointsDao().deleteAll();
        getDaoSession().getResultsDao().deleteAll();

        ResultTypes resultTypes = new ResultTypes();
        resultTypes.id = (long)1;
        resultTypes.c_name = "Документ 1";
        resultTypes.n_order = 900;
        getDaoSession().getResultTypesDao().insert(resultTypes);

        resultTypes = new ResultTypes();
        resultTypes.id = (long)2;
        resultTypes.c_name = "Документ 2";
        resultTypes.n_order = 1000;
        getDaoSession().getResultTypesDao().insert(resultTypes);

        resultTypes = new ResultTypes();
        resultTypes.id = (long)3;
        resultTypes.c_name = "Документ 3";
        resultTypes.n_order = 800;
        getDaoSession().getResultTypesDao().insert(resultTypes);

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
        results.fn_point = firstPointId;
        results.fn_route = routes.id;
        results.fn_user_point = userPoints.id;
        getDaoSession().getResultsDao().insert(results);

        points = new Points();
        String secondPointId = points.id = UUID.randomUUID().toString();
        points.f_registr_pts = UUID.randomUUID().toString();
        points.f_route = routes.id;
        getDaoSession().getPointsDao().insert(points);

        List<PointResult> pointResults = dataManager.getPointDocuments(firstPointId);
        assertEquals(3, pointResults.size());
        assertEquals(pointResults.get(0).getName(), "Документ 2");
        assertTrue(pointResults.get(1).isExists());

        pointResults = dataManager.getPointDocuments(secondPointId);
        assertEquals(3, pointResults.size());
        assertFalse(pointResults.get(0).isExists());
        assertFalse(pointResults.get(1).isExists());
    }

    @Test
    public void getRouteInfo() {
        getDaoSession().getRoutesDao().deleteAll();
        getDaoSession().getRouteHistoryDao().deleteAll();
        getDaoSession().getRouteTypesDao().deleteAll();
        getDaoSession().getRouteStatusesDao().deleteAll();

        RouteStatuses routeStatuses = new RouteStatuses();
        routeStatuses.id = (long)1;
        routeStatuses.c_name = "Cоздан";
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
        getDaoSession().getInputMeterReadingsDao().deleteAll();
        getDaoSession().getTimeZonesDao().deleteAll();
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
        registrPts.c_subscr = "111-111";
        registrPts.c_device = "ПУ-111";
        registrPts.c_address = "адрес";
        registrPts.id = UUID.randomUUID().toString();
        registrPts.c_fio = "Иванов Иван Иванович";
        registrPts.f_division = division.id;
        registrPts.f_subdivision = subDivision.id;
        getDaoSession().getRegistrPtsDao().insert(registrPts);

        TimeZones timeZone = new TimeZones();
        timeZone.id = (long)1;
        timeZone.c_name = "Сутки";
        timeZone.c_short_name = "сутки";
        timeZone.n_order = 900;
        getDaoSession().getTimeZonesDao().insert(timeZone);

        TimeZones timeZone2 = new TimeZones();
        timeZone2.id = (long)2;
        timeZone2.c_name = "Ночь";
        timeZone2.c_short_name = "ночь";
        timeZone2.n_order = 1000;
        getDaoSession().getTimeZonesDao().insert(timeZone2);

        Points point = new Points();
        point.id = UUID.randomUUID().toString();
        point.f_registr_pts = registrPts.id;
        getDaoSession().getPointsDao().insert(point);

        InputMeterReadings inputMeterReadings = new InputMeterReadings();
        inputMeterReadings.id = UUID.randomUUID().toString();
        inputMeterReadings.d_date_prev = DateUtil.convertDateToString(new Date());
        inputMeterReadings.n_value_prev = 0.25;
        inputMeterReadings.f_time_zone = timeZone.id;
        inputMeterReadings.f_point = point.id;
        getDaoSession().getInputMeterReadingsDao().insert(inputMeterReadings);

        inputMeterReadings = new InputMeterReadings();
        inputMeterReadings.id = UUID.randomUUID().toString();
        inputMeterReadings.d_date_prev = DateUtil.convertDateToString(new Date());
        inputMeterReadings.n_value_prev = 0.54;
        inputMeterReadings.f_time_zone = timeZone2.id;
        inputMeterReadings.f_point = point.id;
        getDaoSession().getInputMeterReadingsDao().insert(inputMeterReadings);

        PointInfo info = dataManager.getPointInfo(point.id);
        assertNotNull(info);

        assertEquals(info.getSubscrNumber(), registrPts.c_subscr);
        assertEquals(info.getAddress(), registrPts.c_address);
        assertEquals(info.getFio(), registrPts.c_fio);
        assertEquals(info.getSubDivisionName(), subDivision.c_name);
        assertEquals(info.getMeters().length, 2);
        assertEquals(info.getMeters()[0].getTimeZoneName(), "Ночь");
        assertEquals(info.getMeters()[0].getValuePrev(), 0.54, 0);

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
        routeStatuses.c_name = "Cоздан";
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
        fileManager.clearUserFolder();
    }
}