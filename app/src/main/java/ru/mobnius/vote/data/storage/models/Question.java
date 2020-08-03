package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@SuppressWarnings("unused")
@Entity(nameInDb = "cs_question")
public class Question {
    /**
     * Идентификатор
     */
    @Id
    @Property(nameInDb = "id")
    public Long id;

    /**
     * версия
     */
    @Expose
    public String c_title;

    /**
     * Описание изменения
     */
    @Expose
    private String c_description;

    /**
     * имя приложения
     */
    @Expose
    public String c_text;

    @Expose
    public int n_order;

    public String c_role;

    @Generated(hash = 1330151270)
    public Question(Long id, String c_title, String c_description, String c_text,
            int n_order, String c_role) {
        this.id = id;
        this.c_title = c_title;
        this.c_description = c_description;
        this.c_text = c_text;
        this.n_order = n_order;
        this.c_role = c_role;
    }

    @Generated(hash = 1868476517)
    public Question() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getC_title() {
        return this.c_title;
    }

    public void setC_title(String c_title) {
        this.c_title = c_title;
    }

    public String getC_description() {
        return this.c_description;
    }

    public void setC_description(String c_description) {
        this.c_description = c_description;
    }

    public String getC_text() {
        return this.c_text;
    }

    public void setC_text(String c_text) {
        this.c_text = c_text;
    }

    public int getN_order() {
        return this.n_order;
    }

    public void setN_order(int n_order) {
        this.n_order = n_order;
    }

    public String getC_role() {
        return this.c_role;
    }

    public void setC_role(String c_role) {
        this.c_role = c_role;
    }
}
