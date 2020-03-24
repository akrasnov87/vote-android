package ru.mobnius.vote.ui.fragment.filter;

import android.content.Context;

import java.util.ArrayList;
import java.util.Map;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;

public class PointStatusAdapter extends BaseSpinnerAdapter {
    private static final int NO_FILTER = 0;
    private static final int DONE = 1;
    private static final int NOT_DONE = 2 ;

    private static String[] from = {Names.NAME};
    private static int[] to = {R.id.simple_type_item_name};


    /**
     * @param context
     * @param items   передавать просто объект new ArrayList<Map<String, Object>>()
     */
    public PointStatusAdapter(Context context, ArrayList<Map<String, Object>> items) {
        super(context, items, from, to);
        String[] statuses = context.getResources().getStringArray(R.array.point_statuses);
        addItem(NO_FILTER, statuses[NO_FILTER]);
        addItem(DONE, statuses[DONE]);
        addItem(NOT_DONE, statuses[NOT_DONE]);
    }
}


