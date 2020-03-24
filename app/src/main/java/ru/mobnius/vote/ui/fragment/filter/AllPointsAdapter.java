package ru.mobnius.vote.ui.fragment.filter;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.storage.models.Routes;

public class AllPointsAdapter extends BaseSpinnerAdapter {
    List<Routes> list = mDataManager.getRoutes(DataManager.RouteFilter.ALL);
    private static String[] from = {Names.NAME};
    private static int[] to = {R.id.simple_type_item_name};

    public AllPointsAdapter(Context context, ArrayList<Map<String, Object>> items) {
        super(context, items, from, to);

        addItem(-1, "", "Не установлен");
        for (int i = 0; i < list.size(); i++) {
            addItem((long) i, list.get(i).id, list.get(i).c_number);
        }
    }

    protected void addItem(long id, String routeId, String name) {
        Map<String, Object> m = new HashMap<>();
        m.put(Names.NAME, name);
        m.put(Names.ROUTE_ID, routeId);
        m.put(Names.ID, id);
        mMaps.add(m);
    }

    public String getRouteId(int position) {
        HashMap m = (HashMap) getItem(position);
        return String.valueOf(m.get(Names.ROUTE_ID));
    }

    public int getPositionByRouteId(String routeId) {
        int position = 0;
        for (int i = 0; i < mMaps.size(); i++) {
            if (mMaps.get(i).get(Names.ROUTE_ID).equals(routeId)) {
                position = i;
                break;
            }
        }
        return position;
    }
}
