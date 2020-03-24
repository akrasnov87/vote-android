package ru.mobnius.vote.data.manager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import ru.mobnius.vote.ManagerGenerate;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.credentials.BasicUser;
import ru.mobnius.vote.data.storage.models.InputMeterReadings;
import ru.mobnius.vote.data.storage.models.OutputMeterReadings;
import ru.mobnius.vote.data.storage.models.PointTypes;
import ru.mobnius.vote.data.storage.models.Results;
import ru.mobnius.vote.data.storage.models.TimeZones;
import ru.mobnius.vote.data.storage.models.UserPoints;
import ru.mobnius.vote.ui.model.Meter;
import ru.mobnius.vote.utils.DateUtil;

import static org.junit.Assert.*;

public class DocumentManagerTest extends ManagerGenerate {
    private DocumentManager mDocumentManager;
    private FileManager fileManager;
    private String mPointId;
    private String mRouteId;

    @Before
    public void setUp() {
        mRouteId = UUID.randomUUID().toString();
        mPointId = UUID.randomUUID().toString();
        BasicUser basicUser = getBasicUser();
        Authorization.createInstance(getContext(), getBaseUrl()).setUser(basicUser);

        mDocumentManager = new DocumentManager(getDaoSession(), mRouteId, mPointId);
        fileManager = FileManager.createInstance(basicUser.getCredentials(), getContext());
    }

    @After
    public void tearDown() {
        getDaoSession().getOutputMeterReadingsDao().deleteAll();
        getDaoSession().getInputMeterReadingsDao().deleteAll();
        getDaoSession().getResultsDao().deleteAll();
        getDaoSession().getUserPointsDao().deleteAll();
        getDaoSession().getTimeZonesDao().deleteAll();
        getDaoSession().getPointTypesDao().deleteAll();
        fileManager.clearUserFolder();
    }

    @Test
    public void createOutputMeterReadings() throws ParseException {
        String inputMeterRedingsId = UUID.randomUUID().toString();
        String userPointId = UUID.randomUUID().toString();

        String resultId = mDocumentManager.createOutputMeterReadings(inputMeterRedingsId, userPointId, 1.25);
        OutputMeterReadings outputMeterReadings = getDaoSession().getOutputMeterReadingsDao().load(resultId);
        assertEquals(outputMeterReadings.fn_meter_reading, inputMeterRedingsId);
        assertEquals(outputMeterReadings.fn_user_point, userPointId);
        assertEquals(outputMeterReadings.n_value, 1.25, 0);

        // updateOutputMeterReadings
        mDocumentManager.updateOutputMeterReadings(resultId, 1.50);
        outputMeterReadings = getDaoSession().getOutputMeterReadingsDao().load(resultId);
        assertEquals(outputMeterReadings.n_value, 1.50, 0);
    }

    @Test
    public void createResult() {
        long outputTypeId = 1;
        String userPointId = UUID.randomUUID().toString();
        String notice = "";
        boolean warning = true;

        String resultId = mDocumentManager.createResult(outputTypeId, userPointId, notice, null, warning);
        Results result = getDaoSession().getResultsDao().load(resultId);

        assertEquals(result.fn_type, outputTypeId);
        assertEquals(result.fn_user_point, userPointId);
        assertEquals(result.c_notice, notice);
        assertTrue(result.b_warning);
        notice = "Hello";
        warning = false;

        mDocumentManager.updateResult(resultId, notice, null, warning);
        assertEquals(result.c_notice, notice);
        assertFalse(result.b_warning);
    }

    @Test
    public void createUserPoint() {
        PointTypes pointType = new PointTypes();
        pointType.id = (long)1;
        pointType.c_const = "STANDART";
        getDaoSession().getPointTypesDao().insert(pointType);

        String userPointId = mDocumentManager.createUserPoint(null, null, 0, 0, null, false);
        UserPoints userPoint = getDaoSession().getUserPointsDao().load(userPointId);

        assertNotNull(userPoint);

        mDocumentManager.updateUserPoint(userPointId, "{\"tel\":\"\"}", "{\"email\":\"\"}", "{\"data\":\"\"}");
        userPoint = getDaoSession().getUserPointsDao().load(userPointId);
        assertEquals(userPoint.jb_data, "{\"data\":\"\"}");
        assertEquals(userPoint.jb_email, "{\"email\":\"\"}");
        assertEquals(userPoint.jb_tel, "{\"tel\":\"\"}");
    }

    @Test
    public void  getMeters() throws ParseException {
        String userPointId = UUID.randomUUID().toString();

        TimeZones timeZone = new TimeZones();
        timeZone.id = (long)1;
        timeZone.c_name = "Сутки";
        timeZone.n_order = 900;
        getDaoSession().getTimeZonesDao().insert(timeZone);

        timeZone = new TimeZones();
        timeZone.id = (long)2;
        timeZone.c_name = "Ночь";
        timeZone.n_order = 1000;
        getDaoSession().getTimeZonesDao().insert(timeZone);

        InputMeterReadings inputMeterReading = new InputMeterReadings();
        inputMeterReading.id = UUID.randomUUID().toString();
        inputMeterReading.f_point = mPointId;
        inputMeterReading.f_time_zone = (long)1;
        inputMeterReading.n_value_prev = 1.25;
        inputMeterReading.d_date_prev = DateUtil.convertDateToString(new Date());
        getDaoSession().getInputMeterReadingsDao().insert(inputMeterReading);

        OutputMeterReadings outputMeterReading = new OutputMeterReadings();
        outputMeterReading.n_value = 3.25;
        outputMeterReading.d_date = DateUtil.convertDateToString(new Date());
        outputMeterReading.id = UUID.randomUUID().toString();
        outputMeterReading.fn_user_point = userPointId;
        outputMeterReading.fn_meter_reading = inputMeterReading.id;
        getDaoSession().getOutputMeterReadingsDao().insert(outputMeterReading);

        inputMeterReading = new InputMeterReadings();
        inputMeterReading.id = UUID.randomUUID().toString();
        inputMeterReading.f_point = mPointId;
        inputMeterReading.f_time_zone = (long)2;
        inputMeterReading.n_value_prev = 2.25;
        inputMeterReading.d_date_prev = DateUtil.convertDateToString(new Date());
        getDaoSession().getInputMeterReadingsDao().insert(inputMeterReading);

        outputMeterReading = new OutputMeterReadings();
        outputMeterReading.n_value = 4.25;
        outputMeterReading.d_date = DateUtil.convertDateToString(new Date());
        outputMeterReading.id = UUID.randomUUID().toString();
        outputMeterReading.fn_user_point = userPointId;
        outputMeterReading.fn_meter_reading = inputMeterReading.id;
        getDaoSession().getOutputMeterReadingsDao().insert(outputMeterReading);

        Meter[] meters = mDocumentManager.getOutputMeters(userPointId);
        assertEquals(meters.length, 2);
        assertEquals(meters[0].getValue(), 4.25, 0);
        assertEquals(meters[0].getValuePrev(), 2.25, 0);
    }
}