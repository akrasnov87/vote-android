package ru.mobnius.vote.data.manager.rpc;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import ru.mobnius.vote.data.manager.synchronization.BaseSynchronization;

/**
 * Параметры передаваемые в одиночных запросах RPC
 */
public class SingleItemQuery {

    public SingleItemQuery(Object obj) {
        this.params = new Object[1];
        this.params[0] = obj;
        limit = BaseSynchronization.MAX_COUNT_IN_QUERY;
    }

    /**
     * дополнительные параметры. Применяется для вызова одиночных метод
     */
    @SuppressWarnings("MismatchedReadAndWriteOfArray")
    @Expose
    private final Object[] params;

    @Expose
    private final int limit;

    public String toJsonString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
