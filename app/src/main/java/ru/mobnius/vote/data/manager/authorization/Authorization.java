package ru.mobnius.vote.data.manager.authorization;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import ru.mobnius.vote.data.ICallback;
import ru.mobnius.vote.data.Meta;
import ru.mobnius.vote.data.manager.configuration.ConfigurationSetting;
import ru.mobnius.vote.data.manager.configuration.ConfigurationSettingUtil;
import ru.mobnius.vote.data.manager.configuration.DefaultPreferencesManager;
import ru.mobnius.vote.data.manager.credentials.BasicCredentials;
import ru.mobnius.vote.data.manager.credentials.BasicUser;
import ru.mobnius.vote.utils.ClaimsUtil;

/**
 * Авторизация
 */
public class Authorization {
    private static final String INSPECTOR_CLAIM = "inspector";

    private final AuthorizationCache mAuthorizationCache;
    private final AuthorizationRequestUtil mRequestUtil;

    private ICallback mICallback;

    /**
     * Авторизация при наличии интернет соединения
     */
    public static final int ONLINE = 0;

    /**
     * Авторизация при отсутствии интернета
     */
    public static final int OFFLINE = 1;

    /**
     * Информация о пользователе
     */
    private BasicUser mUser;

    /**
     * Статус авторизации.
     * По умолчанию FAIL
     */
    private AuthorizationStatus mStatus = AuthorizationStatus.FAIL;

    private static Authorization mAuthorization;

    private Authorization(Context context, String baseUrl) {
        mAuthorizationCache = new AuthorizationCache(context);
        mRequestUtil = new AuthorizationRequestUtil(baseUrl);
    }

    /**
     * созданеие экземпляра класса
     * @param context текущее приложение
     * @param baseUrl адрес запроса
     * @return Объект для реализации авторизации пользователя
     */
    public static Authorization createInstance(Context context, String baseUrl) {
        if(mAuthorization != null){
            return mAuthorization;
        }else{
            return mAuthorization = new Authorization(context, baseUrl);
        }
    }

    public static Authorization getInstance(){
        return mAuthorization;
    }

    /**
     * Авторизован пользователь или нет
     * @return true - пользователь авторизован
     */
    public boolean isAuthorized(){
        return mStatus == AuthorizationStatus.SUCCESS;
    }

    /**
     * Обработчик авторизации
     * authorization.onSignIn("login", "password", Authorization.ONLINE, new ICallback() {
     *     public void onResult(Meta meta) {
     *          // здесь обработка
     *     }
     * })
     * @param login логин
     * @param password пароль
     * @param mode режим авторизации: Authorization.ONLINE или Authorization.OFFLINE
     * @param callback результат обратного вызова
     */
    public void onSignIn(String login, String password, int mode, ICallback callback) {
        mICallback = callback;
        if(mode == ONLINE) {
            AuthAsyncTask authAsyncTask = new AuthAsyncTask();
            authAsyncTask.execute(login, password);
        } else {
            BasicUser basicUser = mAuthorizationCache.read(login);
            if(basicUser != null) {
                setUser(basicUser);
                if(basicUser.getCredentials().password.equals(password)) {
                    callback.onResult(new AuthorizationMeta(
                            Meta.OK,
                            "Вы авторизованы",
                            basicUser.getCredentials().getToken(),
                            basicUser.claims,
                            Integer.parseInt(String.valueOf(basicUser.getUserId()))));
                } else {
                    callback.onResult(new AuthorizationMeta(Meta.NOT_AUTHORIZATION, "Логин или пароль введены не верно.", null, null, null));
                    reset();
                }
            } else {
                callback.onResult(new AuthorizationMeta(Meta.NOT_AUTHORIZATION, "У приложения отсутствует доступ к серверу. Проверьте интернет.", null, null, null));
                reset();
            }
        }
    }

    /**
     * обновление пользователя
     * @param basicUser объект пользователя
     */
    public void setUser(BasicUser basicUser) {
        mUser = basicUser;
        mAuthorizationCache.write(basicUser);
        mStatus = AuthorizationStatus.SUCCESS;
    }

    /**
     * получение текущего авторизованного пользователя
     * @return текущий пользователь
     */
    public BasicUser getUser(){
        return mUser;
    }

    /**
     * Требуется ли выполнять автоматическу авторизацию
     * @return true - можно автоматически авторизовать пользователя.
     */
    public boolean isAutoSignIn() {
        return mAuthorizationCache.getNames().length == 1;
    }

    /**
     * Является инспектором
     * @return true - авторизованный пользователь является инспектором
     */
    public boolean isInspector() {
        if(mUser != null) {
            ClaimsUtil util = new ClaimsUtil(mUser.claims);
            return util.isExists(INSPECTOR_CLAIM);
        }

        return false;
    }

    /**
     * Получение информации о последнем авторизованном пользователе
     * @return пользователь
     */
    public BasicUser getLastAuthUser() {
        String[] names = mAuthorizationCache.getNames();
        if(names.length == 1) {
            String name = names[0];
            return mAuthorizationCache.read(name);
        }

        return null;
    }

    /**
     * Сброс авторизации
     */
    public void reset(){
        mUser = null;
        mStatus = AuthorizationStatus.FAIL;
    }

    public void destroy() {
        reset();
        mICallback = null;
        mAuthorizationCache.clear(true);
    }

    @SuppressLint("StaticFieldLeak")
    private class AuthAsyncTask extends AsyncTask<String, Void, AuthorizationMeta> {

        private BasicCredentials mCredentials;

        @Override
        protected AuthorizationMeta doInBackground(String... strings) {
            mCredentials = new BasicCredentials(strings[0], strings[1]);
            /*try {
                List<ConfigurationSetting> configurationSettings = ConfigurationSettingUtil.getSettings(mCredentials);
                if (configurationSettings != null) {
                    DefaultPreferencesManager.getInstance().updateSettings(configurationSettings);
                }
            }catch (Exception ignore) {

            }*/

            return mRequestUtil.request(mCredentials.login, mCredentials.password);
        }

        @Override
        protected void onPostExecute(AuthorizationMeta authorizationMeta) {
            super.onPostExecute(authorizationMeta);

            if(!authorizationMeta.isSuccess()) {
                reset();
            } else {
                BasicUser basicUser = new BasicUser(mCredentials, authorizationMeta.getUserId(),authorizationMeta.getClaims());
                setUser(basicUser);
            }

            mICallback.onResult(authorizationMeta);
        }
    }
}
