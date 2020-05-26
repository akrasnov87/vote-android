package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

@SuppressWarnings({"unused", "StringEquality"})
@Entity(nameInDb = "cd_route_history")
public class RouteHistory implements IEntityTo {

    /**
     * Примечание
     */
    @Expose
    public String c_notice;

    /**
     * Дата изменения
     */
    @Expose
    public String d_date;

    /**
     * Статус
     */
    @Expose
    public long fn_status;

    @ToOne(joinProperty = "fn_status")
    private RouteStatuses status;

    /**
     * Задание
     */
    @Expose
    public String fn_route;

    @ToOne(joinProperty = "fn_route")
    private Routes route;

    /**
     * Пользователь
     */
    @Expose
    public long fn_user;

    @ToOne(joinProperty = "fn_user")
    private Users user;

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

    private String dx_created;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1579188164)
    private transient RouteHistoryDao myDao;

    @Generated(hash = 1673441960)
    public RouteHistory(String c_notice, String d_date, long fn_status,
            String fn_route, long fn_user, String id, String objectOperationType,
            boolean isDelete, boolean isSynchronization, String tid,
            String blockTid, String dx_created) {
        this.c_notice = c_notice;
        this.d_date = d_date;
        this.fn_status = fn_status;
        this.fn_route = fn_route;
        this.fn_user = fn_user;
        this.id = id;
        this.objectOperationType = objectOperationType;
        this.isDelete = isDelete;
        this.isSynchronization = isSynchronization;
        this.tid = tid;
        this.blockTid = blockTid;
        this.dx_created = dx_created;
    }

    @Generated(hash = 650745422)
    public RouteHistory() {
    }

    public String getC_notice() {
        return this.c_notice;
    }

    public void setC_notice(String c_notice) {
        this.c_notice = c_notice;
    }

    public String getD_date() {
        return this.d_date;
    }

    public void setD_date(String d_date) {
        this.d_date = d_date;
    }

    public long getFn_status() {
        return this.fn_status;
    }

    public void setFn_status(long fn_status) {
        this.fn_status = fn_status;
    }

    public String getFn_route() {
        return this.fn_route;
    }

    public void setFn_route(String fn_route) {
        this.fn_route = fn_route;
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

    @Generated(hash = 2138050738)
    private transient Long status__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 653404116)
    public RouteStatuses getStatus() {
        long __key = this.fn_status;
        if (status__resolvedKey == null || !status__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RouteStatusesDao targetDao = daoSession.getRouteStatusesDao();
            RouteStatuses statusNew = targetDao.load(__key);
            synchronized (this) {
                status = statusNew;
                status__resolvedKey = __key;
            }
        }
        return status;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 908244810)
    public void setStatus(@NotNull RouteStatuses status) {
        if (status == null) {
            throw new DaoException(
                    "To-one property 'fn_status' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.status = status;
            fn_status = status.getId();
            status__resolvedKey = fn_status;
        }
    }

    @Generated(hash = 603420700)
    private transient String route__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2146022390)
    public Routes getRoute() {
        String __key = this.fn_route;
        if (route__resolvedKey == null || route__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RoutesDao targetDao = daoSession.getRoutesDao();
            Routes routeNew = targetDao.load(__key);
            synchronized (this) {
                route = routeNew;
                route__resolvedKey = __key;
            }
        }
        return route;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 656419102)
    public void setRoute(Routes route) {
        synchronized (this) {
            this.route = route;
            fn_route = route == null ? null : route.getId();
            route__resolvedKey = fn_route;
        }
    }

    @Generated(hash = 251390918)
    private transient Long user__resolvedKey;

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
    @Generated(hash = 222797149)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRouteHistoryDao() : null;
    }
}
