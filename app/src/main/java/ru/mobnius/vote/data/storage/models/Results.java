package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;

@Entity(nameInDb = "cd_results")
public class Results implements IEntityTo {
    /**
     * Идентификатор
     */
    @Id
    @Expose
    public String id;

    @Expose
    public String fn_route;

    @ToOne(joinProperty = "fn_route")
    public UserPoints route;

    public String fn_point;

    /**
     * Точка
     */
    @Expose
    public String fn_user_point;

    @ToOne(joinProperty = "fn_user_point")
    public UserPoints userPoint;

    /**
     * Тип результата
     */
    @Expose
    public long fn_type;

    /**
     * Пользователь
     */
    @Expose
    public long fn_user;

    /**
     * Дата события
     */
    @Expose
    public String d_date;

    /**
     * Примечание
     */
    @Expose
    public String c_notice;

    /**
     * Предупреждение о возможной не достоверности
     */
    @Expose
    public boolean b_warning;

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

    @Expose
    public String jb_data;

    public String dx_created;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1445092935)
    private transient ResultsDao myDao;

    @Generated(hash = 625862056)
    public Results(String id, String fn_route, String fn_point,
            String fn_user_point, long fn_type, long fn_user, String d_date,
            String c_notice, boolean b_warning, String objectOperationType,
            boolean isDelete, boolean isSynchronization, String tid,
            String blockTid, String jb_data, String dx_created) {
        this.id = id;
        this.fn_route = fn_route;
        this.fn_point = fn_point;
        this.fn_user_point = fn_user_point;
        this.fn_type = fn_type;
        this.fn_user = fn_user;
        this.d_date = d_date;
        this.c_notice = c_notice;
        this.b_warning = b_warning;
        this.objectOperationType = objectOperationType;
        this.isDelete = isDelete;
        this.isSynchronization = isSynchronization;
        this.tid = tid;
        this.blockTid = blockTid;
        this.jb_data = jb_data;
        this.dx_created = dx_created;
    }

    @Generated(hash = 991898843)
    public Results() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFn_route() {
        return this.fn_route;
    }

    public void setFn_route(String fn_route) {
        this.fn_route = fn_route;
    }

    public String getFn_point() {
        return this.fn_point;
    }

    public void setFn_point(String fn_point) {
        this.fn_point = fn_point;
    }

    public String getFn_user_point() {
        return this.fn_user_point;
    }

    public void setFn_user_point(String fn_user_point) {
        this.fn_user_point = fn_user_point;
    }

    public long getFn_type() {
        return this.fn_type;
    }

    public void setFn_type(long fn_type) {
        this.fn_type = fn_type;
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

    public String getC_notice() {
        return this.c_notice;
    }

    public void setC_notice(String c_notice) {
        this.c_notice = c_notice;
    }

    public boolean getB_warning() {
        return this.b_warning;
    }

    public void setB_warning(boolean b_warning) {
        this.b_warning = b_warning;
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

    public String getJb_data() {
        return this.jb_data;
    }

    public void setJb_data(String jb_data) {
        this.jb_data = jb_data;
    }

    public String getDx_created() {
        return this.dx_created;
    }

    public void setDx_created(String dx_created) {
        this.dx_created = dx_created;
    }

    @Generated(hash = 603420700)
    private transient String route__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 218525861)
    public UserPoints getRoute() {
        String __key = this.fn_route;
        if (route__resolvedKey == null || route__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserPointsDao targetDao = daoSession.getUserPointsDao();
            UserPoints routeNew = targetDao.load(__key);
            synchronized (this) {
                route = routeNew;
                route__resolvedKey = __key;
            }
        }
        return route;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1861761853)
    public void setRoute(UserPoints route) {
        synchronized (this) {
            this.route = route;
            fn_route = route == null ? null : route.getId();
            route__resolvedKey = fn_route;
        }
    }

    @Generated(hash = 1584222993)
    private transient String userPoint__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1107015340)
    public UserPoints getUserPoint() {
        String __key = this.fn_user_point;
        if (userPoint__resolvedKey == null || userPoint__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserPointsDao targetDao = daoSession.getUserPointsDao();
            UserPoints userPointNew = targetDao.load(__key);
            synchronized (this) {
                userPoint = userPointNew;
                userPoint__resolvedKey = __key;
            }
        }
        return userPoint;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1948866780)
    public void setUserPoint(UserPoints userPoint) {
        synchronized (this) {
            this.userPoint = userPoint;
            fn_user_point = userPoint == null ? null : userPoint.getId();
            userPoint__resolvedKey = fn_user_point;
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
    @Generated(hash = 455462623)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getResultsDao() : null;
    }
}
