package ru.mobnius.vote.data.manager.configuration;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import ru.mobnius.vote.ManagerGenerate;
import ru.mobnius.vote.data.manager.rpc.RPCRecords;
import ru.mobnius.vote.data.manager.rpc.RPCResult;
import ru.mobnius.vote.data.manager.rpc.RPCResultMeta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ConfigurationSettingUtilTest extends ManagerGenerate {
    private RPCResult success;
    private RPCResult failed;

    @Before
    public void setUp() throws JSONException {
        List<ConfigurationSetting> configurationSettings = new ArrayList<>();
        ConfigurationSetting configurationSetting = new ConfigurationSetting();
        configurationSetting.key = "int";
        configurationSetting.value = "10";
        configurationSetting.type = ConfigurationSetting.INTEGER;
        configurationSettings.add(configurationSetting);

        configurationSetting = new ConfigurationSetting("real", "2.52", "label real", "summary real", ConfigurationSetting.REAL);
        configurationSettings.add(configurationSetting);

        configurationSetting = new ConfigurationSetting("text", "Hello", ConfigurationSetting.TEXT);
        configurationSettings.add(configurationSetting);

        configurationSetting = new ConfigurationSetting("boolean", "1", ConfigurationSetting.BOOLEAN);
        configurationSettings.add(configurationSetting);

        JSONObject[] array = new JSONObject[1];
        array[0] = new JSONObject();
        for(int i =0; i < 4; i++) {

            ConfigurationSetting item = configurationSettings.get(i);
            JSONObject jsonObject = new JSONObject();
            array[0].put(item.key, jsonObject);

            jsonObject.put("key", item.key);
            jsonObject.put("value", item.value);
            jsonObject.put("label", item.label);
            jsonObject.put("summary", item.summary);
            jsonObject.put("type", item.type);
        }

        success = new RPCResult();
        success.meta = new RPCResultMeta();
        success.meta.success = true;
        success.meta.msg = "";

        success.result = new RPCRecords();
        success.result.records = array;

        failed = new RPCResult();
        failed.meta = new RPCResultMeta();
        failed.meta.success = false;
        failed.meta.msg = "failed";
    }

    @Test
    public void getConfigurationSettings() {
        List<ConfigurationSetting> configurationSettings = ConfigurationSettingUtil.getConfigurationSettings(null);
        assertNull(configurationSettings);

        configurationSettings = ConfigurationSettingUtil.getConfigurationSettings(success.result.records[0]);
        assertEquals(configurationSettings.size(), 4);
    }

    @Test
    public void getSettings() {
        List<ConfigurationSetting> configurationSettings = ConfigurationSettingUtil.getSettings(getCredentials());
        assertTrue(configurationSettings.size() > 0);
    }
}
