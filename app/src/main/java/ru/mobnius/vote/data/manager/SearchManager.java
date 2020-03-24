package ru.mobnius.vote.data.manager;



import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

import ru.mobnius.vote.ui.model.FilterItem;

/**
 * управление фильтрами
 */
public abstract class SearchManager<T> extends FilterManager<T> {

    /**
     * Конструктор
     *
     * @param key ключ
     */
    public SearchManager(String key) {
        super(key);
    }

    /**
     * Конструктор
     *
     * @param key         ключ
     * @param deSerialize строка для обработки
     */
    public SearchManager(String key, String deSerialize) {
        super(key, deSerialize);
    }

    /**
     * Удовлетворяет ли условию фильтрации запись или нет
     *
     * @param item     элемент массива
     * @return true - подходит под фильтр
     */
    protected boolean isAppend(T item) {
        boolean append = false;

        try {
            for (FilterItem filterItem : getItems()) {
                Field field = item.getClass().getField(filterItem.getName());

                String valueStr = String.valueOf(field.get(item)).toLowerCase();
                String valueFilter = filterItem.getValue().toLowerCase();
                if (valueStr.contains(valueFilter)) {
                    append = true;
                    break;
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
}
