package ru.mobnius.vote.data.manager;


import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Date;

import ru.mobnius.vote.data.manager.configuration.ConfigurationSetting;
import ru.mobnius.vote.ui.model.FilterItem;
import ru.mobnius.vote.utils.DateUtil;

/**
 * управление фильтрами
 */
public abstract class FilterManager<T> extends ItemsManager<FilterItem, String> {

    /**
     * Конструктор
     *
     * @param key ключ
     */
    public FilterManager(String key) {
        super(key);
    }

    /**
     * Конструктор
     *
     * @param key         ключ
     * @param deSerialize строка для обработки
     */
    public FilterManager(String key, String deSerialize) {
        super(key, deSerialize);
    }

    /**
     * Обновление фильтра
     *
     * @param key   наименование
     * @param value значение
     */
    @Override
    public void updateItem(String key, String value) {
        FilterItem filterItem = getItem(key);
        if (filterItem != null) {
            filterItem.setValue(value);
        }
    }

    /**
     * Фильтрация данных
     *
     * @param items массив данных
     * @return результат
     */
    public abstract T[] toFilters(T[] items);

    /**
     * Удовлетворяет ли условию фильтрации запись или нет
     *
     * @param item     элемент массива
     * @param callback функция обратного вызова
     * @return true - подходит под фильтр
     */
    protected boolean isAppend(T item, IFilterCallback<T> callback) {
        boolean append = true;

        try {
            for (FilterItem filterItem : getItems()) {
                Field field = item.getClass().getField(filterItem.getName());

                switch (filterItem.getType()) {
                    case ConfigurationSetting.INTEGER:
                        int valueInt = Integer.parseInt(String.valueOf(field.get(item)));
                        if (valueInt != Integer.parseInt(filterItem.getValue())) {
                            append = false;
                        }
                        break;

                    case ConfigurationSetting.BOOLEAN:
                        boolean valueBool = Boolean.parseBoolean(String.valueOf(field.get(item)));
                        if (valueBool != Boolean.parseBoolean(filterItem.getValue())) {
                            append = false;
                        }
                        break;

                    case ConfigurationSetting.REAL:
                        double valueDouble = Double.parseDouble(String.valueOf(field.get(item)));
                        if (valueDouble != Double.parseDouble(filterItem.getValue())) {
                            append = false;
                        }
                        break;

                    case ConfigurationSetting.DATE:
                        Date valueDate = (Date) field.get(item);
                        try {
                            assert valueDate != null;
                            if (valueDate.getTime() != DateUtil.convertStringToDate(filterItem.getValue()).getTime()) {
                                append = false;
                            }
                        } catch (ParseException ignore) {
                            append = false;
                        }
                        break;

                    default:
                        String valueStr = String.valueOf(field.get(item)).toLowerCase();
                        if (!valueStr.contains(filterItem.getValue())) {
                            append = false;
                        }
                        break;
                }
                if (callback != null) {
                    append = callback.onFilter(filterItem, item, append);
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
            append = false;
        }
        return append;
    }

    @Override
    protected FilterItem getItemObject(JSONObject jsonObject) throws JSONException {
        return new FilterItem(jsonObject.getString("mName"), jsonObject.getString("mType"), jsonObject.getString("mValue"));
    }

    @Override
    public FilterItem[] getItems() {
        return mItems.toArray(new FilterItem[0]);
    }

    public interface IFilterCallback<T> {
        /**
         * Обработчик фильтрации записи
         *
         * @param filterItem   фильтр
         * @param item         запись
         * @param appendStatus результат добавления
         * @return переназначенный результат добавления
         */
        boolean onFilter(FilterItem filterItem, T item, boolean appendStatus);
    }
}
