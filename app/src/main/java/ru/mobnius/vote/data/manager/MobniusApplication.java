package ru.mobnius.vote.data.manager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.configuration.ConfigurationSetting;
import ru.mobnius.vote.data.manager.configuration.ConfigurationSettingUtil;
import ru.mobnius.vote.data.manager.configuration.DefaultPreferencesManager;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.credentials.BasicCredentials;
import ru.mobnius.vote.data.manager.exception.ExceptionUtils;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.manager.exception.IExceptionGroup;
import ru.mobnius.vote.data.manager.exception.IExceptionIntercept;
import ru.mobnius.vote.data.manager.exception.MyUncaughtExceptionHandler;
import ru.mobnius.vote.data.storage.DbOpenHelper;
import ru.mobnius.vote.data.storage.models.DaoMaster;
import ru.mobnius.vote.data.storage.models.DaoSession;
import ru.mobnius.vote.utils.AuditUtils;
import ru.mobnius.vote.utils.HardwareUtil;
import ru.mobnius.vote.utils.NetworkUtil;

public class MobniusApplication extends android.app.Application implements IExceptionIntercept, OnNetworkChangeListener {
    private ServiceManager serviceManager;
    private List<OnNetworkChangeListener> mNetworkChangeListener;
    // TODO: 01/01/2020 потом заменить на чтение QR-кода
    public static String getBaseUrl() {
        String baseUrl = "http://kes.it-serv.ru";
        String virtualDirPath = "/vote/dev";

        //String baseUrl = "http://192.168.1.68:3000";
        //String virtualDirPath = "";

        return baseUrl + virtualDirPath;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        onExceptionIntercept();
        Logger.setContext(getApplicationContext());

        DefaultPreferencesManager.createInstance(this, DefaultPreferencesManager.NAME);
        serviceManager = new ServiceManager(this);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);

        Authorization.createInstance(this, getBaseUrl());

        // отслеживаем изменения подключения к сети интернет
        registerReceiver(new NetworkChangeReceiver(), filter);
    }


    /**
     * обработчик авторизации пользователя
     * @param type тип авторизации
     */
    public void onAuthorized(int type) {
        BasicCredentials credentials = Authorization.getInstance().getUser().getCredentials();
        PreferencesManager.createInstance(this, credentials.login);

        DaoSession daoSession = new DaoMaster(new DbOpenHelper(this, credentials.login + ".db").getWritableDb()).newSession();

        // enable debug for SQL Queries
        if(PreferencesManager.getInstance().isDebug()) {
            QueryBuilder.LOG_SQL = true;
            QueryBuilder.LOG_VALUES = true;
        }

        DataManager.createInstance(daoSession);

        AuditUtils.write(String.valueOf(type), AuditUtils.ON_AUTH, AuditUtils.Level.HIGH);
        if (SocketManager.getInstance() != null)
            SocketManager.getInstance().destroy();

        SocketManager socketManager = SocketManager.createInstance(getBaseUrl(), credentials, HardwareUtil.getIMEI(this));
        socketManager.open();

        serviceManager.startMyService();

        ExceptionUtils.saveLocalException(this, Authorization.getInstance().getUser().getUserId(), daoSession);
    }

    /**
     * пользователь сбросил авторизацию
     *
     * @param clearUserAuthorization очистка авторизации пользователя
     */
    public void unAuthorized(boolean clearUserAuthorization) {

        AuditUtils.write("", AuditUtils.UN_AUTH, AuditUtils.Level.HIGH);

        serviceManager.stopMyService();

        if (clearUserAuthorization) {
            Authorization.getInstance().destroy();
        } else {
            Authorization.getInstance().reset();
        }
        SocketManager.getInstance().destroy();
        PreferencesManager.getInstance().destroy();
    }

    @Override
    public void onExceptionIntercept() {
        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler(), getExceptionGroup(), getExceptionCode(), this));
    }

    @Override
    public String getExceptionGroup() {
        return IExceptionGroup.APPLICATION;
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.ALL;
    }

    /**
     * Обработчик изменения подключения к сети
     *
     * @param online       приложение в онлайн
     * @param serverExists подключение к серверу доступно.
     */
    @Override
    public void onNetworkChange(boolean online, boolean serverExists) {
        if (mNetworkChangeListener != null) {
            for (OnNetworkChangeListener change : mNetworkChangeListener) {
                if (change != null) {
                    change.onNetworkChange(online, serverExists);
                }
            }
        }
    }

    /**
     * Подписаться для добавления обработчика. Делать это в событии onStart
     *
     * @param change обработчик
     */
    public void addNetworkChangeListener(OnNetworkChangeListener change) {
        if (mNetworkChangeListener == null) {
            mNetworkChangeListener = new ArrayList<>();
        }

        mNetworkChangeListener.add(change);
    }

    /**
     * Подписаться для удаление обработчика. Делать это в событии onStop
     *
     * @param change обработчик
     */
    public void removeNetworkChangeListener(OnNetworkChangeListener change) {
        if (mNetworkChangeListener != null) {
            mNetworkChangeListener.remove(change);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public static class ConfigurationAsyncTask extends AsyncTask<Void, Void, Boolean> {

        private final OnConfigurationLoadedListener mListener;

        public ConfigurationAsyncTask(OnConfigurationLoadedListener listener) {
            mListener = listener;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if(Authorization.getInstance().isAuthorized()) {
                BasicCredentials credentials = Authorization.getInstance().getLastAuthUser().getCredentials();

                try {
                    List<ConfigurationSetting> configurationSettings = ConfigurationSettingUtil.getSettings(credentials);
                    if (configurationSettings != null) {
                        return DefaultPreferencesManager.getInstance().updateSettings(configurationSettings);
                    }
                } catch (Exception ignore) {

                }
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean value) {
            mListener.onConfigurationLoaded(value);
        }
    }

    public interface OnConfigurationLoadedListener {
        void onConfigurationLoaded(boolean configRefreshed);
    }
}
