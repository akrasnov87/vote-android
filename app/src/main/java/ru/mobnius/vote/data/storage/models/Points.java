package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

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
    public String f_registr_pts;

    @ToOne(joinProperty = "f_registr_pts")
    public RegistrPts registrPts;

    /**
     * Маршрут
     */
    @Expose
    public String f_route;

    @ToOne(joinProperty = "f_route")
    public Routes route;

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
    public String jb_data;

    public String dx_created;

    public int n_order;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 617316118)
    private transient PointsDao myDao;

    @Generated(hash = 383509223)
    public Points(String id, String f_registr_pts, String f_route, String c_notice,
            String c_info, String jb_data, String dx_created, int n_order) {
        this.id = id;
        this.f_registr_pts = f_registr_pts;
        this.f_route = f_route;
        this.c_notice = c_notice;
        this.c_info = c_info;
        this.jb_data = jb_data;
        this.dx_created = dx_created;
        this.n_order = n_order;
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

    public String getF_registr_pts() {
        return this.f_registr_pts;
    }

    public void setF_registr_pts(String f_registr_pts) {
        this.f_registr_pts = f_registr_pts;
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

    @Generated(hash = 130924501)
    private transient String registrPts__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 850146108)
    public RegistrPts getRegistrPts() {
        String __key = this.f_registr_pts;
        if (registrPts__resolvedKey == null || registrPts__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RegistrPtsDao targetDao = daoSession.getRegistrPtsDao();
            RegistrPts registrPtsNew = targetDao.load(__key);
            synchronized (this) {
                registrPts = registrPtsNew;
                registrPts__resolvedKey = __key;
            }
        }
        return registrPts;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 861180082)
    public void setRegistrPts(RegistrPts registrPts) {
        synchronized (this) {
            this.registrPts = registrPts;
            f_registr_pts = registrPts == null ? null : registrPts.getId();
            registrPts__resolvedKey = f_registr_pts;
        }
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
