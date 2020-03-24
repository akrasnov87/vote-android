package ru.mobnius.vote.data;

import android.app.Service;

import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.exception.IExceptionGroup;
import ru.mobnius.vote.data.manager.exception.IExceptionIntercept;
import ru.mobnius.vote.data.manager.exception.MyUncaughtExceptionHandler;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.credentials.BasicUser;
import ru.mobnius.vote.data.storage.models.DaoSession;

/**
 * базовый сервис
 */
public abstract class BaseService extends Service implements IExceptionIntercept {
     /**
     * Объект для работы с БД Sqlite
     * @return возвращается объект DaoSession
     */
    protected DaoSession getDaoSession(){
        return DataManager.getInstance().getDaoSession();
    }

    /**
     * Текущий авторизованный пользователь
     * @return возвращается текущий авторизованный пользователь
     */
    protected BasicUser getBasicUser() {
        return Authorization.getInstance().getUser();
    }

    /**
     * Обработчик перехвата ошибок
     */
    public void onExceptionIntercept() {
        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler(), getExceptionGroup(), getExceptionCode(), this));
    }

    /**
     * Группа ошибки из IExceptionGroup
     * @return строка
     */
    public String getExceptionGroup(){
        return IExceptionGroup.SERVICE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        onExceptionIntercept();
    }
}
