package ru.mobnius.vote.data.manager;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.storage.models.DaoSession;
import ru.mobnius.vote.data.storage.models.PointTypes;
import ru.mobnius.vote.data.storage.models.PointTypesDao;
import ru.mobnius.vote.data.storage.models.Results;
import ru.mobnius.vote.data.storage.models.ResultsDao;
import ru.mobnius.vote.data.storage.models.UserPoints;
import ru.mobnius.vote.data.storage.models.UserPointsDao;
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
     * @param userPointId иден. пользовательской точки, UserPoints
     * @param notice примечание
     * @param jData дополнительные данные в формате JSON. По умолчанию отпралять null
     * @param warning предупреждение. По умолчанию передавать false
     * @param question если нет, то указать -1
     * @param answer если нет, то указать -1
     * @return иден. созданной записи, Results
     */
    public String createResult(String userPointId, long question, long answer, String notice, String jData, boolean warning) {
        Results result = new Results();
        result.id = UUID.randomUUID().toString();
        result.fn_user = mUserId;
        result.d_date = DateUtil.convertDateToString(new Date());
        result.fn_user_point = userPointId;
        result.fn_point = mPointId;
        result.fn_route = mRouteId;
        result.fn_type = 1;
        result.c_notice = notice;
        result.b_warning = warning;
        result.fn_question = question;
        result.fn_answer = answer;

        if(!StringUtil.isEmptyOrNull(jData)) {
            result.jb_data = jData;
        }
        result.isSynchronization = false;
        result.objectOperationType = DbOperationType.CREATED;
        mDaoSession.getResultsDao().insert(result);
        return result.id;
    }

    /**
     * Создание пользовательской точки, UserPoints
     * @param jTel номера телефонов в формате JSON. По умолчанию можно передать null
     * @param longitude долгота
     * @param latitude широта
     * @param jData дополнительные данные в формате JSON. По умолчанию отпралять null
     * @return иден. созданной записи, UserPoints
     */
    public String createUserPoint(String jTel, double longitude, double latitude, String jData) {
        long userPointTypeId;
        String type = "STANDART";

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

        userPoint.isSynchronization = false;
        userPoint.objectOperationType = DbOperationType.CREATED;
        mDaoSession.getUserPointsDao().insert(userPoint);

        return userPoint.id;
    }

    /**
     * Удаление всех созданных пользовательских точек и результатов
     */
    public void removeAll() {
        // найти пользоват. точку
        List<UserPoints> userPoints = mDaoSession.getUserPointsDao().queryBuilder().where(UserPointsDao.Properties.Fn_point.eq(mPointId)).list();
        if(userPoints.size() > 0) {
            // удалить результаты
            for (UserPoints point :
                    userPoints) {
                mDaoSession.getUserPointsDao().delete(point);

                List<Results> results = mDaoSession.getResultsDao().queryBuilder().where(ResultsDao.Properties.Fn_user_point.eq(point.id)).list();

                for (Results result : results) {
                    mDaoSession.getResultsDao().delete(result);
                }
            }
        }
    }
}
