package ru.mobnius.vote.data.manager;

import com.google.gson.annotations.Expose;

import org.json.JSONException;

import java.io.IOException;

import ru.mobnius.vote.data.manager.mail.GeoMail;
import ru.mobnius.vote.data.manager.mail.StringMail;
import ru.mobnius.vote.data.manager.rpc.RPCResult;
import ru.mobnius.vote.data.manager.rpc.SingleItemQuery;

public class NotificationManager {
    private String mToken;

    public NotificationManager(String token) {
        mToken = token;
    }

    /**
     * Количество новых уведомлений на сервере
     * @return кол-во уведомлений
     */
    public int getNewMessageCount() throws IOException, JSONException {
        RPCResult[] rpcResults = RequestManager.rpc(MobniusApplication.getBaseUrl(), mToken, "notification", "getUserNotifications", new SingleItemQuery(null));
        if(rpcResults.length > 0) {
            RPCResult rpcResult = rpcResults[0];
            if(rpcResult.isSuccess()) {
                if(rpcResult.result.records.length > 0) {
                    return rpcResult.result.records[0].getInt("value");
                }
            }
        }
        return -1;
    }

    /**
     * Изменение статуса уведомления о том, что о прочитан
     * @param ids иден. уведомлений.
     * @return true - запрос выполнен без ошибок
     */
    public boolean changeStatus(String[] ids) throws IOException {
        RPCResult[] rpcResults = RequestManager.rpc(MobniusApplication.getBaseUrl(), mToken, "notification", "changeStatus", new SingleItemQuery(new Selection(ids)));
        if(rpcResults.length > 0) {
            RPCResult rpcResult = rpcResults[0];
            return rpcResult.isSuccess();
        }

        return false;
    }

    /**
     * Все уведомления доставлены и прочитаны
     * @return true - запрос выполнен без ошибок
     * @throws IOException
     */
    public boolean changeStatusAll() throws IOException {
        RPCResult[] rpcResults = RequestManager.rpc(MobniusApplication.getBaseUrl(), mToken, "notification", "changeStatusAll", new SingleItemQuery(null));
        if(rpcResults.length > 0) {
            RPCResult rpcResult = rpcResults[0];
            return rpcResult.isSuccess();
        }

        return false;
    }

    /**
     * Уведомления доставлены на мобильное устройство
     * @return true - запрос выполнен без ошибок
     */
    public boolean sended() throws IOException {
        RPCResult[] rpcResults = RequestManager.rpc(MobniusApplication.getBaseUrl(), mToken, "notification", "sended", new SingleItemQuery(null));
        if(rpcResults.length > 0) {
            RPCResult rpcResult = rpcResults[0];
            return rpcResult.isSuccess();
        }

        return false;
    }

    /**
     * Отправка уведомления на сервер
     * @param body текст
     * @param to кому
     * @param group гпуппа
     */
    public void sendMessage(String body, String to, String group) {
        // отправляем текущее местоположение пользователя
        SocketManager socketManager = SocketManager.getInstance();
        if(socketManager != null && socketManager.isRegistered()) {
            socketManager.getSocket().emit("notification", (Object) MailManager.send(body, to, group));
        }
    }

    private class Selection {
        public Selection(String[] values) {
            selection = values;
        }

        @Expose
        public String[] selection;
    }
}
