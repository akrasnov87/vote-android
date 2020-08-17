package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;

import java.io.Serializable;

@SuppressWarnings({"unused", "StringEquality"})
@Entity(nameInDb = "cd_contacts")
public class Contacts {

    /**
     * Идентификатор
     */
    @Id
    @Expose
    public String id;

    /**
     * фамилия
     */
    @Expose
    public String c_first_name;

    /**
     * имя
     */
    @Expose
    public String c_last_name;

    /**
     * отчество
     */
    @Expose
    public String c_patronymic;

    /**
     * дом
     */
    @Expose
    public String fn_street;

    /**
     * Вышестоящее отделение
     */
    @ToOne(joinProperty = "fn_street")
    private Streets street;

    /**
     * номер квартиры
     */
    @Expose
    public String c_house_num;

    /**
     * номер квартиры
     */
    @Expose
    public String c_house_build;

    /**
     * номер квартиры
     */
    @Expose
    public String c_appartament;

    /**
     * рейтинг
     */
    @Expose
    public Integer n_rating;

    /**
     * описание
     */
    @Expose
    public String c_description;

    /**
     * дата
     */
    @Expose
    public String d_date;

    /**
     * текущий пользователь
     */
    @Expose
    public long fn_user;

    /**
     * дополнительные данные
     */
    @Expose
    public String jb_data;

    /**
     * телефон
     */
    @Expose
    public String c_phone;

    /**
     * ОТключить
     */
    @Expose
    public boolean b_disabled;

    @Expose
    public long n_order;

    /**
     * Тип операции надл объектом
     */
    public String objectOperationType;

    /**
     * Запись была удалена или нет
     */
    private boolean isDelete;

    /**
     * Была произведена синхронизация или нет
     */
    public boolean isSynchronization;

    /**
     * идентификатор транзакции
     */
    private String tid;

    /**
     * идентификатор блока
     */
    private String blockTid;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 892616969)
    private transient ContactsDao myDao;

    @Generated(hash = 947225395)
    public Contacts(String id, String c_first_name, String c_last_name,
            String c_patronymic, String fn_street, String c_house_num,
            String c_house_build, String c_appartament, Integer n_rating,
            String c_description, String d_date, long fn_user, String jb_data,
            String c_phone, boolean b_disabled, long n_order,
            String objectOperationType, boolean isDelete, boolean isSynchronization,
            String tid, String blockTid) {
        this.id = id;
        this.c_first_name = c_first_name;
        this.c_last_name = c_last_name;
        this.c_patronymic = c_patronymic;
        this.fn_street = fn_street;
        this.c_house_num = c_house_num;
        this.c_house_build = c_house_build;
        this.c_appartament = c_appartament;
        this.n_rating = n_rating;
        this.c_description = c_description;
        this.d_date = d_date;
        this.fn_user = fn_user;
        this.jb_data = jb_data;
        this.c_phone = c_phone;
        this.b_disabled = b_disabled;
        this.n_order = n_order;
        this.objectOperationType = objectOperationType;
        this.isDelete = isDelete;
        this.isSynchronization = isSynchronization;
        this.tid = tid;
        this.blockTid = blockTid;
    }

    @Generated(hash = 1804918957)
    public Contacts() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getC_first_name() {
        return this.c_first_name;
    }

    public void setC_first_name(String c_first_name) {
        this.c_first_name = c_first_name;
    }

    public String getC_last_name() {
        return this.c_last_name;
    }

    public void setC_last_name(String c_last_name) {
        this.c_last_name = c_last_name;
    }

    public String getC_patronymic() {
        return this.c_patronymic;
    }

    public void setC_patronymic(String c_patronymic) {
        this.c_patronymic = c_patronymic;
    }

    public String getFn_street() {
        return this.fn_street;
    }

    public void setFn_street(String fn_street) {
        this.fn_street = fn_street;
    }

    public String getC_house_num() {
        return this.c_house_num;
    }

    public void setC_house_num(String c_house_num) {
        this.c_house_num = c_house_num;
    }

    public String getC_house_build() {
        return this.c_house_build;
    }

    public void setC_house_build(String c_house_build) {
        this.c_house_build = c_house_build;
    }

    public String getC_appartament() {
        return this.c_appartament;
    }

    public void setC_appartament(String c_appartament) {
        this.c_appartament = c_appartament;
    }

    public Integer getN_rating() {
        return this.n_rating;
    }

    public void setN_rating(Integer n_rating) {
        this.n_rating = n_rating;
    }

    public String getC_description() {
        return this.c_description;
    }

    public void setC_description(String c_description) {
        this.c_description = c_description;
    }

    public String getD_date() {
        return this.d_date;
    }

    public void setD_date(String d_date) {
        this.d_date = d_date;
    }

    public long getFn_user() {
        return this.fn_user;
    }

    public void setFn_user(long fn_user) {
        this.fn_user = fn_user;
    }

    public String getJb_data() {
        return this.jb_data;
    }

    public void setJb_data(String jb_data) {
        this.jb_data = jb_data;
    }

    public String getC_phone() {
        return this.c_phone;
    }

    public void setC_phone(String c_phone) {
        this.c_phone = c_phone;
    }

    public boolean getB_disabled() {
        return this.b_disabled;
    }

    public void setB_disabled(boolean b_disabled) {
        this.b_disabled = b_disabled;
    }

    public long getN_order() {
        return this.n_order;
    }

    public void setN_order(long n_order) {
        this.n_order = n_order;
    }

    public String getObjectOperationType() {
        return this.objectOperationType;
    }

    public void setObjectOperationType(String objectOperationType) {
        this.objectOperationType = objectOperationType;
    }

    public boolean getIsDelete() {
        return this.isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public boolean getIsSynchronization() {
        return this.isSynchronization;
    }

    public void setIsSynchronization(boolean isSynchronization) {
        this.isSynchronization = isSynchronization;
    }

    public String getTid() {
        return this.tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getBlockTid() {
        return this.blockTid;
    }

    public void setBlockTid(String blockTid) {
        this.blockTid = blockTid;
    }

    @Generated(hash = 666408978)
    private transient String street__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 989367389)
    public Streets getStreet() {
        String __key = this.fn_street;
        if (street__resolvedKey == null || street__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            StreetsDao targetDao = daoSession.getStreetsDao();
            Streets streetNew = targetDao.load(__key);
            synchronized (this) {
                street = streetNew;
                street__resolvedKey = __key;
            }
        }
        return street;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2059513179)
    public void setStreet(Streets street) {
        synchronized (this) {
            this.street = street;
            fn_street = street == null ? null : street.getId();
            street__resolvedKey = fn_street;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1291567222)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getContactsDao() : null;
    }
}
