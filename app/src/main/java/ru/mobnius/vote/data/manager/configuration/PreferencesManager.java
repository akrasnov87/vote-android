package ru.mobnius.vote.data.manager.configuration;

import android.content.Context;
import android.location.LocationManager;

public class PreferencesManager extends AbstractPreferencesManager {

    private static final String RATING_FILTER_PREFS = "RATING_FILTER_PREFS";

    // фильтр
    private static final String ROUTE_FILTER_PREFS = "ROUTE_FILTER_PREFS";

    // сортировка
    private static final String POINT_SORT_PREFS = "POINT_SORT_PREFS";
    public final static String SERVER_APP_VERSION = "SERVER_APP_VERSION";
    public static boolean ZIP_CONTENT = true;
    public final static String SYNC_PROTOCOL = "v1";
    public final static String APP_VERSION = "MBL_APP_VERSION";
    public static final String DEBUG = "MBL_DEBUG";
    public static final String GENERATED_ERROR = "MBL_GENERATED_ERROR";
    public static final String PIN = "MBL_PIN";
    public static final String MBL_GEO_CHECK = "MBL_GEO_CHECK";
    public static final String MBL_LOG = "MBL_LOG";
    public static final String MBL_LOCATION = "MBL_LOCATION";
    public static final String MBL_DISTANCE = "MBL_DISTANCE";
    public static final String MBL_LOG_LOW = "LOW";

    public static final String MBL_BG_SYNC_INTERVAL = "MBL_BG_SYNC_INTERVAL";
    public static final String MBL_TRACK_INTERVAL = "MBL_TRACK_INTERVAL";
    public static final String MBL_TELEMETRY_INTERVAL = "MBL_TELEMETRY_INTERVAL";

    public static final String MBL_FEEDBACK_ANSWER_COUNT = "MBL_FEEDBACK_ANSWER_COUNT";
    public static final String MBL_DOC = "MBL_DOC";
    public static final String MBL_COLOR_THEME = "MBL_COLOR_THEME";
    public static final String MBL_AUTO_SYNC = "MBL_AUTO_SYNC";
    public static final String MBL_AUTO_SYNC_INTERVAL = "MBL_AUTO_SYNC_INTERVAL";
    public static final String MBL_ZERO_PRIORITY = "MBL_ZERO_PRIORITY";

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

    /**
     * Кол-во ответов на обратную связь
     * @return
     */
    public int getFeedbackAnswerCount() {
        return getDefaultIntValue(MBL_FEEDBACK_ANSWER_COUNT);
    }

    /**
     * Сохранить текущее кол-во сообщений
     * @param value
     */
    public void setFeedbackAnswerCount(int value) {
        getSharedPreferences().edit().putInt(MBL_FEEDBACK_ANSWER_COUNT, value).apply();
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

    public boolean getRating() {
        return getDefaultBooleanValue(RATING_FILTER_PREFS);
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

    public void setRating(boolean value) {
        getSharedPreferences().edit().putBoolean(PreferencesManager.RATING_FILTER_PREFS, value).apply();
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
    public String getDoc() {
        return getStringValue(MBL_DOC, "https://sway.office.com/HVzTm1B3ddm2EOqj?ref=Link");
    }

    public String getLog() {
        return getDefaultStringValue(MBL_LOG);
    }

    public String getLocation() {
        return getDefaultStringValue(MBL_LOCATION).length() == 0 ? LocationManager.NETWORK_PROVIDER : getDefaultStringValue(MBL_LOCATION);
    }

    public int getDistance() {
        return getDefaultIntValue(MBL_DISTANCE) == -1 ? 100 : getDefaultIntValue(MBL_DISTANCE);
    }

    public boolean isSimpleColor(){
        return getDefaultBooleanValue(MBL_COLOR_THEME);
    }
    public void setSimpleColor(boolean value) {
        getSharedPreferences().edit().putBoolean(PreferencesManager.MBL_COLOR_THEME, value).apply();
    }

    public boolean isZeroPriority(){
        return getDefaultBooleanValue(MBL_ZERO_PRIORITY);
    }
    public void setZeroPriority(boolean value) {
        getSharedPreferences().edit().putBoolean(PreferencesManager.MBL_ZERO_PRIORITY, value).apply();
    }

    public boolean isAutoSync(){
        return getDefaultBooleanValue(MBL_AUTO_SYNC);
    }
    public void setAutoSync(boolean value) {
        getSharedPreferences().edit().putBoolean(PreferencesManager.MBL_AUTO_SYNC, value).apply();
    }

    public int getAutoSyncInterval() {
        return getDefaultIntValue(MBL_AUTO_SYNC_INTERVAL);
    }
}
