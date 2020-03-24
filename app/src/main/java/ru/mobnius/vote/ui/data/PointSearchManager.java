package ru.mobnius.vote.ui.data;

import java.util.ArrayList;
import java.util.List;

import ru.mobnius.vote.data.manager.FilterManager;
import ru.mobnius.vote.data.manager.SearchManager;
import ru.mobnius.vote.ui.model.FilterItem;
import ru.mobnius.vote.ui.model.PointItem;

/**
 * Служебный класс для фильтрации точек маршрута
 */
public class PointSearchManager extends SearchManager<PointItem> {
    private static final String KEY = "";

    public PointSearchManager() {
        super(KEY);
    }

    public PointSearchManager(String deSerialize) {
        super(KEY, deSerialize);
    }

    @Override
    public PointItem[] toFilters(PointItem[] items) {
        List<PointItem> results = new ArrayList<>();
        for(PointItem item : items) {
            // тут специально используем сокращенный вариант
            if(isAppend(item)) {
                results.add(item);
            }
        }
        return results.toArray(new PointItem[0]);
    }

    public PointItem[] toFilters(PointItem[] items, String text) {

        mItems.clear();
        addItem(new FilterItem("deviceNumber", text));
        addItem(new FilterItem("address", text));
        addItem(new FilterItem("fio", text));
        addItem(new FilterItem("subscrNumber", text));

        List<PointItem> results = new ArrayList<>();
        for(PointItem item : items) {
            // тут специально используем сокращенный вариант
            if(isAppend(item)) {
                results.add(item);
            }
        }
        return results.toArray(new PointItem[0]);
    }
}
