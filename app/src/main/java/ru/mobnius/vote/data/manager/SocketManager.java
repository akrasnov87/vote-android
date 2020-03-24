package ru.mobnius.vote.data.manager;

import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.data.manager.credentials.BasicCredentials;
import ru.mobnius.vote.utils.UrlUtil;

/**
 * Создание websocket подключения к серверу
 * Подробнее читать тут
 * https://socket.io/blog/native-socket-io-and-android/
 * https://github.com/socketio/socket.io-client-java
 */
public class SocketManager {
    /**
     * Имя события регистрации на сервере
     */
    public final static String EVENT_REGISTRY = "registry";
    /**
     * Событие не авторизации
     */
    public final static String EVENT_NOT_AUTH = "not_auth";
    public final static String EVENT_MAIL_FROM = "mailer-from";
    public final static String EVENT_GROUP_MAIL_FROM = "mailer-group-from";
    private static SocketManager socketManager;
    private ISocketNotification mSocketNotification;

    private Socket socket;
    private boolean isRegistry;

    /**
     * Подключение к сокету
     * @param url адресная строка подключения
     * @param credentials безопасность
     * @param imei IMEI
     */
    private SocketManager(String url, BasicCredentials credentials, String imei){
        /*
          Поддерживаемые протоколы транспорта
         */
        String[] transports = new String[1];
        transports[0] = "websocket";

        try {
            IO.Options opts = new IO.Options();
            opts.forceNew = true;
            opts.path = UrlUtil.getPathUrl(url) + "/socket.io";
            if(credentials != null || imei != null) {
                String query = "";
                if(credentials != null) {
                    query = "token=" + credentials.getToken();
                }

                if(imei != null){
                    query += (query.isEmpty() ? "" : "&") + "imei=" + imei;
                }
                opts.query = query;
            }
            opts.transports = transports;

            socket = IO.socket(UrlUtil.getDomainUrl(url), opts);

        } catch (URISyntaxException e) {
            Logger.error(e);
        }
    }

    /**
     * создает и возвращается текущий экземпляр подключения
     * @param url адресная строка подключения
     * @param credentials безопасность
     * @param imei IMEI
     * @return Объект socket-подключения
     */
    public static SocketManager createInstance(String url, BasicCredentials credentials, String imei){
        if(socketManager != null){
            return socketManager;
        }else{
            return socketManager = new SocketManager(url, credentials, imei);
        }
    }

    /**
     * возвращается текущий экземпляр подключения
     * @return Объект socket-подключения
     */
    public static SocketManager getInstance(){
        return socketManager;
    }

    /**
     * Открытие подключения к серверу
     * @param notification обработчик уведомлений
     */
    public void open(final ISocketNotification notification) {
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("SOCKET OPEN", "OPEN");
            }
        });

        socket.on(EVENT_REGISTRY, new Emitter.Listener(){
            @Override
            public void call(Object... args) {
                isRegistry = true;
            }
        });

        socket.on(EVENT_MAIL_FROM, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                notification.onNotificationMessage(EVENT_MAIL_FROM, (byte[])args[0]);
            }
        });

        socket.on(EVENT_GROUP_MAIL_FROM, new Emitter.Listener(){
            @Override
            public void call(Object... args) {
                notification.onNotificationMessage(EVENT_GROUP_MAIL_FROM, (byte[])args[0]);
            }
        });

        socket.on(EVENT_NOT_AUTH, new Emitter.Listener(){
            @Override
            public void call(Object... args) {
                JSONObject jsonObject = (JSONObject) args[0];
                try {
                    Logger.debug(jsonObject.getJSONObject("data").getString("msg"));
                } catch (JSONException ignored) {

                }
            }
        });

        socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                isRegistry = false;
            }
        });

        socket.connect();
    }

    /**
     * текущее сокет подключение
     * @return сокет соединение
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * зарегистрирован ли пользователь на сервере
     * @return true - пользователь был зарегистрирован ранее
     */
    public boolean isRegistered(){
        if(socket!= null)
            return isRegistry && socket.connected();
        return false;
    }

    /**
     * Подключение к сокет серверу доступно
     * @return true - подключение доступно
     */
    public boolean isConnected(){
        if(socket!= null)
            return socket.connected();
        return false;
    }

    /**
     * Закрытие подключения
     */
    public void close(){
        if(socket != null) {
            socket.off();
            socket.close();
        }
        isRegistry = false;
    }

    public void destroy() {
        close();
        socket = null;
        socketManager = null;
    }
}
