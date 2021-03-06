package ru.mobnius.vote.ui.model;

import com.google.gson.annotations.Expose;

import ru.mobnius.vote.data.manager.configuration.ConfigurationSetting;

/**
 * Элемент фильтрации
 */
public class FilterItem {

    /**
     * Имя фильтра
     */
    @Expose
    private final String mName;

    /**
     * Тип фильтра. Выбирается один из параметров:
     * - ConfigurationSetting.INTEGER
     * - ConfigurationSetting.REAL
     * - ConfigurationSetting.TEXT
     * - ConfigurationSetting.BOOLEAN
     */
    @Expose
    private final String mType;

    /**
     * Значение
     */
    @Expose
    private String mValue;

    private FilterItem(String name, String type, String value) {
        mName = name;
        mType = type;
        mValue = value;
    }

    public FilterItem(String name, String value) {
        this(name, ConfigurationSetting.TEXT, value);
    }

    public String getName() {
        return mName;
    }

    public String getType() {
        return mType;
    }

    public String getValue() {
        if(mValue != null) {
            return mValue.toLowerCase();
        }
        return null;
    }

    public void setValue(String value) {
        mValue = value;
    }
}
