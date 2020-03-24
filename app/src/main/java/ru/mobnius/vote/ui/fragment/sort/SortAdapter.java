package ru.mobnius.vote.ui.fragment.sort;

import android.content.Context;

import java.util.ArrayList;
import java.util.Map;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.ui.fragment.filter.BaseSpinnerAdapter;

public class SortAdapter extends BaseSpinnerAdapter {
    private static final int NO_SORT = 0;
    private static final int DESC = 1 ;
    private static final int ASC = 2;


    private static String[] from = {Names.NAME};
    private static int[] to = {R.id.simple_type_item_name};

    public SortAdapter(Context context, ArrayList<Map<String, Object>> items, boolean isStartDate) {
        super(context, items, from, to);

        String[] sortRouteDate = context.getResources().getStringArray(R.array.sort_route_date);
        addItem(NO_SORT, sortRouteDate[NO_SORT]);
        addItem(ASC, sortRouteDate[ASC]);
        addItem(DESC, sortRouteDate[DESC]);
    }

}
