package ru.mobnius.vote.data.manager.exception;

public interface IExceptionCode {
    /**
     * Ошибка на уровне приложения.
     * Не была перехвачено ни кем
     */
    int ALL = 666;

    /**
     * Общее
     */
    int MAIN = 0;

    /**
     * Авторизация
     */
    int LOGIN = 3;

    /**
     * Информация о точке
     */
    int POINT_INFO = 7;

    /**
     * Список точек
     */
    int POINTS = 8;

    /**
     * Маршруты
     */
    int ROUTES = 9;

    /**
     * синхронизация
     */
    int SYNCHRONIZATION = 10;

    /**
     * Синхронизация в сервисе
     */
    int SYNC_IN_SERVICE = 11;

    /**
     * Сохранение телеметрии
     */
    int SAVE_TELEMETRY = 12;

    /**
     * хранение трекинга
     */
    int SAVE_TRACKING = 13;

    /**
     * Форма акта снятия контрольного показания
     */
    int CONTROL_METER_READINGS = 27;

    /**
     * Добавление фильтров
     */
    int FILTER = 30;
    /**
     * Сортировка маршрутов
     */
    int ROUTE_SORT = 32;
    /**
     * Сортировка заданий
     */
    int POINT_SORT = 33;
    /**
     * Ввод пин-кода
     */
    int PIN_CODE = 34;

    int VOTE_ITEM = 35;
    int SETTING = 36;
    int SERVICE = 37;
    int STATISTIC = 38;
    int COMMENT_DIALOG = 39;
    int CONTACT_DIALOG = 40;
}
