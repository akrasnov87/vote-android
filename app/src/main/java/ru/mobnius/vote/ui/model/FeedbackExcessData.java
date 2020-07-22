package ru.mobnius.vote.ui.model;

import com.google.gson.Gson;

public class FeedbackExcessData {
    public String point_id;
    public String c_appartament;
    public String c_address;

    public FeedbackExcessData(String pointId, String appartament, String address) {
        point_id = pointId;
        c_appartament = appartament;
        c_address = address;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
