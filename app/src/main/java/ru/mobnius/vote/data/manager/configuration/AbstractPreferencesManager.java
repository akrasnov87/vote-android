package ru.mobnius.vote.data.manager.configuration;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.mobnius.vote.data.Logger;

public abstract class AbstractPreferencesManager {

    private SharedPreferences sharedPreferences;

    AbstractPreferencesManager(Context context, String preferenceName) {
        sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
    }

    SharedPreferences getSharedPreferences(){
        return sharedPreferences;
    }

    /**
     * обновление настроек
     * @param configurationSettings массив настроке
     * @return true - настройки обновлены
     */
    public boolean updateSettings(List<ConfigurationSetting> configurationSettings) {
        boolean refresh = false;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        for(ConfigurationSetting configurationSetting : configurationSettings) {
            try {
                if (configurationSetting.type.equals(ConfigurationSetting.INTEGER)) {
                    if(isUpdateInt(configurationSetting.key, Integer.parseInt(configurationSetting.value))){
                        editor.putInt(configurationSetting.key, Integer.parseInt(configurationSetting.value));
                        refresh = true;
                    }
                } else if (configurationSetting.type.equals(ConfigurationSetting.BOOLEAN)) {
                    if(isUpdateBoolean(configurationSetting.key, Boolean.parseBoolean(configurationSetting.value))){
                        editor.putBoolean(configurationSetting.key, Boolean.parseBoolean(configurationSetting.value));
                        refresh = true;
                    }
                } else {
                    if(isUpdateString(configurationSetting.key, configurationSetting.value)){
                        editor.putString(configurationSetting.key, configurationSetting.value);
                        refresh = true;
                    }
                }
            }catch (Exception e){
                Logger.error("Ошибка применения настроек", e);
            }
        }
        editor.apply();
        return refresh;
    }

    /**
     * Чтение настроек из shared preferences
     * @return массив настроек
     */
    public List<ConfigurationSetting> readSettings() {
        List<ConfigurationSetting> configurationSettings = new ArrayList<>();

        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            ConfigurationSetting stg = new ConfigurationSetting();
            stg.key = entry.getKey();
            try {
                stg.value = sharedPreferences.getString(entry.getKey(), null);
            }catch (ClassCastException ignore) {
                try {
                    stg.value = String.valueOf(sharedPreferences.getInt(entry.getKey(), Integer.MIN_VALUE));
                }catch (ClassCastException ignore2){
                    stg.value = String.valueOf(sharedPreferences.getBoolean(entry.getKey(), false));
                }
            }
            configurationSettings.add(stg);
        }

        return configurationSettings;
    }

    /**
     * доступна ли настройка
     * @param key ключ
     * @return true - настройка доступна
     */
    boolean hasValue(String key){
        return getSharedPreferences().contains(key);
    }

    private boolean isUpdateInt(String key, int value){
        int intValue = getIntValue(key, Integer.MIN_VALUE);
        return intValue != value;
    }

    private boolean isUpdateBoolean(String key, boolean value){
        boolean booleanValue = getBooleanValue(key, false);
        return booleanValue != value;
    }

    boolean isUpdateString(String key, String value){
        String stringValue = getStringValue(key, null);
        return stringValue == null || !stringValue.equals(value);
    }

    /**
     * Получение настройки
     * @param key ключ настройки
     * @param defaultValue значение по умолчанию
     * @return значение
     */
    public String getStringValue(String key, String defaultValue){
        return getSharedPreferences().getString(key, defaultValue);
    }

    /**
     * Получение настройки
     * @param key ключ настройки
     * @param defaultValue значение по умолчанию
     * @return значение
     */
    public boolean getBooleanValue(String key, boolean defaultValue) {
        return getSharedPreferences().getBoolean(key, defaultValue);
    }

    /**
     * Получение настройки
     * @param key ключ настройки
     * @param defaultValue значение по умолчанию
     * @return значение
     */
    public int getIntValue(String key, int defaultValue){
        return getSharedPreferences().getInt(key, defaultValue);
    }

    private boolean hasDefaultValue(String key){
        return DefaultPreferencesManager.getInstance().hasValue(key);
    }

    boolean getDefaultBooleanValue(String key){
        if(hasDefaultValue(key)){
            return DefaultPreferencesManager.getInstance().getBooleanValue(key, false);
        }

        return PreferencesManager.getInstance().getBooleanValue(key, false);
    }

    int getDefaultIntValue(String key) {
        if(hasDefaultValue(key)) {
            return DefaultPreferencesManager.getInstance().getIntValue(key, -1);
        }

        return PreferencesManager.getInstance().getIntValue(key, -1);
    }

    String getDefaultStringValue(String key) {
        if(hasDefaultValue(key)) {
            return DefaultPreferencesManager.getInstance().getStringValue(key, "");
        }

        return PreferencesManager.getInstance().getStringValue(key, "");
    }

    /**
     * Очистка настроек
     */
    public void clear() {
        if(sharedPreferences != null) {
            sharedPreferences.edit().clear().apply();
        }
    }

    public void destroy() {
        sharedPreferences = null;
    }
}
