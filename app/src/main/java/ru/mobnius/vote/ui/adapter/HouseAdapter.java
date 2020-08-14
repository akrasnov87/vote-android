package ru.mobnius.vote.ui.adapter;

import android.content.Context;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.storage.models.FeedbackTypes;
import ru.mobnius.vote.data.storage.models.Houses;
import ru.mobnius.vote.utils.LongUtil;
import ru.mobnius.vote.utils.StringUtil;

public class HouseAdapter extends SimpleAdapter {
    protected ArrayList<Map<String, Object>> mMaps;
    protected DataManager mDataManager;

    private static String[] from = { Names.NAME };
    private static int[] to = { R.id.simple_type_item_name };

    public HouseAdapter(Context context, ArrayList<Map<String, Object>> items) {
        super(context, items, R.layout.simple_type_item, from, to);

        mMaps = items;
        mDataManager = DataManager.getInstance();

        List<Houses> houses = mDataManager.getDaoSession().getHousesDao().loadAll();

        for(Houses item : houses) {
            addItem(item.getId(), item.c_street + " " + item.c_number + (!StringUtil.isEmptyOrNull(item.c_build) ? " корп. " + item.c_build : ""));
        }
    }

    public long getId(int position) {
        HashMap m = (HashMap)getItem(position);
        return LongUtil.convertToLong(m.get(Names.ID));
    }

    public ArrayList<Map<String, Object>> getMaps() {
        return mMaps;
    }

    public String getStringValue(int position) {
        HashMap m = (HashMap) getItem(position);
        return String.valueOf(m.get(Names.NAME));
    }

    public int getPositionById(String id) {
        for(int i = 0; i < getCount(); i++) {
            HashMap m = (HashMap)mMaps.get(i);
            String resultId = String.valueOf(m.get(Names.ID));
            if(resultId.equals(id)) {
                return i;
            }
        }

        return  -1;
    }

    protected void addItem(String id, String name) {
        Map<String, Object> m = new HashMap<>();
        m.put(Names.NAME, name);
        m.put(Names.ID, id);

        mMaps.add(m);
    }
}
