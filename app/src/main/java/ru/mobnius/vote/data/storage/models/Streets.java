package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

@SuppressWarnings({"unused", "StringEquality"})
@Entity(nameInDb = "cs_street")
public class Streets {

    /**
     * Идентификатор
     */
    @Id
    @Expose
    public String id;

    /**
     * тип
     */
    @Expose
    public String c_type;

    /**
     * наименование
     */
    @Expose
    public String c_name;


    /**
     * дополнительные данные
     */
    @Expose
    public String jb_data;


    @Generated(hash = 1949114932)
    public Streets(String id, String c_type, String c_name, String jb_data) {
        this.id = id;
        this.c_type = c_type;
        this.c_name = c_name;
        this.jb_data = jb_data;
    }


    @Generated(hash = 2024601194)
    public Streets() {
    }


    public String getId() {
        return this.id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getC_type() {
        return this.c_type;
    }


    public void setC_type(String c_type) {
        this.c_type = c_type;
    }


    public String getC_name() {
        return this.c_name;
    }


    public void setC_name(String c_name) {
        this.c_name = c_name;
    }


    public String getJb_data() {
        return this.jb_data;
    }


    public void setJb_data(String jb_data) {
        this.jb_data = jb_data;
    }
}
