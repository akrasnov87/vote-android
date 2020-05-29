package ru.mobnius.vote.data.manager.exception;

/**
 * Группы ошибок
 */
public interface IExceptionGroup {
    /**
     * Интерфейс
     */
    String USER_INTERFACE = "UI";
    /**
     * Синхронизация
     */
    String SYNCHRONIZATION = "SYNC";
    /**
     * Настройки
     */
    String SETTING = "STG";
    /**
     * Фоновые службы
     */
    String SERVICE = "SRV";

    /**
     * На уровне всего приложения
     */
    String APPLICATION = "APP";

    /**
     * Диалоговые окна
     */
    String DIALOG = "UI_DLG";

    /**
     * Неизвестно
     */
    String NONE = "NONE";
}
