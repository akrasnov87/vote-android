package ru.mobnius.vote.ui.data;

import java.util.ArrayList;
import java.util.List;

import ru.mobnius.vote.data.manager.FilterManager;
import ru.mobnius.vote.ui.model.PointItem;

/**
 * Служебный класс для фильтрации точек маршрута
 */
public class PointFilterManager extends FilterManager<PointItem> {
    public PointFilterManager(String key) {
        super(key);
    }

    public PointFilterManager(String key, String deSerialize) {
        super(key, deSerialize);
    }

    @Override
    public PointItem[] toFilters(PointItem[] items) {
        List<PointItem> results = new ArrayList<>();
        for(PointItem item : items) {
            if(isAppend(item, null)) {
                results.add(item);
            }
        }
        return results.toArray(new PointItem[0]);
    }
}
