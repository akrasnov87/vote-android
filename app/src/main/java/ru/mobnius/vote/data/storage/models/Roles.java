package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "pd_roles")
class Roles {

    /**
     * Идентификатор
     */
    @Id
    @Property(nameInDb = "id")
    private Long id;

    /**
     * Наименование
     */
    @Expose
    private String c_name;

    /**
     * Описание роли
     */
    @Expose
    private String c_description;

    /**
     * Приоритет
     */
    @Expose
    private long n_weight;

    private boolean sn_delete;

    @Generated(hash = 177554464)
    public Roles(Long id, String c_name, String c_description, long n_weight,
            boolean sn_delete) {
        this.id = id;
        this.c_name = c_name;
        this.c_description = c_description;
        this.n_weight = n_weight;
        this.sn_delete = sn_delete;
    }

    @Generated(hash = 1438703163)
    public Roles() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getC_name() {
        return this.c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getC_description() {
        return this.c_description;
    }

    public void setC_description(String c_description) {
        this.c_description = c_description;
    }

    public long getN_weight() {
        return this.n_weight;
    }

    public void setN_weight(long n_weight) {
        this.n_weight = n_weight;
    }

    public boolean getSn_delete() {
        return this.sn_delete;
    }

    public void setSn_delete(boolean sn_delete) {
        this.sn_delete = sn_delete;
    }
}
