package ru.mobnius.vote.ui.data;

import ru.mobnius.vote.data.manager.SortManager;
import ru.mobnius.vote.ui.model.PointItem;

public class PointSortManager extends SortManager<PointItem> {
    public PointSortManager(String key) {
        super(key);
    }

    public PointSortManager(String key, String deSerialize) {
        super(key, deSerialize);
    }
}
