package ru.mobnius.vote.data.manager;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public abstract class ItemsManager<K> {
    @Expose
    protected final List<K> mItems;

    ItemsManager() {
        mItems = new ArrayList<>(3);
    }

    /**
     * Добавление элемента
     *
     * @param item элемент
     */
    protected void addItem(K item) {
        mItems.add(item);
    }
}
