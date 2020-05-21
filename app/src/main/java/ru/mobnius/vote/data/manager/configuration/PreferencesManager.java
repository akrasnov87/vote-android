package ru.mobnius.vote.data.manager.configuration;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class PreferencesManager extends AbstractPreferencesManager {

    // фильтр
    public static final String ROUTE_FILTER_PREFS = "ROUTE_FILTER_PREFS";

    // сортировка
    public static final String POINT_SORT_PREFS = "POINT_SORT_PREFS";

    public final static boolean ZIP_CONTENT = true;
    public final static String SYNC_PROTOCOL = "v1";
    public final static String MAILER_PROTOCOL = "v1";
    public final static String APP_VERSION = "MBL_APP_VERSION";
    public static final String DEBUG = "MBL_DEBUG";
    public static final String GENERATED_ERROR = "MBL_GENERATED_ERROR";
    public static final String PIN= "MBL_PIN";

    private static PreferencesManager preferencesManager;
    public static PreferencesManager getInstance(){
        return preferencesManager;
    }
    public static PreferencesManager createInstance(Context context, String preferenceName){
        return preferencesManager = new PreferencesManager(context, preferenceName);
    }

    protected PreferencesManager(Context context, String preferenceName){
        super(context, preferenceName);
    }

    public boolean isDebug() {
        return getDefaultBooleanValue(DEBUG);
    }

    public boolean isPinAuth() {
        return getDefaultBooleanValue(PIN);
    }

    public boolean getFilter() {
        return getDefaultBooleanValue(ROUTE_FILTER_PREFS);
    }

    public boolean getSort() {
        return getDefaultBooleanValue(POINT_SORT_PREFS);
    }

    /**
     * Устновка фильтра
     * @param value значение
     */
    public void setFilter(boolean value) {
        getSharedPreferences().edit().putBoolean(PreferencesManager.ROUTE_FILTER_PREFS, value).apply();
    }

    /**
     * Установка сортировки
     * @param value значение
     */
    public void setSort(boolean value) {
        getSharedPreferences().edit().putBoolean(PreferencesManager.POINT_SORT_PREFS, value).apply();
    }
}
