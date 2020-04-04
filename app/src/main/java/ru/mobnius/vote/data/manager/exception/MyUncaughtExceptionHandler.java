package ru.mobnius.vote.data.manager.exception;

import android.content.Context;
import android.util.Log;

import java.util.Date;
import java.util.Objects;

import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.utils.StringUtil;

public class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    /**
     * был перехвачен
     */
    private static boolean intercept = false;
    private static final String TAG = "EXCEPTION_LOG";

    private final Thread.UncaughtExceptionHandler oldHandler;

    private final String group;
    private final int code;
    private Context mContext;

    /**
     *
     * @param oldHandler
     * @param group группа исключения IExceptionGroup
     * @param code код исключения IExceptionCode
     */
    public MyUncaughtExceptionHandler(Thread.UncaughtExceptionHandler oldHandler, String group, int code, Context context) {
        this.oldHandler = oldHandler;
        this.group = group;
        this.code = code;
        mContext = context;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        try {
            if(!intercept) {
                Log.d(TAG, "Перехвачено исключение от группы " + group + ", код " + ExceptionUtils.codeToString(code));
                String exceptionString = StringUtil.exceptionToString(e);
                ExceptionModel exceptionModel = ExceptionModel.getInstance(new Date(), exceptionString, group, code);
                boolean isDebug = PreferencesManager.getInstance() != null && PreferencesManager.getInstance().isDebug();
                Log.d(TAG, "Запись исключения " + exceptionModel.getExceptionCode(isDebug) + " в файл.");
                FileExceptionManager.getInstance(mContext).writeBytes(exceptionModel.getFileName(), exceptionModel.toString().getBytes());
                Log.d(TAG, "Исключение " + exceptionModel.getExceptionCode(isDebug) + " записано в файл.");
            }
        }catch (Exception exc) {
            intercept = false;
            Log.d(TAG, Objects.requireNonNull(exc.getMessage()));
        }finally {
            intercept = true;
            if (oldHandler != null) {
                oldHandler.uncaughtException(t, e); //Delegates to Android's error handling
            } else {
                System.exit(2); //Prevents the service/app from freezing
            }
        }
    }
}
