package ru.mobnius.vote.data;

import android.util.Log;

/**
 * Запись и хранение логов
 */
public class Logger {
    /**
     * тег для поиска логов
     */
    private final static String TAG = "MyLogs";

    /**
     * Запись ошибки в лог
     * @param e ошибка
     */
    public static void error(Exception e){
        e.printStackTrace();
    }

    /**
     * Запись ошибки с описанием
     * @param description описание ошибки
     * @param e ошибка
     */
    public static void error(String description, Exception e){
        e.printStackTrace();
    }

    /**
     * Информация для отладки
     * @param msg текст
     */
    public static void debug(String msg){
        Log.d(TAG, msg);
    }
}