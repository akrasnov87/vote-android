package ru.mobnius.vote.ui.model;

import org.json.JSONObject;

public class PointItem {
    public PointItem() {
        color = null;
    }
    public String id;
    public int appartamentNumber;
    public String appartament;
    public String address;
    public String notice;
    public String info;
    public String routeTypeName;
    public String routeName;
    public String routeId;
    public String fio;
    public String[] color;
    public String houseNumber;
    public Integer priority;
    public Integer rating;
    public String data;

    /**
     * Было выполнено или нет
     */
    public boolean done;

    /**
     * Было синхронизировано или нет
     */
    public boolean sync;

    public Integer getStatus() {
        try {
            JSONObject jsonObject = new JSONObject(data);
            return jsonObject.has("n_status") ? jsonObject.getInt("n_status"): null;
        }catch (Exception e) {
            return null;
        }
    }
}
