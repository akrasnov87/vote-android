package ru.mobnius.vote.utils;

import android.widget.SimpleAdapter;

import java.util.HashMap;

import ru.mobnius.vote.Names;

public class AdapterUtil {
    /**
     * Получение позиции по идентификтаору записи
     *
     * @param adapter адаптер
     * @param id      идентификатор записи
     * @return позиция
     */
    public static int getAdapterItemPosition(SimpleAdapter adapter, long id) {
        return getAdapterItemPosition(adapter, id, new IAdapterItemPosition() {
            @Override
            public boolean equals(HashMap<String, Object> map, Object id) {
                return LongUtil.convertToLong(map.get(Names.ID)) == LongUtil.convertToLong(id);
            }
        });
    }

    /**
     * Получение позиции по идентификтаору записи
     *
     * @param adapter адаптер
     * @param id      идентификатор записи
     * @return позиция
     */
    public static int getAdapterItemPosition(SimpleAdapter adapter, int id) {
        return getAdapterItemPosition(adapter, id, new IAdapterItemPosition() {
            @Override
            public boolean equals(HashMap<String, Object> map, Object id) {
                return IntUtil.convertToInt(map.get(Names.ID)) == IntUtil.convertToInt(id);
            }
        });
    }

    /**
     * Получение позиции по идентификтаору записи
     *
     * @param adapter адаптер
     * @param id      идентификатор записи
     * @return позиция
     */
    public static int getAdapterItemPosition(SimpleAdapter adapter, String id) {
        return getAdapterItemPosition(adapter, id, new IAdapterItemPosition() {
            @Override
            public boolean equals(HashMap<String, Object> map, Object id) {
                return String.valueOf(map.get(Names.ID)).equals(String.valueOf(id));
            }
        });
    }

    /**
     * Получение позиции по идентификтаору записи
     *
     * @param adapter адаптер
     * @param id      идентификатор записи
     * @return позиция
     */
    private static int getAdapterItemPosition(SimpleAdapter adapter, Object id, IAdapterItemPosition callback) {
        for (int position = 0; position < adapter.getCount(); position++) {
            if (callback.equals((HashMap<String, Object>) adapter.getItem(position), id)) {
                return position;
            }
        }
        return 0;
    }
}
