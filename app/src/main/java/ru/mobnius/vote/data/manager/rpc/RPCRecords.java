package ru.mobnius.vote.data.manager.rpc;

import org.json.JSONObject;

/**
 * результат с записями
 */
public class RPCRecords {
    /**
     * список записей
     */
    public JSONObject[] records;

    /**
     * количество записей
     */
    public int total;
}
