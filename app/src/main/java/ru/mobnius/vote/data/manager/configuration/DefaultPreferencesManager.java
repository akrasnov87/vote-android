package ru.mobnius.vote.data.manager.configuration;

import android.content.Context;

/**
 * Менеджер настроек по умолчанию для всего приложения
 */
public class DefaultPreferencesManager extends AbstractPreferencesManager {

    public final static String NAME = "__default";

    private static DefaultPreferencesManager preferencesManager;

    public static DefaultPreferencesManager getInstance() {
        return preferencesManager;
    }

    public static void createInstance(Context context, String preferenceName){
        preferencesManager = new DefaultPreferencesManager(context, preferenceName);
    }

    protected DefaultPreferencesManager(Context context, String preferenceName){
        super(context, preferenceName);
    }
}
