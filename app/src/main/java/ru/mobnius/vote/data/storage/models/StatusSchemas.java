package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import org.greenrobot.greendao.annotation.Generated;

@SuppressWarnings("unused")
@Entity(nameInDb = "ss_status_schemas")
class StatusSchemas {

    /**
     * Признак отключенной записи
     */
    @Expose
    private long b_disabled;

    /**
     * Конечный статус
     */
    @Expose
    private long f_end;

    /**
     * Начальный статус
     */
    @Expose
    private long f_start;

    /**
     * Идентификатор
     */
    @Id
    @Property(nameInDb = "id")
    private Long id;

    @Generated(hash = 1278845539)
    public StatusSchemas(long b_disabled, long f_end, long f_start, Long id) {
        this.b_disabled = b_disabled;
        this.f_end = f_end;
        this.f_start = f_start;
        this.id = id;
    }

    @Generated(hash = 1153591283)
    public StatusSchemas() {
    }

    public long getB_disabled() {
        return this.b_disabled;
    }

    public void setB_disabled(long b_disabled) {
        this.b_disabled = b_disabled;
    }

    public long getF_end() {
        return this.f_end;
    }

    public void setF_end(long f_end) {
        this.f_end = f_end;
    }

    public long getF_start() {
        return this.f_start;
    }

    public void setF_start(long f_start) {
        this.f_start = f_start;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
