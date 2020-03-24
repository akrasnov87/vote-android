package ru.mobnius.vote.data.manager.synchronization;

/**
 * Статус завершения синхронизации
 */
public enum FinishStatus {
    /**
     * неизвестно
     */
    NONE,
    /**
     * Успешно
     */
    SUCCESS,
    /**
     * Завершено с ошибкой
     */
    FAIL
}
