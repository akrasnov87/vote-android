package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

@Entity(nameInDb = "ad_audits")
public class Audits implements IEntityTo {

    // TODO: может сразу дату d_date записывать?
    public Audits(){
        c_app_name = "mobile";
    }

    @Generated(hash = 135384903)
    public Audits(Long id, long fn_user, String d_date, String c_data,
            String c_type, String c_app_name, String objectOperationType,
            boolean isDelete, boolean isSynchronization, String tid,
            String blockTid, String dx_created) {
        this.id = id;
        this.fn_user = fn_user;
        this.d_date = d_date;
        this.c_data = c_data;
        this.c_type = c_type;
        this.c_app_name = c_app_name;
        this.objectOperationType = objectOperationType;
        this.isDelete = isDelete;
        this.isSynchronization = isSynchronization;
        this.tid = tid;
        this.blockTid = blockTid;
        this.dx_created = dx_created;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getFn_user() {
        return this.fn_user;
    }

    public void setFn_user(long fn_user) {
        this.fn_user = fn_user;
    }

    public String getD_date() {
        return this.d_date;
    }

    public void setD_date(String d_date) {
        this.d_date = d_date;
    }

    public String getC_data() {
        return this.c_data;
    }

    public void setC_data(String c_data) {
        this.c_data = c_data;
    }

    public String getC_type() {
        return this.c_type;
    }

    public void setC_type(String c_type) {
        this.c_type = c_type;
    }

    public String getC_app_name() {
        return this.c_app_name;
    }

    public void setC_app_name(String c_app_name) {
        this.c_app_name = c_app_name;
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
    @Generated(hash = 1593537311)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAuditsDao() : null;
    }

    /**
     * Идентификатор
     */
    @Id(autoincrement = true)
    private Long id;

    /**
     * Пользователь
     */
    @Expose
    public long fn_user;

    /**
     * Пользователь
     */
    @ToOne(joinProperty = "fn_user")
    private Users user;

    /**
     * Дата события
     */
    @Expose
    public String d_date;

    /**
     * Дополнительные параметры
     */
    @Expose
    public String c_data;

    /**
     * Тип события
     */
    @Expose
    public String c_type;

    /**
     * Имя приложение
     */
    @Expose
    private String c_app_name;

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
    @Generated(hash = 676051977)
    private transient AuditsDao myDao;

    @Generated(hash = 251390918)
    private transient Long user__resolvedKey;
}
