package ru.mobnius.vote.data.manager;

public interface OnNetworkChangeListener {

    /**
     * Обработчик изменения сети
     * @param online приложение в онлайн
     * @param serverExists подключение к серверу доступно.
     * @param isFast соединение достаточно быстрое
     */
    // TODO 28/01/2020 доступность подключения к серверу не определяется автоматически только при измеении подключения к интернету
    void onNetworkChange(boolean online, boolean serverExists, boolean isFast);
}
