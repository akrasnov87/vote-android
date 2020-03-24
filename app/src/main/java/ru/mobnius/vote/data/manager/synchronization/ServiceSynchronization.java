package ru.mobnius.vote.data.manager.synchronization;

import android.app.Activity;

import java.util.UUID;

import ru.mobnius.vote.data.GlobalSettings;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.rpc.RPCResult;
import ru.mobnius.vote.data.manager.SocketManager;
import ru.mobnius.vote.data.manager.synchronization.utils.PackageResult;
import ru.mobnius.vote.data.manager.synchronization.utils.ToServerOnly;
import ru.mobnius.vote.data.storage.models.AuditsDao;
import ru.mobnius.vote.data.storage.models.ClientErrorsDao;
import ru.mobnius.vote.data.storage.models.DaoSession;
import ru.mobnius.vote.data.storage.models.MobileDevicesDao;
import ru.mobnius.vote.data.storage.models.MobileIndicatorsDao;
import ru.mobnius.vote.data.storage.models.TrackingDao;
import ru.mobnius.vote.utils.PackageCreateUtils;
import ru.mobnius.vote.utils.PackageReadUtils;

/**
 * Механизм синхронизации служебных данных
 */
public class ServiceSynchronization extends WebSocketSynchronization {

    private static ServiceSynchronization serviceSynchronization;

    public static ServiceSynchronization getInstance(boolean zip){
        if(serviceSynchronization != null){
            return serviceSynchronization;
        }else{
            return serviceSynchronization = new ServiceSynchronization(DataManager.getInstance().getDaoSession(), zip);
        }
    }

    /**
     * конструктор
     * @param session сессия для подключения к БД
     */
    protected ServiceSynchronization(DaoSession session, boolean zip){
        super(session, "SERVICE_SYNCHRONIZATION", zip);

        serverSidePackage = new ToServerOnly();
    }

    @Override
    public void start(Activity activity, IProgress progress) {
        super.start(activity, progress);

        // для каждой сущности генерируем свой пакет
        for(Entity entity : getEntities()){
            byte[] bytes;
            try {
                bytes = generatePackage(entity.tid, entity.tableName);
                sendBytes(entity.tid, bytes);
            }catch (Exception e){
                onError(IProgressStep.START, "Ошибка обработки пакета для таблицы " + entity.tableName + ". " + e.toString(), entity.tid);
            }
        }
    }

    @Override
    protected void onProcessingPackage(PackageReadUtils utils, String tid) {
        try {
            RPCResult[] rpcResults = utils.getResultTo(isZip());
            for(RPCResult result : rpcResults){
                PackageResult packageResult = serverSidePackage.to(getDaoSession(), result, tid);
                if(!packageResult.success){
                    oneOnlyMode = true;
                    onError(IProgressStep.RESTORE, packageResult.message, tid);
                }else {
                    oneOnlyMode = false;
                }
            }
        }catch (Exception e){
            onError(IProgressStep.RESTORE, e, tid);
        }
    }

    @Override
    protected byte[] generatePackage(String tid, Object... args) throws Exception {
        PackageCreateUtils utils = new PackageCreateUtils(isZip());
        String tableName = (String) args[0];
        if(tableName == null || (tableName != null && tableName.isEmpty())){
            throw new Exception("Имя таблицы в аргументах не передано.");
        }
        processingPackageTo(utils, tableName, tid);
        return utils.generatePackage(tid);
    }

    @Override
    protected void initEntities() {
        addEntity(new Entity(TrackingDao.TABLENAME).setTid(UUID.randomUUID().toString()));
        addEntity(new Entity(AuditsDao.TABLENAME).setTid(UUID.randomUUID().toString()));
        addEntity(new Entity(MobileDevicesDao.TABLENAME).setTid(UUID.randomUUID().toString()));
        addEntity(new Entity(MobileIndicatorsDao.TABLENAME).setTid(UUID.randomUUID().toString()));
        addEntity(new Entity(ClientErrorsDao.TABLENAME).setTid(UUID.randomUUID().toString()));
    }
}
