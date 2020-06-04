package ru.mobnius.vote.data.manager;

import android.location.Location;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.vote.Vote;
import ru.mobnius.vote.data.storage.models.DaoSession;
import ru.mobnius.vote.data.storage.models.PointTypes;
import ru.mobnius.vote.data.storage.models.PointTypesDao;
import ru.mobnius.vote.data.storage.models.Results;
import ru.mobnius.vote.data.storage.models.UserPoints;
import ru.mobnius.vote.ui.data.OnVoteListener;
import ru.mobnius.vote.utils.DateUtil;
import ru.mobnius.vote.utils.StringUtil;

/**
 * Управление документами
 */
public class DocumentManager {
    private final long mUserId;
    private final OnVoteListener mVoteListener;

    public DocumentManager(OnVoteListener context) {
        mVoteListener = context;
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
    public String createResult(String userPointId, long question, long answer, String notice, String jData, int order, boolean warning) {
        Results result = new Results();
        result.id = UUID.randomUUID().toString();
        result.fn_user = mUserId;
        result.d_date = DateUtil.convertDateToString(new Date());
        result.fn_user_point = userPointId;
        result.fn_point = mVoteListener.getPointId();
        result.fn_route = mVoteListener.getRouteId();
        result.fn_type = 1;
        result.c_notice = notice;
        result.b_warning = warning;
        result.fn_question = question;
        result.fn_answer = answer;
        result.n_order = order;

        if(!StringUtil.isEmptyOrNull(jData)) {
            result.jb_data = jData;
        }
        result.isSynchronization = false;
        result.objectOperationType = DbOperationType.CREATED;
        getDaoSession().getResultsDao().insert(result);
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

        List<PointTypes> pointTypes = DataManager.getInstance().getDaoSession().getPointTypesDao().queryBuilder().where(PointTypesDao.Properties.C_const.eq(type)).list();
        if(pointTypes.size() > 0) {
            userPointTypeId = pointTypes.get(0).id;
        } else {
            return null;
        }

        UserPoints userPoint = new UserPoints();
        userPoint.id = UUID.randomUUID().toString();
        userPoint.fn_point = mVoteListener.getPointId();
        userPoint.fn_route = mVoteListener.getRouteId();
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
        getDaoSession().getUserPointsDao().insert(userPoint);

        return userPoint.id;
    }

    /**
     * Сохранения результата опроса
     */
    public void saveVote(OnVoteListener listener) {
        // тут нужно создать userpoint
        Location location = listener.getLocation();
        String userPointID = createUserPoint(null, location.getLongitude(), location.getLatitude(), null);

        for(Vote vote : listener.getVoteManager().getList()) {
            createResult(userPointID, vote.questionId, vote.answerId, vote.getComment(), vote.getJbTel(), vote.getOrder(), location.getAccuracy() == 0);
        }
    }

    private DaoSession getDaoSession() {
        return DataManager.getInstance().getDaoSession();
    }
}
