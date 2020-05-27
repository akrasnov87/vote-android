package ru.mobnius.vote.data.manager.synchronization;

import android.annotation.SuppressLint;
import android.app.Activity;

import java.io.IOException;
import java.util.UUID;

import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.rpc.FilterItem;
import ru.mobnius.vote.data.manager.rpc.RPCItem;
import ru.mobnius.vote.data.manager.rpc.RPCResult;
import ru.mobnius.vote.data.manager.synchronization.meta.TableQuery;
import ru.mobnius.vote.data.manager.synchronization.utils.FullServerSidePackage;
import ru.mobnius.vote.data.manager.synchronization.utils.PackageResult;
import ru.mobnius.vote.data.storage.models.AnswerDao;
import ru.mobnius.vote.data.storage.models.DaoSession;
import ru.mobnius.vote.data.storage.models.DivisionsDao;
import ru.mobnius.vote.data.storage.models.PointTypesDao;
import ru.mobnius.vote.data.storage.models.PointsDao;
import ru.mobnius.vote.data.storage.models.QuestionDao;
import ru.mobnius.vote.data.storage.models.RegistrPtsDao;
import ru.mobnius.vote.data.storage.models.ResultTypesDao;
import ru.mobnius.vote.data.storage.models.ResultsDao;
import ru.mobnius.vote.data.storage.models.RolesDao;
import ru.mobnius.vote.data.storage.models.RouteHistoryDao;
import ru.mobnius.vote.data.storage.models.RouteStatusesDao;
import ru.mobnius.vote.data.storage.models.RouteTypesDao;
import ru.mobnius.vote.data.storage.models.RoutesDao;
import ru.mobnius.vote.data.storage.models.StatusSchemasDao;
import ru.mobnius.vote.data.storage.models.SubDivisionsDao;
import ru.mobnius.vote.data.storage.models.UserInDivisionsDao;
import ru.mobnius.vote.data.storage.models.UserInRoutesDao;
import ru.mobnius.vote.data.storage.models.UserPointsDao;
import ru.mobnius.vote.data.storage.models.UsersDao;
import ru.mobnius.vote.utils.PackageCreateUtils;
import ru.mobnius.vote.utils.PackageReadUtils;

/**
 * Ручная синхронизация данных
 */
public class ManualSynchronization extends WebSocketSynchronization {

    @SuppressLint("StaticFieldLeak")
    private static ManualSynchronization manualSynchronization;

    public static ManualSynchronization getInstance(boolean zip){
        if(manualSynchronization != null){
            return manualSynchronization;
        }else{
            return manualSynchronization = new ManualSynchronization(DataManager.getInstance().getDaoSession(), zip);
        }
    }

    private String totalTid;
    public String dictionaryTid;

    /**
     * конструктор
     * @param session сессия для подключения к БД
     */
    ManualSynchronization(DaoSession session, boolean zip) {
        super(session, "MANUAL_SYNCHRONIZATION", zip);
        oneOnlyMode = true;
        serverSidePackage = new FullServerSidePackage();
    }

    @Override
    protected void initEntities() {

        totalTid = UUID.randomUUID().toString();
        dictionaryTid = UUID.randomUUID().toString();

        addEntity(new EntityDictionary(DivisionsDao.TABLENAME, false, true).setTid(dictionaryTid));
        addEntity(new EntityDictionary(SubDivisionsDao.TABLENAME, false, true).setTid(dictionaryTid));
        addEntity(new EntityDictionary(RouteStatusesDao.TABLENAME, false, true).setTid(dictionaryTid));
        addEntity(new EntityDictionary(PointTypesDao.TABLENAME, false, true).setTid(dictionaryTid));
        addEntity(new EntityDictionary(ResultTypesDao.TABLENAME, false, true).setTid(dictionaryTid));
        addEntity(new EntityDictionary(RouteTypesDao.TABLENAME, false, true).setTid(dictionaryTid));
        addEntity(new EntityDictionary(StatusSchemasDao.TABLENAME, false, true).setTid(dictionaryTid));
        addEntity(new EntityDictionary(AnswerDao.TABLENAME, false, true).setTid(dictionaryTid));
        addEntity(new EntityDictionary(QuestionDao.TABLENAME, false, true).setTid(dictionaryTid));
        addEntity(new EntityDictionary(RolesDao.TABLENAME, false, true).setTid(dictionaryTid));
        addEntity(new EntityDictionary(UserInDivisionsDao.TABLENAME, false, true).setTid(dictionaryTid).setFilter(new FilterItem(UserInDivisionsDao.Properties.F_user.name, getUserID())));

        addEntity(Entity.createInstance(RoutesDao.TABLENAME, false, true).setTid(totalTid).setParam(getUserID()).setUseCFunction());
        addEntity(Entity.createInstance(UserPointsDao.TABLENAME, true, true).setTid(totalTid).setParam(getUserID()).setUseCFunction());
        addEntity(Entity.createInstance(PointsDao.TABLENAME, false, true).setTid(totalTid).setParam(getUserID()).setUseCFunction());
        addEntity(Entity.createInstance(ResultsDao.TABLENAME, true, true).setTid(totalTid).setParam(getUserID()).setUseCFunction());
        addEntity(Entity.createInstance(UserInRoutesDao.TABLENAME, false, true).setTid(totalTid).setParam(getUserID()).setUseCFunction());
        addEntity(Entity.createInstance(RouteHistoryDao.TABLENAME, true, true).setTid(totalTid).setParam(getUserID()).setUseCFunction());
        addEntity(Entity.createInstance(UsersDao.TABLENAME, true, true).setTid(totalTid).setParam(getUserID()).setUseCFunction());
        addEntity(Entity.createInstance(RegistrPtsDao.TABLENAME, false, true).setTid(totalTid).setParam(getUserID()).setUseCFunction());
    }

    @Override
    protected byte[] generatePackage(String tid, Object... args) throws IOException {
        PackageCreateUtils utils = new PackageCreateUtils(isZip());
        for(Entity entity : getEntities()){
            // обработка только элемента с указанным ключом
            if(entity.tid.equals(tid)) {
                if (entity.from) {
                    TableQuery tableQuery = new TableQuery(entity.tableName, entity.select);
                    RPCItem rpcItem;
                    if(entity.useCFunction) {
                        rpcItem = tableQuery.toRPCSelect(entity.params);
                    } else {
                        rpcItem = tableQuery.toRPCQuery(MAX_COUNT_IN_QUERY, entity.filters);
                    }
                    utils.addFrom(rpcItem);
                }

                if (entity.to) {
                    processingPackageTo(utils, entity.tableName, tid);
                }
            }
        }
        return utils.generatePackage(tid);
    }

    @Override
    public void start(Activity activity, IProgress progress) {
        super.start(activity, progress);

        onProgress(IProgressStep.START, "пакет со справочниками " + dictionaryTid, null);
        onProgress(IProgressStep.START, "пакет общих данных " + totalTid, null);

        // для каждой сущности генерируем свой пакет
        byte[] bytes;
        try {
            bytes = generatePackage(dictionaryTid, (Object) null);
            sendBytes(dictionaryTid, bytes);
        }catch (Exception e){
            onError(IProgressStep.START, "Ошибка обработки пакета для справочников. " + e.toString(), dictionaryTid);
        }
        try {
            bytes = generatePackage(totalTid, (Object) null);
            sendBytes(totalTid, bytes);
        }catch (Exception e){
            onError(IProgressStep.START, "Ошибка обработки пакета для общих таблиц. " + e.toString(), totalTid);
        }
    }

    @Override
    protected void onProcessingPackage(PackageReadUtils utils, String tid) {
        /*
        Если хоть одна вставка была ошибочной, данные не добавлять
         */
        boolean success = true;
        try {
            for (RPCResult result : utils.getResultTo(isZip())) { // при добавление информации была ошибка на сервере.

                PackageResult packageResult = serverSidePackage.to(getDaoSession(), result, tid);
                if(!packageResult.success){
                    onError(IProgressStep.RESTORE, packageResult.message, tid);
                }
                if(success && !packageResult.success)
                {
                    success = false;
                }
            }
        }catch (Exception e){
            onError(IProgressStep.RESTORE, e, tid);
            success = false;
        }

        if(!success){
            return;
        }

        try {
            FullServerSidePackage fullServerSidePackage = (FullServerSidePackage) serverSidePackage;
            //fullServerSidePackage.setDeleteRecordBeforeAppend(true); // TODO: для других режимов синхронизации тут нужно другое значение

            for (RPCResult result : utils.getResultFrom(isZip())) {

                String tableName = result.action;
                Entity entity = getEntity(tableName);
                PackageResult packageResult = serverSidePackage.from(getDaoSession(),result,tid,entity.to, false);

                if(!packageResult.success){
                    onError(IProgressStep.PACKAGE_CREATE, packageResult.message, tid);
                }
            }
        }catch (Exception e){
            onError(IProgressStep.PACKAGE_CREATE, e, tid);
        }
    }
}
