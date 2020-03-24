package ru.mobnius.vote.data.manager;

import android.location.Location;
import android.service.autofill.LuhnChecksumValidator;

import com.google.gson.internal.bind.JsonTreeReader;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.storage.models.AttachmentTypes;
import ru.mobnius.vote.data.storage.models.AttachmentTypesDao;
import ru.mobnius.vote.data.storage.models.Attachments;
import ru.mobnius.vote.data.storage.models.AttachmentsDao;
import ru.mobnius.vote.data.storage.models.DaoSession;
import ru.mobnius.vote.data.storage.models.Files;
import ru.mobnius.vote.data.storage.models.InputMeterReadings;
import ru.mobnius.vote.data.storage.models.InputMeterReadingsDao;
import ru.mobnius.vote.data.storage.models.Points;
import ru.mobnius.vote.data.storage.models.PointsDao;
import ru.mobnius.vote.data.storage.models.RegistrPts;
import ru.mobnius.vote.data.storage.models.ResultTypes;
import ru.mobnius.vote.data.storage.models.Results;
import ru.mobnius.vote.data.storage.models.ResultsDao;
import ru.mobnius.vote.data.storage.models.RouteHistory;
import ru.mobnius.vote.data.storage.models.RouteHistoryDao;
import ru.mobnius.vote.data.storage.models.RouteStatuses;
import ru.mobnius.vote.data.storage.models.RouteTypes;
import ru.mobnius.vote.data.storage.models.Routes;
import ru.mobnius.vote.data.storage.models.UserPoints;
import ru.mobnius.vote.data.storage.models.UserPointsDao;
import ru.mobnius.vote.ui.model.Image;
import ru.mobnius.vote.ui.model.PointFilter;
import ru.mobnius.vote.ui.model.PointInfo;
import ru.mobnius.vote.ui.model.PointItem;
import ru.mobnius.vote.ui.model.PointResult;
import ru.mobnius.vote.ui.model.PointState;
import ru.mobnius.vote.ui.model.RouteInfo;
import ru.mobnius.vote.ui.model.RouteInfoHistory;
import ru.mobnius.vote.ui.model.RouteItem;
import ru.mobnius.vote.utils.DateUtil;
import ru.mobnius.vote.utils.StringUtil;

/**
 * вспомогательные функции для получения данных от БД Sqlite
 */
public class DataManager {
    private DaoSession daoSession;

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
     * Получение байтов для файла
     *
     * @param fileId идентификтаор объекта
     * @return массив байтов
     * @throws IOException
     */
    public byte[] getFileBytes(String fileId) throws IOException {
        Files files = getFile(fileId);
        if (files != null) {
            return FileManager.getInstance().readPath(files.folder, files.c_name);
        } else {
            return null;
        }
    }

    /**
     * Получение данных о файле
     * @param fileId идентификтаор
     * @return данные
     */
    public Files getFile(String fileId) {
        return daoSession.getFilesDao().load(fileId);
    }

    /**
     * Удаление файла по идентификтаору
     *
     * @param fileId идентификтаор
     * @return true - файл был удален
     */
    public boolean removeFile(String fileId) {
        Files files = getFile(fileId);
        if (files != null) {
            if(files.isSynchronization) {
                files.objectOperationType = DbOperationType.REMOVED;
                files.isDelete = true;
                files.isSynchronization = false;

                daoSession.getFilesDao().update(files);
            }else{
                daoSession.getFilesDao().delete(files);
            }
            FileManager fileManager = FileManager.getInstance();
            try {
                fileManager.deleteFile(files.folder, files.c_name);
            }catch (FileNotFoundException ignore){
                // это нормально
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Сохранение файла
     *
     * @param c_name имя файла
     * @param bytes  массив байтов
     * @return файл
     * @throws IOException
     */
    public Files saveFile(String c_name, byte[] bytes) throws IOException {
        return saveFile(c_name, bytes, FileManager.FILES);
    }

    /**
     * Сохранение файла
     *
     * @param c_name имя файла
     * @param bytes  массив байтов
     * @param folder каталог
     * @return файл
     * @throws IOException
     */
    public Files saveFile(String c_name, byte[] bytes, String folder) throws IOException {
        FileManager fileManager = FileManager.getInstance();
        fileManager.writeBytes(folder, c_name, bytes);

        Files file = new Files();
        file.c_name = c_name;
        file.c_mime = StringUtil.getMimeByName(c_name);
        file.c_extension = StringUtil.getExtension(c_name);
        file.d_date = DateUtil.convertDateToString(new Date());
        file.folder = folder;
        file.objectOperationType = DbOperationType.CREATED;

        daoSession.getFilesDao().insert(file);
        return file;
    }

    /**
     * Получение вложения
     * @param attachmentId идентификатор
     * @return результат
     */
    public Attachments getAttachment(String attachmentId) {
        return daoSession.getAttachmentsDao().load(attachmentId);
    }

    /**
     * сохранение файлов
     * @param c_name иям файла
     * @param fn_type тип файла, информация из таблицы cs_file_types
     * @param fn_result результат выполнения задания
     * @param notice примечание
     * @param location местоположение
     * @throws IOException исключение при работе с файловым менеджером
     */
    public Attachments saveAttachment(String c_name, long fn_type, String fn_result, String notice, Location location, byte[] bytes) throws IOException {
        Files file = saveFile(c_name, bytes, FileManager.ATTACHMENTS);

        Attachments attachment = new Attachments();
        attachment.c_name = c_name;
        attachment.fn_type = fn_type;
        attachment.fn_result = fn_result;
        attachment.n_latitude = location.getLatitude();
        attachment.n_longitude = location.getLongitude();
        attachment.d_date = DateUtil.convertDateToString(new Date(location.getTime()));
        attachment.folder = FileManager.ATTACHMENTS;
        attachment.fn_file = file.id;
        attachment.c_notice = notice;
        attachment.objectOperationType = DbOperationType.CREATED;

        daoSession.getAttachmentsDao().insert(attachment);
        return attachment;
    }

    /**
     * обновление существующего файла
     * @param attachmentId идентификатор файла
     * @param type тип
     * @param notice описание
     */
    public Attachments updateAttachment(String attachmentId, long type, String notice) {
        Attachments attachment = daoSession.getAttachmentsDao().load(attachmentId);
        if(attachment != null){
            attachment.fn_type = type;
            attachment.c_notice = notice;
            if(!attachment.objectOperationType.equals(DbOperationType.CREATED) && !attachment.isSynchronization) {
                attachment.objectOperationType = DbOperationType.UPDATED;
                attachment.isSynchronization = false;
            }

            daoSession.getAttachmentsDao().update(attachment);
            return attachment;
        }

        return null;
    }

    /**
     * Можно ли обновлять вложение
     * @param attachmentId иден. вложения
     * @return true - обновление разрешены
     */
    public boolean isUpdateAttachment(String attachmentId) {
        Attachments attachment = daoSession.getAttachmentsDao().load(attachmentId);
        if(attachment != null) {
            return !attachment.isSynchronization;
        }

        return false;
    }

    /**
     * Удаление вложения
     * @param attachmentId идентификатор
     */
    public void removeAttachment(String attachmentId) {
        Attachments attachments = getAttachment(attachmentId);
        if(attachments != null) {
            removeFile(attachments.fn_file);
            daoSession.getAttachmentsDao().delete(attachments);
        }
    }

    /**
     * Получение байтов для вложений
     *
     * @param attachmentId идентификтаор объекта
     * @return массив байтов
     * @throws IOException
     */
    public byte[] getFileAttachment(String attachmentId) throws IOException {
        Attachments attachments = getAttachment(attachmentId);
        if (attachments != null) {
            return FileManager.getInstance().readPath(attachments.folder, attachments.c_name);
        } else {
            return null;
        }
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
        List<Routes> routes = daoSession.getRoutesDao().loadAll();
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

                PointState pointState = getPointState(point.id);
                if(pointState != null) {
                    pointItem.done = pointState.isDone();
                    pointItem.sync = pointState.isSync();
                }

                RegistrPts registrPts = point.getRegistrPts();
                if(registrPts != null) {
                    pointItem.address = registrPts.c_address;
                    pointItem.deviceNumber = registrPts.c_device;
                    pointItem.fio = registrPts.c_fio;
                    pointItem.info = point.c_info;
                    pointItem.notice = point.c_notice;
                    pointItem.subscrNumber = registrPts.c_subscr;
                    pointItem.routeId = point.f_route;
                }

                Routes route = point.getRoute();
                if(route != null) {
                    pointItem.routeName = route.c_notice;
                    pointItem.routeTypeName = route.getType().c_name;
                }

                pointItems.add(pointItem);
            }

            return pointItems;
        }

        return null;
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
            if(userPoint.isSynchronization) {
                List<Results> results = daoSession.getResultsDao().queryBuilder().where(ResultsDao.Properties.Fn_user_point.eq(userPoint.id)).list();
                for(Results result : results) {
                    if(result.isSynchronization) {
                        List<Attachments> attachments = daoSession.getAttachmentsDao().queryBuilder().where(AttachmentsDao.Properties.Fn_result.eq(result.id)).list();
                        for(Attachments attachment : attachments) {
                            if(attachment.getFile() != null) {
                                if (!attachment.isSynchronization || !attachment.getFile().isSynchronization) {
                                    return pointState;
                                }
                            }
                        }
                    } else {
                        return pointState;
                    }
                }
            } else {
                return pointState;
            }
        }

        if(userPoints.size() > 0) {
            pointState.setSync(true);
        }
        return pointState;
    }

    /**
     * Получение списка доступных документов при работе с точкой маршрута
     * @param pointId идентификатор точки маршрута
     * @return список документов
     */
    public List<PointResult> getPointDocuments(String pointId) {
        List<ResultTypes> resultTypes = daoSession.getResultTypesDao().loadAll();
        List<Results> resultsList = daoSession.getResultsDao().queryBuilder().where(ResultsDao.Properties.Fn_point.eq(pointId)).list();
        List<PointResult> pointResults = new ArrayList<>();

        for(ResultTypes resultType : resultTypes) {
            boolean exists = false;
            for(Results result : resultsList){
                if(result.fn_type == resultType.id) {
                    pointResults.add(PointResult.getInstance(resultType.id, resultType.c_name, resultType.n_order, result.id, false));
                    exists = true;
                    break; // создание двух одинаковых актов не возможно
                }
            }

            if(!exists) {
                pointResults.add(PointResult.getInstance(resultType.id, resultType.c_name, resultType.n_order, null, false));
            }
        }

        Collections.sort(pointResults, new Comparator<PointResult>() {
            @Override
            public int compare(PointResult o1, PointResult o2) {
                return Integer.compare(o2.getOrder(), o1.getOrder());
            }
        });

        return pointResults;
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
                routeInfo.setNotice(route.c_notice);
                routeInfo.setDateEnd(DateUtil.convertStringToDate(route.d_date_end));
                routeInfo.setDateStart(DateUtil.convertStringToDate(route.d_date_start));
                routeInfo.setExtended(route.b_extended);
                if(!StringUtil.isEmptyOrNull(route.d_extended)) {
                    routeInfo.setDateExtended(DateUtil.convertStringToDate(route.d_extended));
                }

                List<RouteHistory> histories = daoSession.getRouteHistoryDao().queryBuilder().where(RouteHistoryDao.Properties.Fn_route.eq(routeId)).list();
                for(RouteHistory routeHistory : histories) {
                    RouteStatuses routeStatuse = routeHistory.getStatus();
                    if(routeStatuse != null) {
                        routeInfo.addHistory(routeStatuse.id, routeStatuse.c_name, DateUtil.convertStringToDate(routeHistory.d_date), routeHistory.c_notice);
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
            RegistrPts registrPts = point.getRegistrPts();
            if(registrPts != null) {
                PointInfo info = new PointInfo(registrPts);
                List<InputMeterReadings> inputMeterReadings = daoSession.getInputMeterReadingsDao().queryBuilder().where(InputMeterReadingsDao.Properties.F_point.eq(pointId)).list();
                for (InputMeterReadings inputMeterReading:
                        inputMeterReadings) {
                    info.addMeter(inputMeterReading);
                }

                return info;
            }
        }
        return null;
    }

    /**
     * Получение списка изображений привязанных к результату
     * @param resultId иден. результата, Results
     * @return список изображений, Image[]
     */
    public Image[] getImages(String resultId) {
        List<Attachments> attachments = daoSession.getAttachmentsDao().queryBuilder().where(AttachmentsDao.Properties.Fn_result.eq(resultId)).list();
        if(attachments.size() > 0) {
            List<Image> images = new ArrayList<>();
            for (Attachments attachment : attachments) {
                try {
                    images.add(Image.getInstance(this, attachment));
                } catch (ParseException e) {
                    Logger.error(e);
                } catch (IOException e) {
                    Logger.error(e);
                }
            }
            return images.toArray(new Image[0]);
        }

        return null;
    }

    /**
     * Тип изображения по умолчанию
     * @return тип изображения
     */
    public AttachmentTypes getDefaultImageType() {
        DataManager dataManager = DataManager.getInstance();
        List<AttachmentTypes> attachmentTypes = dataManager.getDaoSession().getAttachmentTypesDao().queryBuilder()
                .where(AttachmentTypesDao.Properties.B_default.eq(true)).orderAsc(AttachmentTypesDao.Properties.N_order)
                .list();
        if(attachmentTypes.size() > 0) {
            return attachmentTypes.get(0);
        }
        return dataManager.getDaoSession().getAttachmentTypesDao().loadAll().get(0);
    }

    /**
     * Тип изображения
     * @return типы изображений
     */
    public AttachmentTypes[] getAttachmentTypes() {
        DataManager dataManager = DataManager.getInstance();
        List<AttachmentTypes> attachmentTypes = dataManager.getDaoSession().getAttachmentTypesDao().queryBuilder().orderAsc(AttachmentTypesDao.Properties.N_order).list();
        return attachmentTypes.toArray(new AttachmentTypes[0]);
    }

    /**
     * Тип изображения
     * @param id идентификатор
     * @return тип изображения
     */
    public AttachmentTypes getImageType(long id) {
        return dataManager.getDaoSession().getAttachmentTypesDao().load(id);
    }

    /**
     * Завершение маршрута
     * @param routeId идентификатор маршрута
     */
    public void setRouteFinish(String routeId) {
        List<RouteStatuses> routeStatuses = daoSession.getRouteStatusesDao().queryBuilder().list();

        long finishId = -1;

        for(RouteStatuses routeStatus : routeStatuses) {
            if(routeStatus.c_const.equals("DONED")) {
                finishId = routeStatus.id;
                break;
            }
        }

        // если маршрут не был ранее завершен
        if(!isRouteFinish(routeId)) {
            RouteHistory routeHistory = new RouteHistory();
            routeHistory.id = UUID.randomUUID().toString();
            routeHistory.c_notice = "Принудительно завершено пользователем.";
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
    public boolean isRouteFinish(String routeId) {
        List<RouteHistory> routeHistories = daoSession.getRouteHistoryDao().queryBuilder().where(RouteHistoryDao.Properties.Fn_route.eq(routeId)).orderDesc(RouteHistoryDao.Properties.D_date).list();
        List<RouteStatuses> routeStatuses = daoSession.getRouteStatusesDao().queryBuilder().list();

        long finishId = -1;

        for(RouteStatuses routeStatus : routeStatuses) {
            if(routeStatus.c_const.equals("DONED")) {
                finishId = routeStatus.id;
                break;
            }
        }

        boolean isFinished = false;

        for(RouteHistory routeHistory : routeHistories) {
            if(routeHistory.fn_status == finishId) {
                isFinished = true;
                break;
            }
        }

        return isFinished;
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
    public void revertRouteFinish(String routeId) {
        if(isRevertRouteFinish(routeId)) {
            List<RouteHistory> routeHistories = daoSession.getRouteHistoryDao().queryBuilder().where(RouteHistoryDao.Properties.Fn_route.eq(routeId)).orderDesc(RouteHistoryDao.Properties.D_date).list();
            List<RouteStatuses> routeStatuses = daoSession.getRouteStatusesDao().queryBuilder().list();

            long finishId = -1;

            for(RouteStatuses routeStatus : routeStatuses) {
                if(routeStatus.c_const.equals("DONED")) {
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

    public boolean isWait(String routeId) {
        Routes routes = daoSession.getRoutesDao().load(routeId);
        try {
            Date dateStart = DateUtil.convertStringToDate(routes.d_date_start);
            Date dateEnd = DateUtil.convertStringToDate(routes.d_date_end);
            Date date = new Date();
            return dateStart.getTime() < date.getTime() &&
                    dateEnd.getTime() > date.getTime();
        } catch (ParseException e) {
            return false;
        }
    }

    public void destroy() {
        dataManager = null;
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
