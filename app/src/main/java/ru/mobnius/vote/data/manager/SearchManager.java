package ru.mobnius.vote.data.manager;



import java.lang.reflect.Field;

import ru.mobnius.vote.ui.model.FilterItem;

/**
 * управление фильтрами
 */
public abstract class SearchManager<T> extends FilterManager<T> {

    protected SearchManager() {
        super();
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
                Field field = item.getClass().getDeclaredField(filterItem.getName());

                String valueStr = String.valueOf(field.get(item)).toLowerCase();
                String valueFilter = filterItem.getValue().toLowerCase();
                if (valueStr.contains(valueFilter)) {
                    append = true;
                    break;
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
        return append;
    }
}
