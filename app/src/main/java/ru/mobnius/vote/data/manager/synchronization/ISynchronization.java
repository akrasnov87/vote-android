package ru.mobnius.vote.data.manager.synchronization;

import android.app.Activity;

import java.util.List;

import ru.mobnius.vote.data.storage.models.DaoSession;

/**
 * интерфейс механизма синхронизации
 */
public interface ISynchronization {
    /**
     * имя синхронизации
     * @return возвращается имя синхронизации
     */
    String getName();

    /**
     * идентификатор пользователя
     * @return идентификатор пользователя
     */
    long getUserID();

    /**
     * объект для подключения к БД
     * @return DaoSession
     */
    DaoSession getDaoSession();

    /**
     * статус завершения синхронизации
     * @return статус
     */
    FinishStatus getFinishStatus();

    /**
     * Список сущностей
     * @return возвращается список сущностей по которым разрешена отправка на сервер
     */
    List<ru.mobnius.vote.data.manager.synchronization.Entity> getEntityToList();

    /**
     * Список сущностей
     * @param tid идентификатор транзакции
     * @return возвращается список сущностей с tid
     */
    Entity[] getEntities(String tid);

    /**
     * Запуск на выполение
     * @param activity экран
     * @param progress результат выполнения
     */
    void start(Activity activity, IProgress progress);

    /**
     * Принудительная остановка выполнения
     */
    void stop();

    /**
     * обработчик ошибок
     * @param step шаг см. IProgressStep
     * @param e исключение
     * @param tid идентификатор транзакции
     */
    void onError(int step, Exception e, String tid);

    /**
     * обработчик ошибок
     * @param step шаг см. IProgressStep
     * @param message текстовое сообщение
     * @param tid идентификатор транзакции
     */
    void onError(int step, String message, String tid);
}
