package ru.mobnius.vote.data.manager;


import org.json.JSONException;
import org.json.JSONObject;

import ru.mobnius.vote.ui.model.FilterItem;

/**
 * управление фильтрами
 */
public abstract class FilterManager<T> extends ItemsManager<FilterItem, String> {

    /**
     * Конструктор
     *
     * @param key ключ
     */
    FilterManager(String key) {
        super(key);
    }

    /**
     * Конструктор
     *
     * @param key         ключ
     * @param deSerialize строка для обработки
     */
    FilterManager(String key, String deSerialize) {
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

    @Override
    protected FilterItem getItemObject(JSONObject jsonObject) throws JSONException {
        return new FilterItem(jsonObject.getString("mName"), jsonObject.getString("mType"), jsonObject.getString("mValue"));
    }

    @Override
    public FilterItem[] getItems() {
        return mItems.toArray(new FilterItem[0]);
    }

    interface IFilterCallback<T> {
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
