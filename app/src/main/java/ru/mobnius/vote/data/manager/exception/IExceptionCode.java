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
     * синхронизация
     */
    int SYNCHRONIZATION = 10;
    /**
     * Форма акта снятия контрольного показания
     */
    int QUESTION = 27;
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
    int RATING = 41;
    int FEEDBACK = 42;
    int STATISTIC_DIALOG = 43;
    int RATING_DIALOG = 44;
    int VOTING_DIALOG = 45;
    int CONTACT_LIST = 46;
    int MY_CONTACT_ITEM = 47;
    int NOTIFICATION = 48;
    int UPDATE_ABOUT = 49;
    int HELP = 50;
}
