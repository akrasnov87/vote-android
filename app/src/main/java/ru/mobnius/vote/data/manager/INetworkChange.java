package ru.mobnius.vote.data.manager;

public interface INetworkChange {

    /**
     * Обработчик изменения сети
     * @param online приложение в онлайн
     * @param serverExists подключение к серверу доступно.
     */
    // TODO 28/01/2020 доступность подключения к серверу не определяется автоматически только при измеении подключения к интернету
    void onNetworkChange(boolean online, boolean serverExists);
}
