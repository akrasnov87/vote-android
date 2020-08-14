package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

@SuppressWarnings({"unused", "StringEquality"})
@Entity(nameInDb = "cs_house")
public class Houses {

    /**
     * Идентификатор
     */
    @Id
    @Expose
    public String id;

    /**
     * Адрес
     */
    @Expose
    public String c_street;

    /**
     * номер дома
     */
    @Expose
    public String c_number;

    /**
     * строение
     */
    @Expose
    public String c_build;

    /**
     * дополнительные данные
     */
    @Expose
    public String jb_data;

    @Generated(hash = 1007198275)
    public Houses(String id, String c_street, String c_number, String c_build,
            String jb_data) {
        this.id = id;
        this.c_street = c_street;
        this.c_number = c_number;
        this.c_build = c_build;
        this.jb_data = jb_data;
    }

    @Generated(hash = 885499324)
    public Houses() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getC_street() {
        return this.c_street;
    }

    public void setC_street(String c_street) {
        this.c_street = c_street;
    }

    public String getC_number() {
        return this.c_number;
    }

    public void setC_number(String c_number) {
        this.c_number = c_number;
    }

    public String getC_build() {
        return this.c_build;
    }

    public void setC_build(String c_build) {
        this.c_build = c_build;
    }

    public String getJb_data() {
        return this.jb_data;
    }

    public void setJb_data(String jb_data) {
        this.jb_data = jb_data;
    }
}
