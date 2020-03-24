package ru.mobnius.vote.ui.model;

import com.google.gson.annotations.Expose;

import ru.mobnius.vote.data.manager.ItemsManager;

/**
 * Элемент сортировки
 */
public class SortItem implements ItemsManager.IItemManager {

    public final static int DESC = 1;
    public final static int ASC = 2;

    /**
     * Имя
     */
    @Expose
    private String mName;

    /**
     * Тип сортировки. Выбирается один из параметров:
     * - SortItem.DESC
     * - SortItem.ASC
     */
    @Expose
    private int mType;

    public SortItem(String name, int type) {
        mName = name;
        mType = type;
    }

    /**
     * Сортировка по умолчанию ASC
     * @param name имя поля
     */
    public SortItem(String name) {
        this(name, SortItem.ASC);
    }

    public String getName() {
        return mName;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }
}
