package ru.mobnius.vote.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.util.Date;

import ru.mobnius.vote.data.manager.exception.ExceptionModel;
import ru.mobnius.vote.data.manager.exception.FileExceptionManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.manager.exception.IExceptionGroup;
import ru.mobnius.vote.utils.StringUtil;

/**
 * Запись и хранение логов
 */
public class Logger {
    /**
     * тег для поиска логов
     */
    private final static String TAG = "MyLogs";

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    public static void setContext(Context context) {
        sContext = context;
    }

    /**
     * Запись ошибки в лог
     * @param e ошибка
     */
    public static void error(Exception e) {
        error("", e);
    }

    /**
     * Запись ошибки с описанием
     * @param description описание ошибки
     * @param e ошибка
     */
    public static void error(String description, Exception e) {
        String exceptionString = StringUtil.exceptionToString(e) + description;
        Log.d(TAG, exceptionString);
        ExceptionModel exceptionModel = ExceptionModel.getInstance(new Date(), exceptionString, IExceptionGroup.NONE, IExceptionCode.ALL);
        FileExceptionManager.getInstance(sContext).writeBytes(exceptionModel.getFileName(), exceptionModel.toString().getBytes());
    }
}