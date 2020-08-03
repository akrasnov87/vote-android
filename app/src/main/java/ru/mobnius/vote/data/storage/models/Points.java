package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;

import org.greenrobot.greendao.DaoException;

@SuppressWarnings({"unused", "StringEquality"})
@Entity(nameInDb = "cd_points")
public class Points {

    /**
     * Идентификатор
     */
    @Id
    @Expose
    public String id;

    /**
     * Адрес
     */
    @Expose
    public String f_appartament;

    /**
     * Маршрут
     */
    @Expose
    public String f_route;

    @ToOne(joinProperty = "f_route")
    private Routes route;

    /**
     * Примечание
     */
    @Expose
    public String c_notice;

    /**
     * Информация
     */
    @Expose
    public String c_info;

    @Expose
    private String jb_data;

    private String dx_created;

    private int n_order;

    public Integer n_priority;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 617316118)
    private transient PointsDao myDao;

    @Generated(hash = 818458000)
    public Points(String id, String f_appartament, String f_route, String c_notice,
            String c_info, String jb_data, String dx_created, int n_order,
            Integer n_priority) {
        this.id = id;
        this.f_appartament = f_appartament;
        this.f_route = f_route;
        this.c_notice = c_notice;
        this.c_info = c_info;
        this.jb_data = jb_data;
        this.dx_created = dx_created;
        this.n_order = n_order;
        this.n_priority = n_priority;
    }

    @Generated(hash = 1607589943)
    public Points() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getF_appartament() {
        return this.f_appartament;
    }

    public void setF_appartament(String f_appartament) {
        this.f_appartament = f_appartament;
    }

    public String getF_route() {
        return this.f_route;
    }

    public void setF_route(String f_route) {
        this.f_route = f_route;
    }

    public String getC_notice() {
        return this.c_notice;
    }

    public void setC_notice(String c_notice) {
        this.c_notice = c_notice;
    }

    public String getC_info() {
        return this.c_info;
    }

    public void setC_info(String c_info) {
        this.c_info = c_info;
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

    public int getN_order() {
        return this.n_order;
    }

    public void setN_order(int n_order) {
        this.n_order = n_order;
    }

    public Integer getN_priority() {
        return this.n_priority;
    }

    public void setN_priority(Integer n_priority) {
        this.n_priority = n_priority;
    }

    @Generated(hash = 603420700)
    private transient String route__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1667685995)
    public Routes getRoute() {
        String __key = this.f_route;
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
    @Generated(hash = 610281170)
    public void setRoute(Routes route) {
        synchronized (this) {
            this.route = route;
            f_route = route == null ? null : route.getId();
            route__resolvedKey = f_route;
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
    @Generated(hash = 596903735)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPointsDao() : null;
    }
}
