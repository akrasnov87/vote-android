package ru.mobnius.vote.data.manager.configuration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ru.mobnius.vote.data.manager.MobniusApplication;
import ru.mobnius.vote.data.manager.RequestManager;
import ru.mobnius.vote.data.manager.credentials.BasicCredentials;
import ru.mobnius.vote.data.manager.rpc.RPCResult;
import ru.mobnius.vote.data.manager.rpc.SingleItemQuery;

public class ConfigurationSettingUtil {
    public final static String ACTION = "setting";
    public final static String METHOD = "getSettings";

    /**
     * Преобразование значения настройки в Integer
     * @param configurationSetting настройка
     */
    public static Integer getIntegerValue(ConfigurationSetting configurationSetting) {
        if(configurationSetting != null && configurationSetting.type.equals(ConfigurationSetting.INTEGER)) {
            try {
                return Integer.parseInt(configurationSetting.value);
            }catch (NumberFormatException ignore) {
                return null;
            }
        }
        return null;
    }

    /**
     * Преобразование значения настройки в Double
     * @param configurationSetting настройка
     */
    public static Double getDoubleValue(ConfigurationSetting configurationSetting) {
        if(configurationSetting != null && configurationSetting.type.equals(ConfigurationSetting.REAL)) {
            try {
                return Double.parseDouble(configurationSetting.value);
            }catch (NumberFormatException ignore) {
                return null;
            }
        }
        return null;
    }

    /**
     * Преобразование значения настройки в String
     * @param configurationSetting настройка
     */
    public static String getStringValue(ConfigurationSetting configurationSetting) {
        if(configurationSetting != null && configurationSetting.type.equals(ConfigurationSetting.TEXT)) {
            return configurationSetting.value;
        }
        return null;
    }

    /**
     * Преобразование значения настройки в Boolean
     * @param configurationSetting настройка
     */
    public static Boolean getBooleanValue(ConfigurationSetting configurationSetting) {
        if(configurationSetting != null && configurationSetting.value != null && configurationSetting.type.equals(ConfigurationSetting.BOOLEAN)) {
            switch (configurationSetting.value.toLowerCase()) {
                case "0":
                case "false":
                    return false;

                case "1":
                case "true":
                    return true;
            }
        }
        return null;
    }

    /**
     * Чтение списка настроек из результата запроса
     * @param result результат запроса
     * @return список настроек
     */
    public static List<ConfigurationSetting> getConfigurationSettings(JSONObject result) {
        if(result == null) {
            return null;
        }
        List<ConfigurationSetting> configurationSettings = new ArrayList<>();

        for (Iterator<String> it = result.keys(); it.hasNext(); ) {
            String key = it.next();

            try {
                ConfigurationSetting configurationSetting = new ConfigurationSetting();

                JSONObject jsonObject = result.getJSONObject(key);

                for (Iterator<String> item = jsonObject.keys(); item.hasNext(); ) {
                    String name = item.next();
                    String val = (String) jsonObject.get(name);
                    switch (name) {
                        case "key":
                            configurationSetting.key = val;
                            break;

                        case "value":
                            configurationSetting.value = val;
                            break;

                        case "label":
                            configurationSetting.label = val;
                            break;

                        case "summary":
                            configurationSetting.summary = val;
                            break;

                        case "type":
                            configurationSetting.type = val;
                            break;
                    }
                }

                configurationSettings.add(configurationSetting);
            }catch (JSONException ignore) {

            }
        }

        if(configurationSettings.size() > 0){
            return configurationSettings;
        }
        return null;
    }

    /**
     * Получение настроек от сервера
     * @return Возвращается список настроек
     */
    public static List<ConfigurationSetting> getSettings(BasicCredentials credentials) {
        String[] params = new String[1];
        params[0] = "MBL";
        try {
            RPCResult[] results = RequestManager.rpc(MobniusApplication.getBaseUrl(), credentials.getToken(), ConfigurationSettingUtil.ACTION, ConfigurationSettingUtil.METHOD, new SingleItemQuery(params));
            if(results[0].isSuccess()) {
                return getConfigurationSettings(results[0].result.records[0]);
            }
        }catch (IOException ignore) {

        }
        return null;
    }
}
