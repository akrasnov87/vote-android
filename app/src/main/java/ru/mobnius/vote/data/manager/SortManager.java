package ru.mobnius.vote.data.manager;


import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import ru.mobnius.vote.ui.model.SortItem;

/**
 * управление сортировками
 * Сортировка работает только для типов Int, Double, Boolean, String, Date, Long
 */
public abstract class SortManager<T> extends ItemsManager<SortItem, Integer> {

    /**
     * Конструктор
     *
     * @param key ключ
     */
    public SortManager(String key) {
        super(key);
    }

    /**
     * Конструктор
     *
     * @param key         ключ
     * @param deSerialize строка для обработки
     */
    public SortManager(String key, String deSerialize) {
        super(key, deSerialize);
    }

    /**
     * Обновление сортировки
     *
     * @param key   наименование
     * @param type тип ASC или DESC
     */
    @Override
    public void updateItem(String key, Integer type) {
        SortItem sortItem = getItem(key);
        if (sortItem != null) {
            sortItem.setType(type);
        }
    }

    public T[] toSorters(T[] items) {
        return toSorters(items, null);
    }

    /**
     * Сортировка данных
     * Внимание!!! Сортировка работает только для типов Int, Double, Boolean, String, Date, Long
     *
     * @param items массив данных
     * @return результат
     */
    public T[] toSorters(T[] items, final ISortCallback callback) {

        List<T> results = Arrays.asList(items);

        final SortItem[] sorters = getItems();

        Collections.sort(results, new Comparator<T>() {
            @Override
            public int compare(T t1, T t2) {
                try {
                    for (SortItem item : sorters) {
                        Field field = t1.getClass().getField(item.getName());

                        if (field.getType() == String.class) {
                            String str1 = String.valueOf(field.get(t1));
                            String str2 = String.valueOf(field.get(t2));
                            int compareStr = item.getType() == SortItem.ASC ? str1.compareTo(str2) : str2.compareTo(str1);
                            if(compareStr != 0) {
                                return compareStr;
                            }
                        }

                        if (field.getType() == int.class) {
                            int i1 = field.getInt(t1);
                            int i2 = field.getInt(t2);
                            int compareInt = item.getType() == SortItem.ASC ? Integer.compare(i1, i2) : Integer.compare(i2, i1);
                            if(compareInt != 0) {
                                return compareInt;
                            }
                        }

                        if (field.getType() == double.class) {
                            double d1 = field.getDouble(t1);
                            double d2 = field.getDouble(t2);
                            int compareDouble = item.getType() == SortItem.ASC ? Double.compare(d1, d2) : Double.compare(d2, d1);
                            if(compareDouble != 0) {
                                return compareDouble;
                            }
                        }

                        if (field.getType() == boolean.class) {
                            boolean b1 = field.getBoolean(t1);
                            boolean b2 = field.getBoolean(t2);
                            int compareBoolean = item.getType() == SortItem.ASC ? Boolean.compare(b1, b2) : Boolean.compare(b2, b1);
                            if(compareBoolean != 0) {
                                return compareBoolean;
                            }
                        }

                        if (field.getType() == Date.class) {
                            long l1 = ((Date)field.get(t1)).getTime();
                            long l2 = ((Date)field.get(t2)).getTime();
                            int compareDate = item.getType() == SortItem.ASC ? Long.compare(l1, l2) : Long.compare(l2, l1);
                            if(compareDate != 0) {
                                return compareDate;
                            }
                        }

                        if (field.getType() == Long.class) {
                            long l1 = field.getLong(t1);
                            long l2 = field.getLong(t2);
                            int compareLong = item.getType() == SortItem.ASC ? Long.compare(l1, l2) : Long.compare(l2, l1);
                            if(compareLong != 0) {
                                return compareLong;
                            }
                        }

                        if(callback != null) {
                            return callback.compareObject(item, t1, t2);
                        }
                    }
                } catch (NoSuchFieldException | IllegalAccessException ignored) {

                }

                return 0;
            }
        });

        return (T[]) results.toArray();
    }

    @Override
    protected SortItem getItemObject(JSONObject jsonObject) throws JSONException {
        return new SortItem(jsonObject.getString("mName"), jsonObject.getInt("mType"));
    }

    @Override
    public SortItem[] getItems() {
        return mItems.toArray(new SortItem[0]);
    }

    public interface ISortCallback<T> {
        int compareObject(SortItem item, T t1, T t2);
    }
}
