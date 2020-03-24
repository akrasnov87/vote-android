package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.UUID;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

import org.greenrobot.greendao.annotation.NotNull;

@Entity(nameInDb = "ad_mobile_devices")
public class MobileDevices implements IEntityTo {

    public MobileDevices(){
        id = UUID.randomUUID().toString();
    }

    @Generated(hash = 634041132)
    public MobileDevices(boolean b_debug, String c_application_version,
            String c_architecture, String c_imei, String c_os, String c_phone_model,
            String c_sdk, String d_date, long fn_user, String id,
            String objectOperationType, boolean isDelete, boolean isSynchronization,
            String tid, String blockTid, String dx_created) {
        this.b_debug = b_debug;
        this.c_application_version = c_application_version;
        this.c_architecture = c_architecture;
        this.c_imei = c_imei;
        this.c_os = c_os;
        this.c_phone_model = c_phone_model;
        this.c_sdk = c_sdk;
        this.d_date = d_date;
        this.fn_user = fn_user;
        this.id = id;
        this.objectOperationType = objectOperationType;
        this.isDelete = isDelete;
        this.isSynchronization = isSynchronization;
        this.tid = tid;
        this.blockTid = blockTid;
        this.dx_created = dx_created;
    }

    public boolean getB_debug() {
        return this.b_debug;
    }

    public void setB_debug(boolean b_debug) {
        this.b_debug = b_debug;
    }

    public String getC_application_version() {
        return this.c_application_version;
    }

    public void setC_application_version(String c_application_version) {
        this.c_application_version = c_application_version;
    }

    public String getC_architecture() {
        return this.c_architecture;
    }

    public void setC_architecture(String c_architecture) {
        this.c_architecture = c_architecture;
    }

    public String getC_imei() {
        return this.c_imei;
    }

    public void setC_imei(String c_imei) {
        this.c_imei = c_imei;
    }

    public String getC_os() {
        return this.c_os;
    }

    public void setC_os(String c_os) {
        this.c_os = c_os;
    }

    public String getC_phone_model() {
        return this.c_phone_model;
    }

    public void setC_phone_model(String c_phone_model) {
        this.c_phone_model = c_phone_model;
    }

    public String getC_sdk() {
        return this.c_sdk;
    }

    public void setC_sdk(String c_sdk) {
        this.c_sdk = c_sdk;
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

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDx_created() {
        return this.dx_created;
    }

    public void setDx_created(String dx_created) {
        this.dx_created = dx_created;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 472964965)
    public Users getUser() {
        long __key = this.fn_user;
        if (user__resolvedKey == null || !user__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UsersDao targetDao = daoSession.getUsersDao();
            Users userNew = targetDao.load(__key);
            synchronized (this) {
                user = userNew;
                user__resolvedKey = __key;
            }
        }
        return user;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1934989217)
    public void setUser(@NotNull Users user) {
        if (user == null) {
            throw new DaoException(
                    "To-one property 'fn_user' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.user = user;
            fn_user = user.getId();
            user__resolvedKey = fn_user;
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
    @Generated(hash = 1805203638)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMobileDevicesDao() : null;
    }

    @Expose
    public boolean b_debug;

    /**
     * Версия приложения
     */
    @Expose
    public String c_application_version;

    /**
     * Архитектура устройства
     */
    @Expose
    public String c_architecture;

    /**
     * IMEI
     */
    @Expose
    public String c_imei;

    /**
     * Версия ОС
     */
    @Expose
    public String c_os;

    /**
     * Модель телефона
     */
    @Expose
    public String c_phone_model;

    /**
     * Версия sdk
     */
    @Expose
    public String c_sdk;

    /**
     * Дата возникновения событий
     */
    @Expose
    public String d_date;

    /**
     * Пользователь
     */
    @Expose
    public long fn_user;
    /**
     * Пользователь
     */
    @ToOne(joinProperty = "fn_user")
    public Users user;

    /**
     * Идентификатор
     */
    @Id
    @Expose
    public String id;

    /**
     * Тип операции надл объектом
     */
    public String objectOperationType;

    /**
     * Запись была удалена или нет
     */
    public boolean isDelete;

    /**
     * Была произведена синхронизация или нет
     */
    public boolean isSynchronization;

    /**
     * идентификатор транзакции
     */
    public String tid;

    /**
     * идентификатор блока
     */
    public String blockTid;

    public String dx_created;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 241478181)
    private transient MobileDevicesDao myDao;

    @Generated(hash = 251390918)
    private transient Long user__resolvedKey;
}
