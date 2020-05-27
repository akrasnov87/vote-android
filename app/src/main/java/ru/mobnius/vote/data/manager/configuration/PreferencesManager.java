package ru.mobnius.vote.data.manager.configuration;

import android.content.Context;

public class PreferencesManager extends AbstractPreferencesManager {

    // фильтр
    private static final String ROUTE_FILTER_PREFS = "ROUTE_FILTER_PREFS";

    // сортировка
    private static final String POINT_SORT_PREFS = "POINT_SORT_PREFS";
    public final static String SERVER_APP_VERSION = "SERVER_APP_VERSION";
    public final static boolean ZIP_CONTENT = true;
    public final static String SYNC_PROTOCOL = "v1";
    public final static String MAILER_PROTOCOL = "v1";
    public final static String APP_VERSION = "MBL_APP_VERSION";
    public static final String DEBUG = "MBL_DEBUG";
    public static final String GENERATED_ERROR = "MBL_GENERATED_ERROR";
    public static final String PIN = "MBL_PIN";
    public static final String MBL_GEO_CHECK = "MBL_GEO_CHECK";

    public static final String MBL_BG_SYNC_INTERVAL = "MBL_BG_SYNC_INTERVAL";
    public static final String MBL_TRACK_INTERVAL = "MBL_TRACK_INTERVAL";
    public static final String MBL_TELEMETRY_INTERVAL = "MBL_TELEMETRY_INTERVAL";

    private static PreferencesManager preferencesManager;
    public static PreferencesManager getInstance(){
        return preferencesManager;
    }
    public static void createInstance(Context context, String preferenceName){
        preferencesManager = new PreferencesManager(context, preferenceName);
    }

    private PreferencesManager(Context context, String preferenceName){
        super(context, preferenceName);
    }

    public boolean isDebug() {
        return getDefaultBooleanValue(DEBUG);
    }

    public void setDebug(boolean value) {
        getSharedPreferences().edit().putBoolean(DEBUG, value).apply();
    }

    public boolean isPinAuth() {
        return getDefaultBooleanValue(PIN);
    }

    public void setPinAuth(boolean value) {
        getSharedPreferences().edit().putBoolean(PreferencesManager.PIN, value).apply();
    }

    public boolean getFilter() {
        return getDefaultBooleanValue(ROUTE_FILTER_PREFS);
    }

    public boolean getSort() {
        return getDefaultBooleanValue(POINT_SORT_PREFS);
    }

    public boolean isGeoCheck() { return getDefaultBooleanValue(MBL_GEO_CHECK); }

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

    public int getSyncInterval() {
        return getDefaultIntValue(MBL_BG_SYNC_INTERVAL);
    }

    public int getTrackingInterval() {
        return getDefaultIntValue(MBL_TRACK_INTERVAL);
    }

    public int getTelemetryInterval() {
        return getDefaultIntValue(MBL_TELEMETRY_INTERVAL);
    }
}
