package ru.mobnius.vote.ui.fragment.filter;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.storage.models.RouteStatuses;

public class RouteStatusAdapter extends BaseSpinnerAdapter {

    private static String[] from = { Names.NAME };
    private static int[] to = { R.id.simple_type_item_name };

    /**
     *
     * @param context
     * @param items передавать просто объект new ArrayList<Map<String, Object>>()
     */
    public RouteStatusAdapter(Context context, ArrayList<Map<String, Object>> items) {
        super(context, items, from, to);

        List<RouteStatuses> routeStatuses = mDataManager.getDaoSession().getRouteStatusesDao().loadAll();

        addItem(-1, "Не установлен");

        for(RouteStatuses item : routeStatuses) {
            addItem(item.id, item.c_name);
        }
    }
}
