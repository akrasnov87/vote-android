package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;

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
    public String fn_house;

    /**
     * Вышестоящее отделение
     */
    @ToOne(joinProperty = "fn_house")
    private Houses mHouse;

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

    @Generated(hash = 1400166230)
    public Contacts(String id, String c_first_name, String c_last_name,
            String c_patronymic, String fn_house, String c_appartament,
            Integer n_rating, String c_description, String d_date, long fn_user,
            String jb_data, String c_phone, boolean b_disabled,
            String objectOperationType, boolean isDelete, boolean isSynchronization,
            String tid, String blockTid) {
        this.id = id;
        this.c_first_name = c_first_name;
        this.c_last_name = c_last_name;
        this.c_patronymic = c_patronymic;
        this.fn_house = fn_house;
        this.c_appartament = c_appartament;
        this.n_rating = n_rating;
        this.c_description = c_description;
        this.d_date = d_date;
        this.fn_user = fn_user;
        this.jb_data = jb_data;
        this.c_phone = c_phone;
        this.b_disabled = b_disabled;
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

    public String getFn_house() {
        return this.fn_house;
    }

    public void setFn_house(String fn_house) {
        this.fn_house = fn_house;
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

    @Generated(hash = 823378290)
    private transient String mHouse__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 667274009)
    public Houses getMHouse() {
        String __key = this.fn_house;
        if (mHouse__resolvedKey == null || mHouse__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            HousesDao targetDao = daoSession.getHousesDao();
            Houses mHouseNew = targetDao.load(__key);
            synchronized (this) {
                mHouse = mHouseNew;
                mHouse__resolvedKey = __key;
            }
        }
        return mHouse;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1181902240)
    public void setMHouse(Houses mHouse) {
        synchronized (this) {
            this.mHouse = mHouse;
            fn_house = mHouse == null ? null : mHouse.getId();
            mHouse__resolvedKey = fn_house;
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
