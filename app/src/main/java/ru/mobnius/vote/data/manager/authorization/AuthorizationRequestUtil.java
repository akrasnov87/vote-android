package ru.mobnius.vote.data.manager.authorization;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import ru.mobnius.vote.data.Meta;

class AuthorizationRequestUtil {
    private final String mUrl;

    /**
     * @param url адрес сервера
     */
    public AuthorizationRequestUtil(String url) {
        mUrl = url;
    }

    /**
     * Создание запроса на сервер для выполнения авторизации
     *
     * @param login    логин
     * @param password пароль
     * @return мета информация в результате запроса
     */
    public AuthorizationMeta request(String login, String password) {
        try {
            String urlParams = "UserName=" + login + "&Password=" + password;
            byte[] postData = urlParams.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;

            URL url = new URL(mUrl + "/auth");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {

                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty("Content-Length", String.valueOf(postDataLength));
                urlConnection.setDoOutput(true);
                urlConnection.setInstanceFollowRedirects(false);
                urlConnection.setUseCaches(false);
                urlConnection.getOutputStream().write(postData);

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                Scanner s = new Scanner(in).useDelimiter("\\A");
                String responseText = s.hasNext() ? s.next() : "";
                try {
                    return convertResponseToMeta(responseText);
                } catch (Exception formatExc) {
                    return new AuthorizationMeta(Meta.ERROR_SERVER, "Ошибка в преобразовании ответа на авторизацию.");
                }
            } catch (Exception innerErr) {
                return new AuthorizationMeta(Meta.ERROR_SERVER, "Ошибка создания запроса на авторизацию.");
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            return new AuthorizationMeta(Meta.ERROR_SERVER, "Общая ошибка авторизации.");
        }
    }

    /**
     * Преобразование статуса ответа в мета-информацию
     *
     * @param response ответ от сервера в формате JSON
     * @return мета информация
     */
    AuthorizationMeta convertResponseToMeta(String response) {
        int status;
        String token = null;
        Integer userId = null;
        String claims = null;
        String message;

        try {
            JSONObject jsonObject = new JSONObject(response);
            try {
                status = jsonObject.getInt("code");
                message = jsonObject.getJSONObject("meta").getString("msg");
            } catch (JSONException e) {
                status = Meta.OK;
                message = "Пользователь авторизован.";
                token = jsonObject.getString("token");
                userId = jsonObject.getJSONObject("user").getInt("userId");
                claims = jsonObject.getJSONObject("user").getString("claims");
            }
        }catch (Exception e){
            status = Meta.ERROR_SERVER;
            message = "Результат авторизации не является JSON.";
        }
        return new AuthorizationMeta(status, message, token, claims, userId);
    }
}
