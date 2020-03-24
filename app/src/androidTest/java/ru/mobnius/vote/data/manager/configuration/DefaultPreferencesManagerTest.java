package ru.mobnius.vote.data.manager.configuration;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class DefaultPreferencesManagerTest {
    private Context appContext;
    private DefaultPreferencesManager preferencesManager;
    private PreferencesManager mPreferencesManager;

    @Before
    public void setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        preferencesManager = new DefaultPreferencesManager(appContext, "default");
        preferencesManager.clear();
    }

    @Test
    public void updateSettingsTest(){
        List<ConfigurationSetting> configurationSettings = new ArrayList<>();
        ConfigurationSetting configurationSetting = new ConfigurationSetting();
        configurationSetting.key = "MBL_C_VALUE";
        configurationSetting.value = "value";
        configurationSetting.type = ConfigurationSetting.TEXT;
        configurationSetting.summary = "";
        configurationSetting.label = "";

        configurationSettings.add(configurationSetting);

        ConfigurationSetting configurationSetting2 = new ConfigurationSetting();
        configurationSetting2.key = "MBL_N_VALUE";
        configurationSetting2.value = "1";
        configurationSetting2.summary = "";
        configurationSetting2.type = ConfigurationSetting.INTEGER;
        configurationSetting2.label = "";

        configurationSettings.add(configurationSetting2);

        ConfigurationSetting configurationSetting3 = new ConfigurationSetting();
        configurationSetting3.key = "MBL_B_VALUE";
        configurationSetting3.value = "true";
        configurationSetting3.summary = "";
        configurationSetting3.type = ConfigurationSetting.BOOLEAN;
        configurationSetting3.label = "";

        configurationSettings.add(configurationSetting3);

        boolean refresh = preferencesManager.updateSettings(configurationSettings);
        Assert.assertTrue(refresh);

        List<ConfigurationSetting> list = preferencesManager.readSettings();
        ConfigurationSetting set = null;
        for(ConfigurationSetting s: list){
            if(s.key.equals("MBL_C_VALUE")){
                set = s;
            }
        }

        Assert.assertEquals(set.value, "value");

        Assert.assertEquals(preferencesManager.hasValue(configurationSetting.key), true);
        Assert.assertEquals(preferencesManager.hasValue("FAKE"), false);

        Assert.assertEquals(preferencesManager.isUpdateString("MBL_C_VALUE", "value"), false);
        Assert.assertEquals(preferencesManager.isUpdateString("MBL_C_VALUE", "value2"), true);

        Assert.assertEquals(preferencesManager.getIntValue("MBL_N_VALUE", 0), 1);
        Assert.assertEquals(preferencesManager.getIntValue("MBL_N_VALUE2", 1), 1);

        Assert.assertEquals(preferencesManager.getStringValue("MBL_C_VALUE", ""), "value");
        Assert.assertEquals(preferencesManager.getBooleanValue("MBL_B_VALUE", false), true);
        preferencesManager.clear();
    }

    @After
    public void downUp() {
        preferencesManager.clear();
    }
}