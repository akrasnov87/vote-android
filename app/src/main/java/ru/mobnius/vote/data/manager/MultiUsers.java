package ru.mobnius.vote.data.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.File;

import static android.content.Context.MODE_PRIVATE;

/**
 * Служебный класс для работы с приложением несколькими пользователями
 */
class MultiUsers {
    private final Context mContext;

    public MultiUsers(Context context) {
        mContext = context;
    }

    /**
     * Имя БД
     *
     * @param login логин
     * @return имя БД
     */
    public String getDatabaseName(String login) {
        return String.format("%s.db", login);
    }

    /**
     * Настройки пользователя
     *
     * @param login логин
     * @return настройки
     */
    public SharedPreferences getPreferenceName(String login) {
        return mContext.getSharedPreferences(login, MODE_PRIVATE);
    }

    /**
     * Файловый каталог для хранения данных
     *
     * @param login  логин
     * @param subDir директория
     * @return каталог
     */
    public File getCatalog(String login, String subDir) {
        return new File(Environment.getExternalStorageDirectory(), login + "/" + subDir);
    }
}
