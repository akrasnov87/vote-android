package ru.mobnius.vote.data.manager.synchronization;

import android.app.Activity;

import org.greenrobot.greendao.AbstractDao;

import java.io.IOException;
import java.util.UUID;

import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.rpc.RPCItem;
import ru.mobnius.vote.data.manager.rpc.RPCResult;
import ru.mobnius.vote.data.manager.synchronization.meta.TableQuery;
import ru.mobnius.vote.data.manager.synchronization.utils.LiteServerSidePackage;
import ru.mobnius.vote.data.manager.synchronization.utils.PackageResult;
import ru.mobnius.vote.data.storage.models.ContactsDao;
import ru.mobnius.vote.data.storage.models.DaoSession;
import ru.mobnius.vote.data.storage.models.FeedbacksDao;
import ru.mobnius.vote.data.storage.models.ResultsDao;
import ru.mobnius.vote.data.storage.models.UserPointsDao;
import ru.mobnius.vote.utils.PackageCreateUtils;
import ru.mobnius.vote.utils.PackageReadUtils;
import ru.mobnius.vote.utils.SyncUtil;

/**
 * Ручная синхронизация данных
 */
public class LiteSynchronization extends WebSocketSynchronization {

    private static LiteSynchronization sLiteSynchronization;

    public static LiteSynchronization getInstance(boolean zip) {
        if(sLiteSynchronization != null) {
            return sLiteSynchronization;
        }else{
            return sLiteSynchronization = new LiteSynchronization(DataManager.getInstance().getDaoSession(), zip);
        }
    }

    public String totalTid;

    /**
     * конструктор
     * @param session сессия для подключения к БД
     */
    protected LiteSynchronization(DaoSession session, boolean zip) {
        super(session, "LITE_SYNCHRONIZATION", zip);
        oneOnlyMode = true;
        serverSidePackage = new LiteServerSidePackage();
    }

    @Override
    protected void initEntities() {
        totalTid = UUID.randomUUID().toString();

        addEntity(Entity.createInstance(UserPointsDao.TABLENAME, true, false).setTid(totalTid).setParam(getUserID()).setUseCFunction());
        addEntity(Entity.createInstance(ResultsDao.TABLENAME, true, false).setTid(totalTid).setParam(getUserID()).setUseCFunction());
        addEntity(Entity.createInstance(FeedbacksDao.TABLENAME, true, false).setTid(totalTid).setParam(getUserID()).setUseCFunction());
        addEntity(Entity.createInstance(ContactsDao.TABLENAME, true, false).setTid(totalTid).setParam(getUserID()).setUseCFunction());
    }

    @Override
    public void start(Activity activity, IProgress progress) {
        super.start(activity, progress);

        onProgress(IProgressStep.START, "пакет общих данных " + totalTid, null);

        // для каждой сущности генерируем свой пакет
        byte[] bytes;
        try {
            bytes = generatePackage(totalTid, (Object) null);
            sendBytes(totalTid, bytes);
        }catch (Exception e){
            onError(IProgressStep.START, "Ошибка обработки пакета для общих таблиц. " + e.toString(), totalTid);
        }
    }

    @Override
    protected byte[] generatePackage(String tid, Object... args) throws IOException {
        PackageCreateUtils utils = new PackageCreateUtils(isZip());
        for(Entity entity : getEntities()){
            // обработка только элемента с указанным ключом
            if(entity.tid.equals(tid)) {
                if (entity.to) {
                    processingPackageTo(utils, entity.tableName, tid);
                }
            }
        }
        return utils.generatePackage(tid);
    }

    @Override
    protected void onProcessingPackage(PackageReadUtils utils, String tid) {
        /*
        Если хоть одна вставка была ошибочной, данные не добавлять
         */
        try {
            for (RPCResult result : utils.getResultTo(isZip())) { // при добавление информации была ошибка на сервере.

                PackageResult packageResult = serverSidePackage.to(getDaoSession(), result, tid);
                if(!packageResult.success){
                    onError(IProgressStep.RESTORE, packageResult.message, tid);
                }
            }
        }catch (Exception e){
            onError(IProgressStep.RESTORE, e, tid);
        }
    }
}