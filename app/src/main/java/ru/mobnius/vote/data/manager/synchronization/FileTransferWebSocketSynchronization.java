package ru.mobnius.vote.data.manager.synchronization;

import android.app.Activity;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import ru.mobnius.vote.data.manager.FileManager;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.rpc.RPCItem;
import ru.mobnius.vote.data.manager.rpc.RPCResult;
import ru.mobnius.vote.data.manager.synchronization.meta.TableQuery;
import ru.mobnius.vote.data.manager.synchronization.utils.FullServerSidePackage;
import ru.mobnius.vote.data.manager.synchronization.utils.PackageResult;
import ru.mobnius.vote.data.storage.models.DaoSession;
import ru.mobnius.vote.data.storage.models.Files;
import ru.mobnius.vote.data.storage.models.FilesDao;
import ru.mobnius.vote.utils.PackageCreateUtils;
import ru.mobnius.vote.utils.PackageReadUtils;

/**
 * синхронизация по передачи данных по websocket с вложениями
 */
public abstract class FileTransferWebSocketSynchronization extends WebSocketSynchronization {

    /**
     * конструктор
     * @param session сессия для подключения к БД
     * @param name имя
     * @param fileManager файловый менеджер
     */
    public FileTransferWebSocketSynchronization(DaoSession session, String name, FileManager fileManager, boolean zip) {
        super(session, name, zip);
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
            fullServerSidePackage.setDeleteRecordBeforeAppend(true); // TODO: для других режимов синхронизации тут нужно другое значение
            fullServerSidePackage.setFileBinary(utils.getFiles());

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
