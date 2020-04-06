package ru.mobnius.vote.data.manager.configuration;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class PreferencesManager extends AbstractPreferencesManager {

    // фильтр
    public static final String POINT_FILTER_PREFS = "POINT_FILTER_PREFS";
    public static final String ROUTE_FILTER_PREFS = "ROUTE_FILTER_PREFS";

    // сортировка
    public static final String ROUTE_SORT_PREFS = "ROUTE_SORT_PREFS";
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
        return getDefaultBooleanValue(DEBUG, false);
    }

    public boolean isPinAuth() {
        return getDefaultBooleanValue(PIN, false);
    }

    /**
     * Устновка фильтра
     * @param key ключ
     * @param serialize строка
     */
    public void setFilter(String key, String serialize) {
        List<ConfigurationSetting> configurationSettings = new ArrayList<>();
        configurationSettings.add(new ConfigurationSetting(key, serialize, ConfigurationSetting.TEXT));

        updateSettings(configurationSettings);
    }

    /**
     * Получение фильтра
     * @param key ключ фильтра
     * @return настройки фильтра
     */
    public String getFilter(String key) {
        return getStringValue(key,"{}");
    }

    /**
     * Установка сортировки
     * @param key ключ
     * @param serialize строка
     */
    public void setSort(String key, String serialize) {
        List<ConfigurationSetting> configurationSettings = new ArrayList<>();
        configurationSettings.add(new ConfigurationSetting(key, serialize, ConfigurationSetting.TEXT));

        updateSettings(configurationSettings);
    }

    /**
     * Получение сортировки
     * @param key ключ сортировки
     * @return настройки сортировки
     */
    public String getSort(String key) {
        return getStringValue(key,"{}");
    }

}
