package ru.mobnius.vote.data.manager;


import ru.mobnius.vote.ui.model.FilterItem;

/**
 * управление фильтрами
 */
public abstract class FilterManager<T> extends ItemsManager<FilterItem> {

    FilterManager() {
        super();
    }

    /**
     * Фильтрация данных
     *
     * @param items массив данных
     * @return результат
     */
    public abstract T[] toFilters(T[] items);

    FilterItem[] getItems() {
        return mItems.toArray(new FilterItem[0]);
    }
}
