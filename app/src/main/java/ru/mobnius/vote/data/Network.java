package ru.mobnius.vote.data;

/**
 * Класс для работы с состоянием сети
 */
public class Network {

    public Network(boolean onLine, boolean socket) {
        this.onLine = onLine;
        /**
         * доступно сокет соединение или нет
         */
    }

    /**
     * доступ к сети интернета
     */
    public final boolean onLine;

}
