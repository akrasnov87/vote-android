package ru.mobnius.vote.data.manager;

public interface ISocketNotification {
    /**
     * Обработчик сообщений
     * @param type тип сообщения
     * @param buffer сообщение
     */
    void onNotificationMessage(String type, byte[] buffer);

    /**
     * Обработчик. Сообщение доставлено
     * @param buffer сообщение
     */
    void onNotificationDelivered(byte[] buffer);

    /**
     * Обработчик. Сообщение не доставлено
     * @param buffer сообщение
     */
    void onNotificationUnDelivered(byte[] buffer);
}
