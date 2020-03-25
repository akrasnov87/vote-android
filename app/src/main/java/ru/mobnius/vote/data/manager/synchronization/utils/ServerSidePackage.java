package ru.mobnius.vote.data.manager.synchronization.utils;

import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;

import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.data.manager.FileManager;
import ru.mobnius.vote.data.manager.packager.FileBinary;
import ru.mobnius.vote.data.manager.rpc.RPCResult;
import ru.mobnius.vote.data.storage.FieldNames;
import ru.mobnius.vote.data.storage.models.DaoSession;
import ru.mobnius.vote.utils.SqlInsertFromJSONObject;
import ru.mobnius.vote.utils.SqlUpdateFromJSONObject;

/**
 * абстрактный обработчик результат от сервера
 */
public abstract class ServerSidePackage implements IServerSidePackage {
    private boolean deleteRecordBeforeAppend = false;
    private FileBinary[] mFileBinary;

    /**
     * Устанавливает параметр удаление записей при добавлении информации в БД
     * @param value значение
     */
    public void setDeleteRecordBeforeAppend(boolean value){
        deleteRecordBeforeAppend = value;
    }

    /**
     * Удалять записи при добавлении информации в БД
     * @return true - все записи из таблицы будут удалены
     */
    public boolean getDeleteRecordBeforeAppend(){
        return deleteRecordBeforeAppend;
    }

    /**
     * Добавление бинарного блока
     * @param fileBinary бинарный блок
     */
    public void setFileBinary(FileBinary[] fileBinary){
        this.mFileBinary = fileBinary;
    }

    /**
     * обработка блока to
     * результат обработки информации переданнйо в блоке to
     * @param session сессия
     * @param rpcResult результат RPC
     * @param packageTid идентификатор пакета
     * @return результат
     */
    public PackageResult to(DaoSession session, RPCResult rpcResult, String packageTid) {

        Database db = session.getDatabase();
        // если все хорошо обновляем запись в локальной БД
        String sqlQuery = "";
        Object[] values;
        try {
            // TODO: тут нужно переделать
            if(!rpcResult.meta.success){
                sqlQuery = "UPDATE " + rpcResult.action + " set " + FieldNames.IS_SYNCHRONIZATION + " = ?, "+FieldNames.TID+" = ?, "+ FieldNames.BLOCK_TID+" = ? where " + FieldNames.TID + " = ? and " + FieldNames.BLOCK_TID + " = ?;";
                values = new Object[5];
                values[0] = false;
                values[1] = null;
                values[2] = null;
                values[3] = packageTid;
                values[4] = String.valueOf(rpcResult.tid);

                db.execSQL(sqlQuery, values);

                return PackageResult.fail("Ошибка обработки блока на сервере. " + rpcResult.meta.msg, null);
            }else {
                sqlQuery = "UPDATE " + rpcResult.action + " set " + FieldNames.IS_SYNCHRONIZATION + " = ?, "+FieldNames.OBJECT_OPERATION_TYPE+" = ?, "+FieldNames.TID+" = ?, "+ FieldNames.BLOCK_TID+" = ? where " + FieldNames.TID + " = ? and " + FieldNames.BLOCK_TID + " = ?;";
                values = new Object[6];
                values[0] = true;
                values[1] = null;
                values[2] = null;
                values[3] = null;
                values[4] = packageTid;
                values[5] = String.valueOf(rpcResult.tid);

                db.execSQL(sqlQuery, values);

                return PackageResult.success(null);
            }
        } catch (Exception e) {
            return PackageResult.fail("Ошибка обновление положительного результат в БД. Запрос: " + sqlQuery, e);
        }
    }

    /**
     * обработка блока from
     * результат обработки информации переданной в блоке from
     * @param session сессия
     * @param rpcResult результат RPC
     * @param packageTid идентификатор пакета
     * @param isRequestToServer может ли объект делать запрос на сервер.
     * @return результат
     */
    public PackageResult from(DaoSession session, RPCResult rpcResult, String packageTid, boolean isRequestToServer) {
        return from(session, rpcResult, packageTid, isRequestToServer, false);
    }

    /**
     * обработка блока from
     * результат обработки информации переданной в блоке from
     * @param session сессия
     * @param rpcResult результат RPC
     * @param packageTid идентификатор пакета
     * @param isRequestToServer может ли объект делать запрос на сервер.
     * @param attachmentUse применяется обработка вложений
     * @return результат
     */
    public PackageResult from(DaoSession session, RPCResult rpcResult, String packageTid, boolean isRequestToServer, boolean attachmentUse) {
        if (rpcResult.meta.success) {
            Database db = session.getDatabase();
            AbstractDao abstractDao = null;
            String tableName = rpcResult.action;
            for (AbstractDao ad : session.getAllDaos()) {
                if (ad.getTablename().equals(tableName)) {
                    abstractDao = ad;
                    break;
                }
            }

            if (abstractDao == null) {
                return PackageResult.fail("Имя сущности не найдено в локальной БД. " + tableName + ".", new NullPointerException("AbstractDao not found."));
            }

            PackageResult packageResult;

            if(!rpcResult.method.equals("Query") && !rpcResult.method.equals("Select")){
                return  PackageResult.fail("Метод результата " + tableName + " должен быть Query. Текущее значение " + rpcResult.method, null);
            }
            db.beginTransaction();

            if (getDeleteRecordBeforeAppend()) {
                db.execSQL("delete from " + tableName);
                // таким образом очищаем кэш http://greenrobot.org/greendao/documentation/sessions/
                abstractDao.detachAll();
            }

            if (rpcResult.result.records.length > 0) {
                JSONObject firstObject = rpcResult.result.records[0];
                if(firstObject.isNull("__error")) {
                    SqlInsertFromJSONObject sqlInsert = new SqlInsertFromJSONObject(firstObject, tableName, abstractDao);
                    try {
                        for (JSONObject object : rpcResult.result.records) {
                            try {
                                db.execSQL(sqlInsert.convertToQuery(isRequestToServer), sqlInsert.getValues(object, isRequestToServer));
                            } catch (SQLiteConstraintException ignored) {
                                Log.e("SYNC_ERROR", ignored.getMessage());
                                // тут нужно обновить запись
                                String pkColumnName = "";
                                for (AbstractDao a : session.getAllDaos()) {
                                    if (a.getTablename().equals(tableName)) {
                                        pkColumnName = a.getPkProperty().columnName;
                                        break;
                                    }
                                }
                                if (pkColumnName.isEmpty()) {
                                    throw new Exception("Колонка для первичного ключа, таблицы " + tableName + " не найден.");
                                } else {
                                    // тут обновление будет только у тех записей у которых не было изменений.
                                    SqlUpdateFromJSONObject sqlUpdate = new SqlUpdateFromJSONObject(firstObject, tableName, pkColumnName);
                                    db.execSQL(sqlUpdate.convertToQuery(isRequestToServer), sqlUpdate.getValues(object, isRequestToServer));
                                }
                            }
                        }
                        db.setTransactionSuccessful();
                        packageResult = PackageResult.success(null);
                    } catch (Exception e) {
                        packageResult = PackageResult.fail("Ошибка вставки записей в таблицу " + tableName + ".", e);
                    } finally {
                        db.endTransaction();
                    }
                }else{
                    String error;
                    try{
                        error = firstObject.getString("__error");
                    }catch (JSONException e){
                        error = e.getMessage();
                    }
                    packageResult = PackageResult.fail("Ошибка вставки записей в таблицу " + tableName + ".", new Exception(error));
                }
                return packageResult;
            }
            return PackageResult.success(null);
        } else {
            return PackageResult.fail("Ошибка обработки блока на сервере. " + rpcResult.meta.msg, null);
        }
    }

    /**
     * Получение файла по имени
     * @param name имя
     * @return возарщается файл
     */
    protected FileBinary getFile(String name) {
        if(mFileBinary != null) {
            for (FileBinary file : mFileBinary) {
                if (file.name.equals(name)) {
                    return file;
                }
            }
        }
        return null;
    }
}
