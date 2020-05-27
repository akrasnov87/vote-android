package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import org.greenrobot.greendao.annotation.Generated;

@SuppressWarnings("unused")
@Entity(nameInDb = "cs_route_statuses")
public class RouteStatuses {

    /**
     * Константа
     */
    @Expose
    public String c_const;

    /**
     * Наименование
     */
    @Expose
    public String c_name;

    /**
     * Краткое наименование
     */
    @Expose
    private String c_short_name;

    /**
     * Идентификатор
     */
    @Id
    @Property(nameInDb = "id")
    public Long id;

    /**
     * Код
     */
    @Expose
    private long n_code;

    /**
     * Приоритет статуса(чем больше число тем выше статус)
     */
    @Expose
    private long n_order;

    /**
     * отключено
     */
    @Expose
    private boolean b_disabled;

    @Generated(hash = 1085664331)
    public RouteStatuses(String c_const, String c_name, String c_short_name,
            Long id, long n_code, long n_order, boolean b_disabled) {
        this.c_const = c_const;
        this.c_name = c_name;
        this.c_short_name = c_short_name;
        this.id = id;
        this.n_code = n_code;
        this.n_order = n_order;
        this.b_disabled = b_disabled;
    }

    @Generated(hash = 601940321)
    public RouteStatuses() {
    }

    public String getC_const() {
        return this.c_const;
    }

    public void setC_const(String c_const) {
        this.c_const = c_const;
    }

    public String getC_name() {
        return this.c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getC_short_name() {
        return this.c_short_name;
    }

    public void setC_short_name(String c_short_name) {
        this.c_short_name = c_short_name;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getN_code() {
        return this.n_code;
    }

    public void setN_code(long n_code) {
        this.n_code = n_code;
    }

    public long getN_order() {
        return this.n_order;
    }

    public void setN_order(long n_order) {
        this.n_order = n_order;
    }

    public boolean getB_disabled() {
        return this.b_disabled;
    }

    public void setB_disabled(boolean b_disabled) {
        this.b_disabled = b_disabled;
    }
}
