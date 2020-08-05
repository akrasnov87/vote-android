package ru.mobnius.vote.data.manager;

import android.content.Context;

import org.greenrobot.greendao.query.QueryBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.credentials.BasicUser;
import ru.mobnius.vote.data.storage.models.Answer;
import ru.mobnius.vote.data.storage.models.AnswerDao;
import ru.mobnius.vote.data.storage.models.DaoSession;
import ru.mobnius.vote.data.storage.models.Feedbacks;
import ru.mobnius.vote.data.storage.models.Points;
import ru.mobnius.vote.data.storage.models.PointsDao;
import ru.mobnius.vote.data.storage.models.Question;
import ru.mobnius.vote.data.storage.models.QuestionDao;
import ru.mobnius.vote.data.storage.models.Results;
import ru.mobnius.vote.data.storage.models.ResultsDao;
import ru.mobnius.vote.data.storage.models.RouteHistory;
import ru.mobnius.vote.data.storage.models.RouteHistoryDao;
import ru.mobnius.vote.data.storage.models.RouteStatuses;
import ru.mobnius.vote.data.storage.models.RouteTypes;
import ru.mobnius.vote.data.storage.models.Routes;
import ru.mobnius.vote.data.storage.models.RoutesDao;
import ru.mobnius.vote.data.storage.models.UserPoints;
import ru.mobnius.vote.data.storage.models.UserPointsDao;
import ru.mobnius.vote.data.storage.models.Users;
import ru.mobnius.vote.ui.model.PointFilter;
import ru.mobnius.vote.ui.model.PointInfo;
import ru.mobnius.vote.ui.model.PointItem;
import ru.mobnius.vote.ui.model.PointState;
import ru.mobnius.vote.ui.model.ProfileItem;
import ru.mobnius.vote.ui.model.RouteInfo;
import ru.mobnius.vote.ui.model.RouteItem;
import ru.mobnius.vote.utils.DateUtil;
import ru.mobnius.vote.utils.HardwareUtil;
import ru.mobnius.vote.utils.StringUtil;

/**
 * вспомогательные функции для получения данных от БД Sqlite
 */
public class DataManager {
    private final DaoSession daoSession;

    private static DataManager dataManager;

    public static DataManager createInstance(DaoSession daoSession){
        return dataManager = new DataManager(daoSession);
    }

    public static DataManager getInstance(){
        return dataManager;
    }

    /**
     * конструктор
     * @param daoSession подключение к БД
     */
    private DataManager(DaoSession daoSession){
        this.daoSession = daoSession;
    }

    public DaoSession getDaoSession(){
        return daoSession;
    }

    /**
     * Получить количество выполненных строк в маршруте
     * @param routeId идентификатор маршрута. Если передать null, то статистика будет собрана по всем маршрутам
     * @return количество выполненных строк в маршруте
     */
    public int getCountDonePoints(String routeId) {
        List<UserPoints> userPoints;

        if(routeId == null) {
            userPoints = daoSession.getUserPointsDao().queryBuilder().list();
        } else {
            userPoints = daoSession.getUserPointsDao().queryBuilder()
                    .where(UserPointsDao.Properties.Fn_route.eq(routeId)).list();
        }

        int count = 0;
        String lastUserPointId = "";
        for(UserPoints userPoint : userPoints) {
            if(!userPoint.fn_point.equals(lastUserPointId)) {
                lastUserPointId = userPoint.fn_point;
                count++;
            }
        }

        return count;
    }

    /**
     * Получение списка маршрутов согласно указанному фильтру filter
     * @param filter фильтр запроса. Любое значение из RouteFilter
     * @return Список маршрутов
     */
    public List<Routes> getRoutes(RouteFilter filter) {
        List<Routes> routes = daoSession.getRoutesDao().queryBuilder().orderAsc(RoutesDao.Properties.C_number).list();
        List<Routes> results = new ArrayList<>(routes.size());
        switch (filter) {
            case ALL:
                return routes;

            case ACTIVE:
                for(Routes route : routes) {
                    try {
                        Date dtStart = DateUtil.convertStringToDate(route.d_date_start);
                        Date dtEnd = DateUtil.convertStringToDate(route.d_date_end);
                        long time = new Date().getTime();
                        if(dtStart.getTime() <= time && time <= dtEnd.getTime()) {
                            results.add(route);
                        }
                    }catch (ParseException ignore) {

                    }
                }
                return results;

            case PAST:
                for(Routes route : routes) {
                    try {
                        Date dtEnd = DateUtil.convertStringToDate(route.d_date_end);
                        long time = new Date().getTime();
                        if(time > dtEnd.getTime()) {
                            results.add(route);
                        }
                    }catch (ParseException ignore) {

                    }
                }
                return results;

            case FUTURE:
                for(Routes route : routes) {
                    try {
                        Date dtStart = DateUtil.convertStringToDate(route.d_date_start);
                        long time = new Date().getTime();
                        if(dtStart.getTime() > time) {
                            results.add(route);
                        }
                    }catch (ParseException ignore) {

                    }
                }
                return results;
        }
        return null;
    }

    /**
     * Получение списка маршрута согласно указанному фильтру filter. альтернативный способ
     * @param filter фильтр запроса. Любое значение из RouteFilter
     * @return Список маршрутов
     */
    public List<RouteItem> getRouteItems(RouteFilter filter) {
        List<Routes> routes = getRoutes(filter);
        if(routes != null) {
            List<RouteItem> routeItems = new ArrayList<>();
            for(Routes route : routes) {
                try {
                    RouteItem routeItem = new RouteItem();
                    routeItem.id = route.id;
                    routeItem.date = DateUtil.convertStringToDate(route.d_date);
                    routeItem.dateEnd = DateUtil.convertStringToDate(route.d_date_end);
                    routeItem.dateStart = DateUtil.convertStringToDate(route.d_date_start);
                    if(route.b_extended && route.d_extended != null) {
                        routeItem.extended = DateUtil.convertStringToDate(route.d_extended);
                    }
                    routeItem.isExtended = route.b_extended;
                    routeItem.notice = route.c_notice;
                    routeItem.number = route.c_number;
                    routeItem.order = route.n_order;
                    RouteTypes routeType = route.getType();
                    routeItem.typeId = route.f_type;
                    routeItem.typeName = routeType.c_name;
                    routeItem.count = route.n_count;

                    List<RouteHistory> histories = daoSession.getRouteHistoryDao().queryBuilder()
                            .where(RouteHistoryDao.Properties.Fn_route.eq(route.id)).list();

                    Collections.sort(histories, new Comparator<RouteHistory>() {
                        @Override
                        public int compare(RouteHistory o1, RouteHistory o2) {
                            try {
                                return Long.compare(DateUtil.convertStringToDate(o2.d_date).getTime(), DateUtil.convertStringToDate(o1.d_date).getTime());
                            }catch (ParseException ignore) {
                                return 0;
                            }
                        }
                    });

                    if(histories.size() > 0){
                        RouteHistory routeHistory = histories.get(0);
                        routeItem.statusId = routeHistory.fn_status;
                        routeItem.statusName = routeHistory.getStatus().c_name;
                    }

                    routeItems.add(routeItem);
                }catch (ParseException ignore){

                }
            }
            return routeItems;
        }
        return null;
    }

    /**
     * Получение точек маршрута
     * @param routeId идентификатор маршрута. Можно передать null
     * @param filter фильтр
     * @return список точек
     */
    public List<Points> getPoints(String routeId, PointFilter filter) {
        QueryBuilder<UserPoints> userPointsQueryBuilder = daoSession.getUserPointsDao().queryBuilder().orderAsc(UserPointsDao.Properties.Fn_point);

        QueryBuilder<Points> pointsQueryBuilder = daoSession.getPointsDao().queryBuilder().orderAsc(PointsDao.Properties.N_order);

        List<UserPoints> userPoints;
        if(routeId == null) {
            userPoints = userPointsQueryBuilder.list();
        } else {
            userPoints = userPointsQueryBuilder.where(UserPointsDao.Properties.Fn_route.eq(routeId)).list();
        }
        List<Points> points;

        if(routeId == null) {
            points = pointsQueryBuilder.list();
        } else {
            points = pointsQueryBuilder.where(PointsDao.Properties.F_route.eq(routeId)).list();
        }

        List<Points> results = new ArrayList<>(points.size());

        switch (filter){
            case ALL:
                return points;

            case DONE:
                String lastUserPointId = "";
                for(UserPoints userPoint : userPoints) {
                    if(!lastUserPointId.equals(userPoint.id)) {
                        lastUserPointId = userPoint.id;

                        for(Points point : points) {
                            if(point.id.equals(userPoint.fn_point)) {
                                results.add(point);
                                break;
                            }
                        }
                    }
                }
                return results;

            case UNDONE:
                for (Points point : points) {
                    boolean exists = false;
                    for(UserPoints userPoint : userPoints) {
                        if(userPoint.fn_point.equals(point.id)) {
                            exists = true;
                            break;
                        }
                    }
                    if(!exists) {
                        results.add(point);
                    }
                }
                return results;
        }

        return null;
    }

    /**
     * Альтернативный способ получения точек маршрута
     * @param routeId идентификатор маршрута. Можно передать null
     * @param filter фильтр
     * @return список точек
     */
    public List<PointItem> getPointItems(String routeId, PointFilter filter) {
        List<Points> points = getPoints(routeId, filter);
        if(points != null) {
            List<PointItem> pointItems = new ArrayList<>();

            for(Points point : points) {
                PointItem pointItem = new PointItem();
                pointItem.id = point.id;
                pointItem.priority = point.n_priority;

                PointState pointState = getPointState(point.id);
                if(pointState != null) {
                    pointItem.done = pointState.isDone();
                    pointItem.sync = pointState.isSync();
                    pointItem.color = pointState.getColor();
                    pointItem.rating = pointState.getRating();
                }

                String jd_data = point.getJb_data();
                if(!StringUtil.isEmptyOrNull(jd_data)) {
                    try {
                        JSONObject jsonObject = new JSONObject(jd_data);
                        pointItem.address = jsonObject.getString("c_address");
                        pointItem.appartament = jsonObject.getString("c_appartament_num");
                        pointItem.appartamentNumber = jsonObject.getInt("n_appartament_num");

                        String build = jsonObject.getString("c_build_num");

                        pointItem.houseNumber = jsonObject.getString("c_house_num") + (StringUtil.isEmptyOrNull(build) ? "" : " корп. " + build);
                    } catch (JSONException e) {
                        Logger.error(e);
                    }

                    pointItem.info = point.c_info;
                    pointItem.notice = point.c_notice;

                    pointItem.routeId = point.f_route;
                }

                Routes route = point.getRoute();
                if(route != null) {
                    pointItem.routeName = route.c_number;
                    pointItem.routeTypeName = route.getType().c_name;
                }

                pointItems.add(pointItem);
            }

            return pointItems;
        }

        return null;
    }

    public List<Results> getPointResults(String pointID) {
        return daoSession.getResultsDao().queryBuilder().where(ResultsDao.Properties.Fn_point.eq(pointID)).orderAsc(ResultsDao.Properties.N_order).list();
    }

    /**
     * Вернуть состояние точки маршрута
     * @param pointId идентификатор точки маршрута
     * @return состояние
     */
    public PointState getPointState(String pointId) {
        PointState pointState = new PointState();

        if(daoSession.getUserPointsDao().queryBuilder().where(UserPointsDao.Properties.Fn_point.eq(pointId)).count() > 0) {
            pointState.setDone(true);
        }

        // проверяем на синхронизацию
        List<UserPoints> userPoints = daoSession.getUserPointsDao().queryBuilder().where(UserPointsDao.Properties.Fn_point.eq(pointId)).list();

        for(UserPoints userPoint : userPoints) {
            List<Results> results = daoSession.getResultsDao().queryBuilder().where(ResultsDao.Properties.Fn_user_point.eq(userPoint.id)).list();
            boolean stop = false;
            for(Results result : results) {
                Answer answer = result.getAnswer();
                if(answer != null) {
                    pointState.setColor(answer.c_color);
                    pointState.setRating(result.n_rating);
                }

                if(!result.isSynchronization) {
                    stop = true;
                }
            }
            if(stop || !userPoint.isSynchronization) {
                return pointState;
            }
        }

        pointState.setSync(true);
        return pointState;
    }

    /**
     * Получение информации о маршруте
     * @param routeId идентификатор маршрута
     * @return информация о маршруте
     */
    public RouteInfo getRouteInfo(String routeId) {
        Routes route = daoSession.getRoutesDao().load(routeId);
        if(route != null) {
            try {
                RouteInfo routeInfo = new RouteInfo();

                routeInfo.setNumber(route.c_number);
                routeInfo.setCount(route.n_count);
                routeInfo.setNotice(route.c_notice);
                routeInfo.setDateEnd(DateUtil.convertStringToDate(route.d_date_end));
                routeInfo.setDateStart(DateUtil.convertStringToDate(route.d_date_start));
                routeInfo.setExtended(route.b_extended);
                if(!StringUtil.isEmptyOrNull(route.d_extended)) {
                    routeInfo.setDateExtended(DateUtil.convertStringToDate(route.d_extended));
                }

                List<RouteHistory> histories = daoSession.getRouteHistoryDao().queryBuilder().where(RouteHistoryDao.Properties.Fn_route.eq(routeId)).list();
                for(RouteHistory routeHistory : histories) {
                    RouteStatuses routeStatuses = routeHistory.getStatus();
                    if(routeStatuses != null) {
                        routeInfo.addHistory(routeStatuses.id, routeStatuses.c_name, DateUtil.convertStringToDate(routeHistory.d_date), routeHistory.c_notice);
                    }
                }
                return routeInfo;
            }catch (Exception ignore) {

            }
        }
        return null;
    }

    /**
     * Получение информации о точке маршрута
     * @param pointId идентификатор точки маршрута
     * @return информация о точке маршрута
     */
    public PointInfo getPointInfo(String pointId) {
        Points point = daoSession.getPointsDao().load(pointId);
        if(point != null) {
            PointInfo info = new PointInfo(point.getJb_data());
            info.setNotice(point.c_info);
            info.setAddress(point.getRoute().c_number);
            info.setPriority(point.n_priority);
            return info;
        }
        return null;
    }

    /**
     * Обновление рейтинга
     * @param resultID иден. результата
     * @param rating рейтинг
     */
    public void updateRating(String resultID, int rating) {
        Results result = daoSession.getResultsDao().load(resultID);
        result.n_rating = rating;
        daoSession.getResultsDao().update(result);
    }

    /**
     * Завершение маршрута
     * @param routeId идентификатор маршрута
     */
    public void setRouteStatus(String routeId, String status) {
        List<RouteStatuses> routeStatuses = daoSession.getRouteStatusesDao().queryBuilder().list();

        long finishId = -1;

        for(RouteStatuses routeStatus : routeStatuses) {
            if(routeStatus.c_const.equals(status)) {
                finishId = routeStatus.id;
                break;
            }
        }

        // если маршрут не был ранее завершен
        if(!isRouteStatus(routeId, status)) {
            RouteHistory routeHistory = new RouteHistory();
            routeHistory.id = UUID.randomUUID().toString();
            routeHistory.c_notice = "";
            routeHistory.fn_status = finishId;
            routeHistory.fn_route = routeId;
            routeHistory.fn_user = Authorization.getInstance().getUser().getUserId();
            routeHistory.d_date = DateUtil.convertDateToString(new Date());
            routeHistory.objectOperationType = DbOperationType.CREATED;
            routeHistory.isSynchronization = false;

            getDaoSession().getRouteHistoryDao().insert(routeHistory);
        }
    }

    /**
     * Маршрут завершен или нет
     * @param routeId идентификатор маршрута
     * @return true - маршрут завершен
     */
    public boolean isRouteStatus(String routeId, String status) {
        List<RouteHistory> routeHistories = daoSession.getRouteHistoryDao().queryBuilder().where(RouteHistoryDao.Properties.Fn_route.eq(routeId)).orderDesc(RouteHistoryDao.Properties.D_date).list();
        List<RouteStatuses> routeStatuses = daoSession.getRouteStatusesDao().queryBuilder().list();

        long statusId = -1;

        for(RouteStatuses routeStatus : routeStatuses) {
            if(routeStatus.c_const.equals(status)) {
                statusId = routeStatus.id;
                break;
            }
        }

        boolean isStatus = false;

        for(RouteHistory routeHistory : routeHistories) {
            if(routeHistory.fn_status == statusId) {
                isStatus = true;
                break;
            }
        }

        return isStatus;
    }

    /**
     * Возвращение статуса маршрута
     * @param routeId идентификатор маршрута
     * @return true - разрешено ли отменить завершение.
     */
    public boolean isRevertRouteFinish(String routeId) {
        List<RouteHistory> routeHistories = daoSession.getRouteHistoryDao().queryBuilder().where(RouteHistoryDao.Properties.Fn_route.eq(routeId)).orderDesc(RouteHistoryDao.Properties.D_date).list();
        List<RouteStatuses> routeStatuses = daoSession.getRouteStatusesDao().queryBuilder().list();

        long finishId = -1;

        for(RouteStatuses routeStatus : routeStatuses) {
            if(routeStatus.c_const.equals("DONED")) {
                finishId = routeStatus.id;
                break;
            }
        }

        boolean revert = false;

        for(RouteHistory routeHistory : routeHistories) {
            if(routeHistory.fn_status == finishId && !routeHistory.isSynchronization) {
                revert = true;
                break;
            }
        }

        return revert;
    }

    /**
     * Отменить завершение маршрута
     * @param routeId иден. маршрута
     */
    public void revertRouteStatus(String routeId, String status) {
        if(isRevertRouteFinish(routeId)) {
            List<RouteHistory> routeHistories = daoSession.getRouteHistoryDao().queryBuilder().where(RouteHistoryDao.Properties.Fn_route.eq(routeId)).orderDesc(RouteHistoryDao.Properties.D_date).list();
            List<RouteStatuses> routeStatuses = daoSession.getRouteStatusesDao().queryBuilder().list();

            long finishId = -1;

            for(RouteStatuses routeStatus : routeStatuses) {
                if(routeStatus.c_const.equals(status)) {
                    finishId = routeStatus.id;
                    break;
                }
            }

            for(RouteHistory routeHistory : routeHistories) {
                if(routeHistory.fn_status == finishId && !routeHistory.isSynchronization) {
                    daoSession.getRouteHistoryDao().delete(routeHistory);
                    break;
                }
            }
        }
    }

    /**
     * Список вопросов
     * @return получить список вопросов
     */
    public Question[] getQuestions() {
        List<Question> questions = daoSession.getQuestionDao().queryBuilder().orderAsc(QuestionDao.Properties.N_order).list();
        return questions.toArray(new Question[0]);
    }

    /**
     * Список вопросов для кандидата
     * @param priority приоритет
     * @return получить список вопросов
     */
    public Question[] getQuestions(int priority) {
        String role = Authorization.getInstance().getUser().isCandidateOne() ? BasicUser.CANDIDATE_ONE : BasicUser.CANDIDATE_LIST;

        List<Question> questions = daoSession.getQuestionDao().queryBuilder().where(QuestionDao.Properties.N_priority.eq(priority), QuestionDao.Properties.C_role.eq(role)).orderAsc(QuestionDao.Properties.N_order).list();
        return questions.toArray(new Question[0]);
    }

    /**
     * вопрос
     * @return получить вопрос
     */
    public Question getQuestion(long id) {
        return daoSession.getQuestionDao().load(id);
    }

    /**
     * список ответов
     * @param question вопрос
     * @return получение списка ответов по вопросу
     */
    public Answer[] getAnswers(long question) {
        List<Answer> answers = daoSession.getAnswerDao().queryBuilder().where(AnswerDao.Properties.F_question.eq(question)).orderAsc(AnswerDao.Properties.N_order).list();
        return answers.toArray(new Answer[0]);
    }

    /**
     * Удаление всех созданных пользовательских точек и результатов
     */
    public void removeVoteResult(String pointID) {
        // найти пользоват. точку
        List<UserPoints> userPoints = getDaoSession().getUserPointsDao().queryBuilder().where(UserPointsDao.Properties.Fn_point.eq(pointID)).list();
        if(userPoints.size() > 0) {
            // удалить результаты
            for (UserPoints point : userPoints) {
                getDaoSession().getUserPointsDao().delete(point);

                List<Results> results = getDaoSession().getResultsDao().queryBuilder().where(ResultsDao.Properties.Fn_user_point.eq(point.id)).list();

                for (Results result : results) {
                    getDaoSession().getResultsDao().delete(result);
                }
            }
        }
    }

    public ProfileItem getProfile() {
        Users user = daoSession.getUsersDao().load(Authorization.getInstance().getUser().getUserId());
        if(user != null) {
            ProfileItem item = new ProfileItem();
            item.fio = StringUtil.isEmptyOrNull(user.getC_fio()) ? user.getC_login() : user.getC_fio();
            item.uik = user.n_uik;
            item.subDivision = user.getF_subdivision();
            return item;
        }
        return null;
    }

    /**
     * добавление вопроса
     * @param context контекст
     * @param type тип вопроса
     * @param message сообщение пользователя
     * @param data данные
     */
    public void saveFeedback(Context context, long type, String message, String data) {
        Feedbacks feedback = new Feedbacks();
        feedback.id = UUID.randomUUID().toString();
        feedback.c_imei = HardwareUtil.getIMEI(context);
        feedback.c_question = message;
        feedback.fn_type = type;
        feedback.d_date_question = DateUtil.convertDateToString(new Date());
        feedback.fn_user = Authorization.getInstance().getUser().getUserId();
        feedback.jb_data = data;
        feedback.objectOperationType = DbOperationType.CREATED;

        daoSession.getFeedbacksDao().insert(feedback);
    }

    /**
     * Фильтрация маршрутов
     */
    public enum RouteFilter {
        /**
         * Все маршруты
         */
        ALL,
        /**
         * Все активные на текущий момент
         */
        ACTIVE,
        /**
         * Прошедшие
         */
        PAST,
        /**
         * Будущие
         */
        FUTURE
    }
}
