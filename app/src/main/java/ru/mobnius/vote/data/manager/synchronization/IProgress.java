package ru.mobnius.vote.data.manager.synchronization;

import ru.mobnius.vote.data.manager.synchronization.utils.transfer.ITransferStatusCallback;

/**
 * Интерфейс для отслеживания процесса синхронизации
 */
public interface IProgress extends ITransferStatusCallback {

    /**
     * Запуск синхронизации
     * @param synchronization объект синхронизации
     */
    void onStart(ISynchronization synchronization);

    /**
     * Отмена синхронизации
     * @param synchronization объект синхронизации
     */
    void onStop(ISynchronization synchronization);

    /**
     * Прогресс выполнения синхрониации
     * @param synchronization объект синхронизации
     * @param step шаг выполнения
     * @param message текстовое сообщения
     * @param tid Идентификатор транзакции
     */
    void onProgress(ISynchronization synchronization, int step, String message, String tid);

    /**
     * обработчсик ошибок
     * @param synchronization объект синхронизации
     * @param step шаг выполнения
     * @param message текстовое сообщения
     * @param tid Идентификатор транзакции
     */
    void onError(ISynchronization synchronization, int step, String message, String tid);
}
