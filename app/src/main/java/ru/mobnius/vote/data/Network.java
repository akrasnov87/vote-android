package ru.mobnius.vote.data;

/**
 * Класс для работы с состоянием сети
 */
public class Network {

    public Network(boolean onLine, boolean socket) {
        this.onLine = onLine;
        this.socket = socket;
    }

    /**
     * доступ к сети интернета
     */
    public boolean onLine;

    /**
     * доступно сокет соединение или нет
     */
    public boolean socket;
}
