package ru.mobnius.vote.ui.model;

public class RatingItemModel {
    public int id;
    public int user_id;
    public String c_login;
    public Integer n_uik;
    public Integer f_subdivision;
    public String c_subdivision;
    public int n_all;
    public int n_count;
    public int n_today_count;
    public boolean b_its_me;

    public String getUik() {
        return f_subdivision != null ? c_subdivision : String.valueOf(n_uik);
    }
}
