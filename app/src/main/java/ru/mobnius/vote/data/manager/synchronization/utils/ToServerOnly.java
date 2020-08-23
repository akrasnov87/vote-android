package ru.mobnius.vote.data.manager.synchronization.utils;

import org.greenrobot.greendao.database.Database;

import ru.mobnius.vote.data.manager.rpc.RPCResult;
import ru.mobnius.vote.data.storage.models.DaoSession;

/**
 * Обработчик для пакетов только в направлении сервера
 */
public class ToServerOnly extends ServerSidePackage {
    @Override
    public PackageResult to(DaoSession session, RPCResult rpcResult, String packageTid) {
        if(rpcResult.meta.success) {
            // тут все хорошо, нужно удалить все записи c tid
            Database db = session.getDatabase();
            Object[] params = new Object[1];
            params[0] = packageTid;

            db.execSQL("delete from " + rpcResult.action + " where tid = ?", params);
            return PackageResult.success(null);
        } else {
            return PackageResult.fail(rpcResult.meta.msg, null);
        }
    }
}
