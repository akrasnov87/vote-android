package ru.mobnius.vote.utils;

import org.greenrobot.greendao.database.Database;

import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.data.storage.FieldNames;
import ru.mobnius.vote.data.storage.models.DaoSession;

public class SyncUtil {
    /**
     * объект для сброса параметров транзакции
     * @return
     */
    public static Object[] getResetTidParams(){
        Object[] params = new Object[2];
        params[0] = null;
        params[1] = null;
        return params;
    }

    /**
     * запрос на сброса транзакции
     * @param tableName имя таблицы
     * @return sql-запрос
     */
    public static String getResetTidSqlQuery(String tableName){
        return "update " + tableName + " set "+ FieldNames.TID +" = ?, "+ FieldNames.BLOCK_TID +" = ?";
    }

    /**
     * сбрасывает идентификаторы транзакций у таблиц по которым разрешено отправлять данные
     * @param context объект синхронизации
     * @return false - информация не была сброшена
     */
    public static boolean resetTid(ru.mobnius.vote.data.manager.synchronization.ISynchronization context){
        boolean result;
        DaoSession daoSession = context.getDaoSession();
        Database db = daoSession.getDatabase();
        db.beginTransaction();
        try {
            Object[] params = getResetTidParams();
            for(ru.mobnius.vote.data.manager.synchronization.Entity entity : context.getEntityToList()) {
                db.execSQL(getResetTidSqlQuery(entity.tableName), params);
            }
            db.setTransactionSuccessful();
            result = true;
        }catch (Exception e){
            result = false;
            Logger.error(e);
            context.onError(ru.mobnius.vote.data.manager.synchronization.IProgressStep.NONE, e, null);
        }finally {
            db.endTransaction();
        }

        return result;
    }

    /**
     * обновление идентификатора транзакции для записи
     * @param context объект синхронизации
     * @param tableName имя таблицы
     * @param tid идентификатор транзакции
     * @return возвращается результат обработки
     */
    public static boolean updateTid(ru.mobnius.vote.data.manager.synchronization.ISynchronization context, String tableName, String tid){
        boolean result = false;
        DaoSession daoSession = context.getDaoSession();
        Database db = daoSession.getDatabase();
        try {
            Object[] params = new Object[2];
            params[0] = tid;
            params[1] = "";
            db.execSQL("update " + tableName + " set "+FieldNames.TID+" = ? where "+FieldNames.TID+" is null OR "+FieldNames.TID+" = ?", params);
            result = true;
        }catch (Exception e){
            Logger.error(e);
            context.onError(ru.mobnius.vote.data.manager.synchronization.IProgressStep.START, e, tid);
        }
        return result;
    }

    /**
     * обновление идентификатора блока для записи
     * @param context объект синхронизации
     * @param tableName имя таблицы
     * @param tid идентификатор транзакции
     * @param blockTid идентификатор транзакции
     * @param operationType тип операции
     * @return возвращается результат обработки
     */
    public static void updateBlockTid(ru.mobnius.vote.data.manager.synchronization.ISynchronization context, String tableName, String tid, String blockTid, String operationType){
        DaoSession daoSession = context.getDaoSession();
        Database db = daoSession.getDatabase();
        try {
            Object[] params = new Object[3];
            params[0] = blockTid;
            params[1] = tid;
            params[2] = operationType;

            db.execSQL("update " + tableName + " set "+ FieldNames.BLOCK_TID+" = ? where "+FieldNames.TID+" = ? AND " + FieldNames.OBJECT_OPERATION_TYPE + " = ?", params);
        }catch (Exception e) {
            Logger.error(e);
            context.onError(ru.mobnius.vote.data.manager.synchronization.IProgressStep.START, e, tid);
        }
    }

    /**
     * обновление идентификатора транзакции во всех связанных таблицах
     * @param context объект синхронизации
     * @param tid идентификатор транзакции
     * @return возвращается результат обработки
     */
    public static boolean updateTid(ru.mobnius.vote.data.manager.synchronization.ISynchronization context, String tid){
        boolean result;
        DaoSession daoSession = context.getDaoSession();
        Database db = daoSession.getDatabase();
        db.beginTransaction();
        try {
            Object[] params = new Object[2];
            params[0] = tid;
            params[1] = "";
            for(ru.mobnius.vote.data.manager.synchronization.Entity entity : context.getEntityToList()) {
                db.execSQL("update " + entity.tableName + " set "+FieldNames.TID+" = ? where "+FieldNames.TID+" is null OR "+FieldNames.TID+" = ?", params);
            }
            db.setTransactionSuccessful();
            result = true;
        }catch (Exception e){
            result = false;
            Logger.error(e);
        }finally {
            db.endTransaction();
        }
        return result;
    }
}