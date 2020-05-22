package ru.mobnius.vote.data.manager;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.configuration.DefaultPreferencesManager;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.manager.credentials.BasicCredentials;
import ru.mobnius.vote.data.manager.exception.ExceptionUtils;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.manager.exception.IExceptionGroup;
import ru.mobnius.vote.data.manager.exception.IExceptionIntercept;
import ru.mobnius.vote.data.manager.exception.MyUncaughtExceptionHandler;
import ru.mobnius.vote.data.manager.packager.MetaSize;
import ru.mobnius.vote.data.manager.packager.PackageUtil;
import ru.mobnius.vote.data.storage.DbOpenHelper;
import ru.mobnius.vote.data.storage.models.DaoMaster;
import ru.mobnius.vote.data.storage.models.DaoSession;
import ru.mobnius.vote.utils.AuditUtils;
import ru.mobnius.vote.utils.HardwareUtil;

public class MobniusApplication extends android.app.Application implements IExceptionIntercept, OnNetworkChangeListener, ISocketNotification{
    private ServiceManager serviceManager;
    private List<OnNetworkChangeListener> mNetworkChangeListener;
    private List<ISocketNotification> mSocketNotificationListener;
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
     */
    public void onAuthorized() {
        BasicCredentials credentials = Authorization.getInstance().getUser().getCredentials();
        PreferencesManager.createInstance(this, credentials.login);
        FileManager fileManager = FileManager.createInstance(credentials, this);

        // создаем директории для хранения изображений
        File dir = fileManager.getTempPictureFolder();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        dir = fileManager.getAttachmentsFolder();
        if (!dir.exists()) {
            dir.mkdirs();
        }

        DaoSession daoSession = new DaoMaster(new DbOpenHelper(this, credentials.login + ".db").getWritableDb()).newSession();

        // enable debug for SQL Queries
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;

        DataManager.createInstance(daoSession);

        AuditUtils.write(this, "", AuditUtils.ON_AUTH, AuditUtils.Level.HIGH);
        if (SocketManager.getInstance() != null)
            SocketManager.getInstance().destroy();

        SocketManager socketManager = SocketManager.createInstance(getBaseUrl(), credentials, HardwareUtil.getIMEI(this));
        socketManager.open(this);

        serviceManager.startMyService();

        ExceptionUtils.saveLocalException(this, Authorization.getInstance().getUser().getUserId(), daoSession);
    }

    /**
     * пользователь сбросил авторизацию
     *
     * @param clearUserAuthorization очистка авторизации пользователя
     */
    public void unAuthorized(boolean clearUserAuthorization) {

        AuditUtils.write(this, "", AuditUtils.UN_AUTH, AuditUtils.Level.HIGH);

        serviceManager.stopMyService();

        if (clearUserAuthorization) {
            Authorization.getInstance().destroy();
        } else {
            Authorization.getInstance().reset();
        }
        FileManager.getInstance().destroy();
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

    /**
     * Подписаться для добавления обработчика. Делать это в событии onStart
     *
     * @param notification обработчик
     */
    public void addNotificationListener(ISocketNotification notification) {
        if (mSocketNotificationListener == null) {
            mSocketNotificationListener = new ArrayList<>();
        }

        mSocketNotificationListener.add(notification);
    }

    /**
     * Подписаться для удаление обработчика. Делать это в событии onStop
     *
     * @param notification обработчик
     */
    public void removeNotificationListener(ISocketNotification notification) {
        if (mSocketNotificationListener != null) {
            mSocketNotificationListener.remove(notification);
        }
    }

    @Override
    public void onNotificationMessage(String type, byte[] buffer) {

        try {
            MetaSize metaSize = PackageUtil.readSize(buffer);
            if (metaSize.status == MetaSize.UN_DELIVERED) {
                // не доставлено
                onNotificationUnDelivered(buffer);
                return;
            }
            if (metaSize.status == MetaSize.DELIVERED) {
                // доставлено
                onNotificationDelivered(buffer);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        //StringMail mail = StringMail.getInstance(buffer);

        //NotificationManager notificationManager = new NotificationManager(Authorization.getInstance().getUser().getCredentials().getToken());
        //notificationManager.sendMessage("Hello", "server", "");

        if (mSocketNotificationListener != null) {
            for (ISocketNotification notification : mSocketNotificationListener) {
                notification.onNotificationMessage(type, buffer);
            }
        }
    }

    @Override
    public void onNotificationDelivered(byte[] buffer) {
        if (mSocketNotificationListener != null) {
            for (ISocketNotification notification : mSocketNotificationListener) {
                notification.onNotificationDelivered(buffer);
            }
        }
    }

    @Override
    public void onNotificationUnDelivered(byte[] buffer) {
        if (mSocketNotificationListener != null) {
            for (ISocketNotification notification : mSocketNotificationListener) {
                notification.onNotificationUnDelivered(buffer);
            }
        }
    }
}
