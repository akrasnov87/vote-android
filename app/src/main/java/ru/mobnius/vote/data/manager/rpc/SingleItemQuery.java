package ru.mobnius.vote.data.manager.rpc;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import ru.mobnius.vote.data.manager.synchronization.BaseSynchronization;

/**
 * Параметры передаваемые в одиночных запросах RPC
 */
public class SingleItemQuery {

    public SingleItemQuery(Object... obj) {
        this.params = obj;
        limit = BaseSynchronization.MAX_COUNT_IN_QUERY;
    }

    public void setFilter(Object[] items) {
        filter = items;
    }

    @Expose
    private Object[] filter;

    /**
     * дополнительные параметры. Применяется для вызова одиночных метод
     */
    @Expose
    private final Object[] params;

    @Expose
    private final int limit;

    public String toJsonString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
