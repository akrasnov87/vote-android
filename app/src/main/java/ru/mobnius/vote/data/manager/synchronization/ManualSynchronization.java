package ru.mobnius.vote.data.manager.synchronization;

import android.app.Activity;

import java.util.UUID;

import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.FileManager;
import ru.mobnius.vote.data.manager.rpc.FilterItem;
import ru.mobnius.vote.data.manager.synchronization.utils.FullServerSidePackage;
import ru.mobnius.vote.data.storage.models.DaoSession;
import ru.mobnius.vote.data.storage.models.DigestsDao;
import ru.mobnius.vote.data.storage.models.DivisionsDao;
import ru.mobnius.vote.data.storage.models.PointTypesDao;
import ru.mobnius.vote.data.storage.models.PointsDao;
import ru.mobnius.vote.data.storage.models.RegistrPts;
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

/**
 * Ручная синхронизация данных
 */
public class ManualSynchronization extends FileTransferWebSocketSynchronization {

    private static ManualSynchronization manualSynchronization;

    public static ManualSynchronization getInstance(boolean zip){
        if(manualSynchronization != null){
            return manualSynchronization;
        }else{
            return manualSynchronization = new ManualSynchronization(DataManager.getInstance().getDaoSession(), FileManager.getInstance(), zip);
        }
    }

    public String totalTid;
    public String dictionaryTid;

    /**
     * конструктор
     * @param session сессия для подключения к БД
     * @param fileManager файловый менеджер
     */
    protected ManualSynchronization(DaoSession session, FileManager fileManager, boolean zip) {
        super(session, "MANUAL_SYNCHRONIZATION", fileManager, zip);
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
        addEntity(new EntityDictionary(RolesDao.TABLENAME, false, true).setTid(dictionaryTid));
        addEntity(new EntityDictionary(DigestsDao.TABLENAME, false, true).setTid(dictionaryTid).setFilter(new FilterItem(DigestsDao.Properties.C_app_name.name, "android")));
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
}
