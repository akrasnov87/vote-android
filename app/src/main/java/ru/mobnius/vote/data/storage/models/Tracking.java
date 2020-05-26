package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.UUID;

import ru.mobnius.vote.data.storage.models.IEntityTo;
import ru.mobnius.vote.data.storage.models.Users;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

@SuppressWarnings("unused")
@Entity(nameInDb = "ad_tracking")
public class Tracking implements IEntityTo {

    public Tracking(){
        id = UUID.randomUUID().toString();
    }

    @Generated(hash = 905782830)
    public Tracking(String c_network_status, String d_date, long fn_user, String id,
            double n_latitude, double n_longitude, String objectOperationType,
            boolean isDelete, boolean isSynchronization, String tid,
            String blockTid, String dx_created) {
        this.c_network_status = c_network_status;
        this.d_date = d_date;
        this.fn_user = fn_user;
        this.id = id;
        this.n_latitude = n_latitude;
        this.n_longitude = n_longitude;
        this.objectOperationType = objectOperationType;
        this.isDelete = isDelete;
        this.isSynchronization = isSynchronization;
        this.tid = tid;
        this.blockTid = blockTid;
        this.dx_created = dx_created;
    }

    public String getC_network_status() {
        return this.c_network_status;
    }

    public void setC_network_status(String c_network_status) {
        this.c_network_status = c_network_status;
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

    public double getN_latitude() {
        return this.n_latitude;
    }

    public void setN_latitude(double n_latitude) {
        this.n_latitude = n_latitude;
    }

    public double getN_longitude() {
        return this.n_longitude;
    }

    public void setN_longitude(double n_longitude) {
        this.n_longitude = n_longitude;
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
    @Generated(hash = 2070004789)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTrackingDao() : null;
    }

    /**
     * Тип сети
     */
    @Expose
    public String c_network_status;

    /**
     * Дата
     */
    @Expose
    public String d_date;

    /**
     * Исполнитель
     */
    @Expose
    public long fn_user;
    /**
     * Исполнитель
     */
    @ToOne(joinProperty = "fn_user")
    private Users user;

    /**
     * Идентификатор
     */
    @Id
    @Expose
    private String id;

    /**
     * Широта
     */
    @Expose
    public double n_latitude;

    /**
     * Долгота
     */
    @Expose
    public double n_longitude;

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
    private boolean isSynchronization;

    /**
     * идентификатор транзакции
     */
    public String tid;

    /**
     * идентификатор блока
     */
    public String blockTid;

    private String dx_created;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 86903257)
    private transient TrackingDao myDao;

    @Generated(hash = 251390918)
    private transient Long user__resolvedKey;
}
