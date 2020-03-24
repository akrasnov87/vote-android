package ru.mobnius.vote.data.manager.rpc;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

/**
 * Параметры передаваемые в одиночных запросах RPC
 */
public class SingleItemQuery {

    public SingleItemQuery(Object obj) {
        this.params = new Object[1];
        this.params[0] = obj;
    }

    /**
     * дополнительные параметры. Применяется для вызова одиночных метод
     */
    @Expose
    private Object[] params;

    public String toJsonString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
