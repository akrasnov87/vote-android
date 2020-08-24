package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "cd_mobile_help")
public class MobileHelp {
    @Id
    @Property(nameInDb = "id")
    public Long id;

    @Expose
    public String c_key;

    @Expose
    public String c_html;

    @Expose
    public String d_date;

    @Expose
    public String c_title;

    @Generated(hash = 1545509637)
    public MobileHelp(Long id, String c_key, String c_html, String d_date,
            String c_title) {
        this.id = id;
        this.c_key = c_key;
        this.c_html = c_html;
        this.d_date = d_date;
        this.c_title = c_title;
    }

    @Generated(hash = 480356006)
    public MobileHelp() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getC_key() {
        return this.c_key;
    }

    public void setC_key(String c_key) {
        this.c_key = c_key;
    }

    public String getC_html() {
        return this.c_html;
    }

    public void setC_html(String c_html) {
        this.c_html = c_html;
    }

    public String getD_date() {
        return this.d_date;
    }

    public void setD_date(String d_date) {
        this.d_date = d_date;
    }

    public String getC_title() {
        return this.c_title;
    }

    public void setC_title(String c_title) {
        this.c_title = c_title;
    }
}
