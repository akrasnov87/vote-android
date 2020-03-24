package ru.mobnius.vote.data.manager;

import android.location.Location;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.storage.models.DaoSession;
import ru.mobnius.vote.data.storage.models.InputMeterReadings;
import ru.mobnius.vote.data.storage.models.InputMeterReadingsDao;
import ru.mobnius.vote.data.storage.models.OutputMeterReadings;
import ru.mobnius.vote.data.storage.models.OutputMeterReadingsDao;
import ru.mobnius.vote.data.storage.models.PointTypes;
import ru.mobnius.vote.data.storage.models.PointTypesDao;
import ru.mobnius.vote.data.storage.models.Results;
import ru.mobnius.vote.data.storage.models.TimeZones;
import ru.mobnius.vote.data.storage.models.UserPoints;
import ru.mobnius.vote.ui.model.Document;
import ru.mobnius.vote.ui.model.Meter;
import ru.mobnius.vote.ui.model.PointInfo;
import ru.mobnius.vote.utils.DateUtil;
import ru.mobnius.vote.utils.StringUtil;

/**
 * Управление документами
 */
public class DocumentManager {
    private DaoSession mDaoSession;
    private String mRouteId;
    private String mPointId;
    private long mUserId;

    public DocumentManager(DaoSession daoSession, String routeId, String pointId) {
        mDaoSession = daoSession;
        mPointId = pointId;
        mRouteId = routeId;
        mUserId = Authorization.getInstance().getUser().getUserId();
    }

    /**
     * Создание выходного показания, OutputMeterReadings
     * @param inputMeterRedingsId иден. входного показания, InputMeterReadings
     * @param userPointId иден. пользовательской точки, UserPoints
     * @param value показание
     * @return иден. созданной записи
     */
    public String createOutputMeterReadings(String inputMeterRedingsId, String userPointId, double value) {
        OutputMeterReadings outputMeterReading = new OutputMeterReadings();
        outputMeterReading.id = UUID.randomUUID().toString();
        outputMeterReading.d_date = DateUtil.convertDateToString(new Date());
        outputMeterReading.fn_meter_reading = inputMeterRedingsId;
        outputMeterReading.fn_user_point = userPointId;
        outputMeterReading.n_value = value;
        outputMeterReading.isSynchronization = false;
        outputMeterReading.objectOperationType = DbOperationType.CREATED;

        mDaoSession.getOutputMeterReadingsDao().insert(outputMeterReading);
        return outputMeterReading.id;
    }

    /**
     * Обновление выходного показания, OutputMeterReadings
     * @param id идентификатор показания, OutputMeterReadings
     * @param value показание
     */
    public void updateOutputMeterReadings(String id, double value){
        OutputMeterReadings outputMeterReadings = mDaoSession.getOutputMeterReadingsDao().load(id);
        if(outputMeterReadings != null) {
            outputMeterReadings.d_date = DateUtil.convertDateToString(new Date());
            outputMeterReadings.n_value = value;

            if(!outputMeterReadings.objectOperationType.equals(DbOperationType.CREATED)){
                outputMeterReadings.objectOperationType = DbOperationType.UPDATED;
                outputMeterReadings.isSynchronization = false;
            }

            mDaoSession.getOutputMeterReadingsDao().update(outputMeterReadings);
        }
    }

    /**
     * Создание вызодного результата, Results
     * @param outputTypeId иден. типа выходного результата, ResultTypes
     * @param userPointId иден. пользовательской точки, UserPoints
     * @param notice примечание
     * @param jData дополнительные данные в формате JSON. По умолчанию отпралять null
     * @param warning предупреждение. По умолчанию передавать false
     * @return иден. созданной записи, Results
     */
    public String createResult(long outputTypeId, String userPointId, String notice, String jData, boolean warning) {
        Results result = new Results();
        result.id = UUID.randomUUID().toString();
        result.fn_user = mUserId;
        result.d_date = DateUtil.convertDateToString(new Date());
        result.fn_user_point = userPointId;
        result.fn_point = mPointId;
        result.fn_route = mRouteId;
        result.fn_type = outputTypeId;
        result.c_notice = notice;
        result.b_warning = warning;
        if(!StringUtil.isEmptyOrNull(jData)){
            result.jb_data = jData;
        }
        result.isSynchronization = false;
        result.objectOperationType = DbOperationType.CREATED;
        mDaoSession.getResultsDao().insert(result);
        return result.id;
    }

    /**
     * обновление результата, Results
     * @param id иден. результата Results
     * @param notice примечание
     * @param jData дополнительные данные в формате JSON. По умолчанию отпралять null
     * @param warning предупреждение
     */
    public void updateResult(String id, String notice, String jData, boolean warning) {
        Results result = mDaoSession.getResultsDao().load(id);
        if(result != null) {
            result.b_warning = warning;
            result.c_notice = notice;
            if(!StringUtil.isEmptyOrNull(jData)){
                result.jb_data = jData;
            }
            if(!result.objectOperationType.equals(DbOperationType.CREATED)){
                result.objectOperationType = DbOperationType.UPDATED;
                result.isSynchronization = false;
            }
            mDaoSession.getResultsDao().update(result);
        }
    }

    /**
     * Создание пользовательской точки, UserPoints
     * @param jTel номера телефонов в формате JSON. По умолчанию можно передать null
     * @param jEmail адреса эл. почты в формате JSON. По умолчанию можно передать null
     * @param longitude долгота
     * @param latitude широта
     * @param jData дополнительные данные в формате JSON. По умолчанию отпралять null
     * @param isAnomaly является аномалией или нет, По умолчанию ставить false.
     * @return иден. созданной записи, UserPoints
     */
    public String createUserPoint(String jTel, String jEmail, double longitude, double latitude, String jData, boolean isAnomaly) {
        final String POINT_STANDART = "STANDART";
        final String POINT_ANOMALY = "ANOMALY";
        long userPointTypeId;
        String type = isAnomaly ? POINT_ANOMALY : POINT_STANDART;

        List<PointTypes> pointTypes = mDaoSession.getPointTypesDao().queryBuilder().where(PointTypesDao.Properties.C_const.eq(type)).list();
        if(pointTypes.size() > 0) {
            userPointTypeId = pointTypes.get(0).id;
        } else {
            return null;
        }

        UserPoints userPoint = new UserPoints();
        userPoint.id = UUID.randomUUID().toString();
        userPoint.fn_point = mPointId;
        userPoint.fn_route = mRouteId;
        userPoint.fn_type = userPointTypeId;
        userPoint.fn_user = mUserId;
        userPoint.d_date = DateUtil.convertDateToString(new Date());
        userPoint.n_latitude = latitude;
        userPoint.n_longitude = longitude;
        if(!StringUtil.isEmptyOrNull(jData)) {
            userPoint.jb_data = jData;
        }

        if(!StringUtil.isEmptyOrNull(jTel)) {
            userPoint.jb_tel = jTel;
        }

        if(!StringUtil.isEmptyOrNull(jEmail)) {
            userPoint.jb_email = jEmail;
        }
        userPoint.isSynchronization = false;
        userPoint.objectOperationType = DbOperationType.CREATED;
        mDaoSession.getUserPointsDao().insert(userPoint);

        return userPoint.id;
    }

    /**
     * Создание пользовательской точки, UserPoints
     * @param id иден. пользовательской точки
     * @param jTel номера телефонов в формате JSON. По умолчанию можно передать null
     * @param jEmail адреса эл. почты в формате JSON. По умолчанию можно передать null
     * @param jData дополнительные данные в формате JSON. По умолчанию отпралять null
     */
    public void updateUserPoint(String id, String jTel, String jEmail, String jData) {
        UserPoints userPoint = mDaoSession.getUserPointsDao().load(id);
        if(userPoint != null) {
            if(!StringUtil.isEmptyOrNull(jData)) {
                userPoint.jb_data = jData;
            }

            if(!StringUtil.isEmptyOrNull(jTel)) {
                userPoint.jb_tel = jTel;
            }

            if(!StringUtil.isEmptyOrNull(jEmail)) {
                userPoint.jb_email = jEmail;
            }
            if(!userPoint.objectOperationType.equals(DbOperationType.CREATED)){
                userPoint.objectOperationType = DbOperationType.UPDATED;
                userPoint.isSynchronization = false;
            }
            mDaoSession.getUserPointsDao().update(userPoint);
        }
    }

    /**
     * Получение списка показаний
     * @param userPointId пользовательская точка
     * @return список показаний
     * @throws ParseException
     */
    public Meter[] getOutputMeters(String userPointId) throws ParseException {
        List<OutputMeterReadings> outputMeterReadings = mDaoSession.getOutputMeterReadingsDao().queryBuilder().where(OutputMeterReadingsDao.Properties.Fn_user_point.eq(userPointId)).list();
        List<Meter> meters = new ArrayList<>(outputMeterReadings.size());
        for(OutputMeterReadings outputMeterReading : outputMeterReadings) {
            InputMeterReadings inputMeterReadings = outputMeterReading.getMeterReading();
            Date datePrev = DateUtil.convertStringToDate(inputMeterReadings.d_date_prev);
            Date date = DateUtil.convertStringToDate(outputMeterReading.d_date);
            TimeZones timeZone = inputMeterReadings.getTimeZone();
            String name = timeZone.c_name;
            int order = timeZone.n_order;
            Meter meter = new Meter(name, inputMeterReadings.n_value_prev, datePrev, outputMeterReading.n_value, date, order);
            meter.setInputMeterReadingsId(inputMeterReadings.id);
            meter.setOutputMeterReadingsId(outputMeterReading.id);

            meters.add(meter);
        }

        Collections.sort(meters, new Comparator<Meter>() {
            @Override
            public int compare(Meter o1, Meter o2) {
                return Integer.compare(o2.getOrder(), o1.getOrder());
            }
        });

        return meters.toArray(new Meter[0]);
    }

    /**
     * Получение списка показаний
     * @param pointId точка маршрута
     * @return список показаний
     */
    public Meter[] getInputMeters(String pointId) throws ParseException {
        List<InputMeterReadings> inputMeterReadings = mDaoSession.getInputMeterReadingsDao().queryBuilder().where(InputMeterReadingsDao.Properties.F_point.eq(pointId)).list();
        List<Meter> meters = new ArrayList<>(inputMeterReadings.size());
        for(InputMeterReadings inputMeterReading : inputMeterReadings) {
            Date datePrev = DateUtil.convertStringToDate(inputMeterReading.d_date_prev);
            TimeZones timeZone = inputMeterReading.getTimeZone();
            String name = timeZone.c_name;
            int order = timeZone.n_order;

            double value = 0;
            Date date = null;
            String outputMeterReadingsId = null;

            List<OutputMeterReadings> outputMeterReadings = mDaoSession.getOutputMeterReadingsDao().queryBuilder().where(OutputMeterReadingsDao.Properties.Fn_point.eq(pointId)).list();
            if(outputMeterReadings.size() == 1) {
                value = outputMeterReadings.get(0).n_value;
                date = DateUtil.convertStringToDate(outputMeterReadings.get(0).d_date);
                outputMeterReadingsId = outputMeterReadings.get(0).id;
            }

            Meter meter = new Meter(name, inputMeterReading.n_value_prev, datePrev, value, date, order);
            meter.setInputMeterReadingsId(inputMeterReading.id);
            meter.setOutputMeterReadingsId(outputMeterReadingsId);
            meters.add(meter);
        }

        Collections.sort(meters, new Comparator<Meter>() {
            @Override
            public int compare(Meter o1, Meter o2) {
                return Integer.compare(o2.getOrder(), o1.getOrder());
            }
        });

        return meters.toArray(new Meter[0]);
    }

    /**
     * Получение информации о документе
     * @param resultId идентификатор результата
     * @return документ
     */
    public Document getDocument(String resultId) {
        Results result = mDaoSession.getResultsDao().load(resultId);
        if(result != null) {
            Document document = new Document();
            return document.setNotice(result.c_notice);
        }

        return null;
    }
}
