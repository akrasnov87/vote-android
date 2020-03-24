package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "cs_point_types")
public class PointTypes {

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
    public long n_code;

    /**
     * Наименование
     */
    @Expose
    public String c_name;

    /**
     * Краткое наименование
     */
    @Expose
    public String c_short_name;

    /**
     * Константа
     */
    @Expose
    public String c_const;

    /**
     * отключено
     */
    @Expose
    public boolean b_disabled;

    /**
     * сортировка
     */
    @Expose
    public int n_order;

    @Generated(hash = 1313352481)
    public PointTypes(Long id, long n_code, String c_name, String c_short_name,
            String c_const, boolean b_disabled, int n_order) {
        this.id = id;
        this.n_code = n_code;
        this.c_name = c_name;
        this.c_short_name = c_short_name;
        this.c_const = c_const;
        this.b_disabled = b_disabled;
        this.n_order = n_order;
    }

    @Generated(hash = 1538716286)
    public PointTypes() {
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

    public String getC_const() {
        return this.c_const;
    }

    public void setC_const(String c_const) {
        this.c_const = c_const;
    }

    public boolean getB_disabled() {
        return this.b_disabled;
    }

    public void setB_disabled(boolean b_disabled) {
        this.b_disabled = b_disabled;
    }

    public int getN_order() {
        return this.n_order;
    }

    public void setN_order(int n_order) {
        this.n_order = n_order;
    }
}
