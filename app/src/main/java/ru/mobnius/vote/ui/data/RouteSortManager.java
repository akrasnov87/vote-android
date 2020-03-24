package ru.mobnius.vote.ui.data;

import ru.mobnius.vote.data.manager.SortManager;
import ru.mobnius.vote.ui.model.RouteItem;

public class RouteSortManager extends SortManager<RouteItem> {
    public RouteSortManager(String key) {
        super(key);
    }

    public RouteSortManager(String key, String deSerialize) {
        super(key, deSerialize);
    }
}
