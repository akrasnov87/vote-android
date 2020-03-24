package ru.mobnius.vote.data.manager.synchronization.utils;

import org.json.JSONException;

import ru.mobnius.vote.data.manager.rpc.RPCResult;
import ru.mobnius.vote.data.storage.models.DaoSession;


/**
 * интерфейс для обработки пакетов принятых от сервера
 */
public interface IServerSidePackage {
    /**
     * обработка блока to
     * результат обработки информации переданнйо в блоке to
     * @param session сессия
     * @param rpcResult результат RPC
     * @param packageTid идентификатор пакета
     * @return результат
     */
    PackageResult to(DaoSession session, RPCResult rpcResult, String packageTid);

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
    PackageResult from(DaoSession session, RPCResult rpcResult, String packageTid, boolean isRequestToServer, boolean attachmentUse) throws JSONException;
}
