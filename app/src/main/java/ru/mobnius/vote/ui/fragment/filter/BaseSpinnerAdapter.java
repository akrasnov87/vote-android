package ru.mobnius.vote.ui.fragment.filter;

import android.content.Context;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.utils.LongUtil;

public abstract class BaseSpinnerAdapter extends SimpleAdapter {
    protected ArrayList<Map<String, Object>> mMaps;
    protected DataManager mDataManager;

    public BaseSpinnerAdapter(Context context, ArrayList<Map<String, Object>> items, String[] from, int[] to) {
        super(context, items, R.layout.simple_type_item, from, to);
        mMaps = items;
        mDataManager = DataManager.getInstance();
    }

    public long getId(int position) {
        HashMap m = (HashMap)getItem(position);
        return LongUtil.convertToLong(m.get(Names.ID));
    }

    public String getStringValue(int position){
        HashMap m = (HashMap) getItem(position);
        return String.valueOf(m.get(Names.NAME));
    }

    public int getPositionById(long id) {
        for(int i = 0; i < getCount(); i++) {
            HashMap m = (HashMap)mMaps.get(i);
            long resultId = LongUtil.convertToLong(m.get(Names.ID));
            if(resultId == id) {
                return i;
            }
        }

        return  -1;
    }

    protected void addItem(long id, String name) {
        Map<String, Object> m = new HashMap<>();
        m.put(Names.NAME, name);
        m.put(Names.ID, id);

        mMaps.add(m);
    }
}
