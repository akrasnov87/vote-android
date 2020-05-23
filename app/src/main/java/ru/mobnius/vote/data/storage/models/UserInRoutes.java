package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

@SuppressWarnings("StringEquality")
@Entity(nameInDb = "cd_userinroutes")
class UserInRoutes {

    /**
     * Является главным
     */
    @Expose
    private boolean b_main;

    /**
     * Задание
     */
    @Expose
    private String f_route;

    @ToOne(joinProperty = "f_route")
    private Routes route;

    /**
     * Пользователь
     */
    @Expose
    private long f_user;

    @ToOne(joinProperty = "f_user")
    private Users user;

    /**
     * Идентифиактор
     */
    @Id
    @Expose
    private String id;

    private String dx_created;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1195871307)
    private transient UserInRoutesDao myDao;

    @Generated(hash = 1198027940)
    public UserInRoutes(boolean b_main, String f_route, long f_user, String id,
            String dx_created) {
        this.b_main = b_main;
        this.f_route = f_route;
        this.f_user = f_user;
        this.id = id;
        this.dx_created = dx_created;
    }

    @Generated(hash = 368274361)
    public UserInRoutes() {
    }

    public boolean getB_main() {
        return this.b_main;
    }

    public void setB_main(boolean b_main) {
        this.b_main = b_main;
    }

    public String getF_route() {
        return this.f_route;
    }

    public void setF_route(String f_route) {
        this.f_route = f_route;
    }

    public long getF_user() {
        return this.f_user;
    }

    public void setF_user(long f_user) {
        this.f_user = f_user;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Generated(hash = 251390918)
    private transient Long user__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1941823327)
    public Users getUser() {
        long __key = this.f_user;
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
    @Generated(hash = 854887694)
    public void setUser(@NotNull Users user) {
        if (user == null) {
            throw new DaoException(
                    "To-one property 'f_user' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.user = user;
            f_user = user.getId();
            user__resolvedKey = f_user;
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
    @Generated(hash = 2136110590)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserInRoutesDao() : null;
    }
}
