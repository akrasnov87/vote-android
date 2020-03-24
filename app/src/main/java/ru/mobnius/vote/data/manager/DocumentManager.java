package ru.mobnius.vote.data.manager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.storage.models.DaoSession;
import ru.mobnius.vote.data.storage.models.PointTypes;
import ru.mobnius.vote.data.storage.models.PointTypesDao;
import ru.mobnius.vote.data.storage.models.Results;
import ru.mobnius.vote.data.storage.models.UserPoints;
import ru.mobnius.vote.ui.model.Document;
import ru.mobnius.vote.ui.model.Meter;
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
