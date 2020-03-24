package ru.mobnius.vote.data.manager;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Scanner;

import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.data.manager.rpc.QueryData;
import ru.mobnius.vote.data.manager.rpc.SingleItemQuery;
import ru.mobnius.vote.data.manager.rpc.RPCItem;
import ru.mobnius.vote.data.manager.rpc.RPCResult;
import ru.mobnius.vote.utils.StringUtil;

public class RequestManager {
    /**
     * заголовок для авторизации по умолчанию
     */
    public final static String AUTHORIZATION_HEADER = "rpc-authorization";

    /**
     * время на проверку подключения к серверу в милисекундах
     */
    public final static int SERVER_CONNECTION_TIMEOUT = 3000;

    public final static String KEY_VERSION = "VERSION";
    public final static String KEY_DB_VERSION = "DB_VERSION";
    public final static String KEY_IP = "IP";

    /**
     * Выполнение RPC запроса
     * @param baseUrl настройки соединения
     * @param token токен-авторизация
     * @param postData входные данные
     * @return возвращается строка если возникла ошибка, либо объект RPCResult[]
     * @throws IOException общая ошибка
     */
    public static RPCResult[] rpc(String baseUrl, String token, byte[] postData) throws IOException {
        URL url = new URL(baseUrl + "/rpc");
        RPCResult[] rpcResults = null;
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {

            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.setRequestProperty("Accept","application/json");

            if(!StringUtil.isEmptyOrNull(token))
                urlConnection.setRequestProperty(AUTHORIZATION_HEADER, token);

            urlConnection.setRequestProperty("Content-Length", String.valueOf(postData.length));
            urlConnection.setDoOutput(true);
            urlConnection.setInstanceFollowRedirects( false );
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(SERVER_CONNECTION_TIMEOUT);

            urlConnection.getOutputStream().write(postData);

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            Scanner s = new Scanner(in).useDelimiter("\\A");
            String serverResult = s.hasNext() ? s.next() : "";

            try{
                rpcResults = RPCResult.createInstance(serverResult);
            }catch (Exception formatExc){
                Logger.error(formatExc);
            }
        }catch (Exception innerErr) {
            Logger.error("Ошибка создания запроса RPC.", innerErr);
        }finally {
            urlConnection.disconnect();
        }

        return rpcResults;
    }

    /**
     * RPC запрос
     * @param baseUrl настройки соединения
     * @param token токен-авторизация
     * @param action сущность
     * @param method метод
     * @param data данные
     * @return возвращается строка если возникла ошибка, либо объект RPCResult[]
     */
    public static RPCResult[] rpc(String baseUrl, String token, String action, String method, SingleItemQuery data) throws IOException {
        return rpc(baseUrl, token, action, method, (Object) data);
    }

    /**
     * RPC запрос
     * @param baseUrl настройки соединения
     * @param token токен-авторизация
     * @param action сущность
     * @param method метод
     * @param data данные
     * @return возвращается строка если возникла ошибка, либо объект RPCResult[]
     */
    public static RPCResult[] rpc(String baseUrl, String token, String action, String method, QueryData data) throws IOException {
        return rpc(baseUrl, token, action, method, (Object) data);
    }

    /**
     * RPC запрос
     * @param baseUrl настройки соединения
     * @param token токен-авторизация
     * @param action сущность
     * @param method метод
     * @param data данные
     * @return возвращается строка если возникла ошибка, либо объект RPCResult[]
     */
    public static RPCResult[] rpc(String baseUrl, String token, String action, String method, Object data) throws IOException {
        RPCItem item = new RPCItem();
        item.action = action;
        item.method = method;
        if(data != null) {
            item.data = new Object[1];
            item.data[0] = data;
        }else {
            item.data = new Object[1];
        }

        String urlParams = item.toJsonString();
        byte[] postData = urlParams.getBytes(StandardCharsets.UTF_8);

        return rpc(baseUrl, token, postData);
    }

    /**
     * Проверка на доступность подключения к серверу приложения
     *
     * @param baseUrl настройки соединения
     * @return возвращается строка если возникла ошибка, либо объект ServerExists
     * @throws IOException общая ошибка
     */
    public static HashMap<String, String> exists(String baseUrl) throws IOException {
        URL url = new URL(baseUrl + "/exists");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        HashMap<String, String> hashMap = null;
        try {

            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(SERVER_CONNECTION_TIMEOUT);

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            Scanner s = new Scanner(in).useDelimiter("\\A");
            String result = s.hasNext() ? s.next() : "";
            try {
                JSONObject object = new JSONObject(result);

                hashMap = new HashMap<>();
                hashMap.put(KEY_VERSION, object.getString("version"));
                hashMap.put(KEY_DB_VERSION, object.getString("dbVersion"));
                hashMap.put(KEY_IP, object.getString("ip"));

                return hashMap;
            } catch (Exception formatExc) {
                Logger.error("Результат не является JSON, либо формат неизвестен.", formatExc);
            }
        } catch (Exception e) {
            Logger.error(e);
        } finally {
            urlConnection.disconnect();
        }

        return hashMap;
    }
}
