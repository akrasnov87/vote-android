package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

@Entity(nameInDb = "sd_digests")
class Digests {
    /**
     * Идентификатор
     */
    @Id
    @Property(nameInDb = "id")
    private Long id;

    /**
     * версия
     */
    @Expose
    private String c_version;

    /**
     * Описание изменения
     */
    @Expose
    private String c_description;

    /**
     * филиал
     */
    @Expose
    private long f_division;

    /**
     * имя приложения
     */
    @Expose
    private String c_app_name;

    /**
     * скрыт
     */
    @Expose
    private boolean b_hidden;

    @Generated(hash = 1812362365)
    public Digests(Long id, String c_version, String c_description, long f_division,
            String c_app_name, boolean b_hidden) {
        this.id = id;
        this.c_version = c_version;
        this.c_description = c_description;
        this.f_division = f_division;
        this.c_app_name = c_app_name;
        this.b_hidden = b_hidden;
    }

    @Generated(hash = 1934016046)
    public Digests() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getC_version() {
        return this.c_version;
    }

    public void setC_version(String c_version) {
        this.c_version = c_version;
    }

    public String getC_description() {
        return this.c_description;
    }

    public void setC_description(String c_description) {
        this.c_description = c_description;
    }

    public long getF_division() {
        return this.f_division;
    }

    public void setF_division(long f_division) {
        this.f_division = f_division;
    }

    public String getC_app_name() {
        return this.c_app_name;
    }

    public void setC_app_name(String c_app_name) {
        this.c_app_name = c_app_name;
    }

    public boolean getB_hidden() {
        return this.b_hidden;
    }

    public void setB_hidden(boolean b_hidden) {
        this.b_hidden = b_hidden;
    }
}
