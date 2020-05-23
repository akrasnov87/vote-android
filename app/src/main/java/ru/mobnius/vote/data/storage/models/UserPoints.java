package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

@SuppressWarnings({"StringEquality", "unused"})
@Entity(nameInDb = "cd_user_points")
public class UserPoints implements IEntityTo {

    /**
     * Идентификатор
     */
    @Id
    @Expose
    public String id;

    @Expose
    public String fn_point;

    @ToOne(joinProperty = "fn_point")
    private Points point;

    /**
     * Пользователь
     */
    @Expose
    public long fn_user;

    @ToOne(joinProperty = "fn_user")
    private Users user;

    private String c_user;

    /**
     * Маршрут
     */
    @Expose
    public String fn_route;

    @ToOne(joinProperty = "fn_route")
    private Routes route;

    /**
     * тип
     */
    @Expose
    public long fn_type;

    @Expose
    public String jb_tel;

    @Expose
    private String jb_email;

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
     * Примечание
     */
    @Expose
    private String c_notice;

    /**
     * Подтверждено
     */
    @Expose
    private boolean b_check;

    @Expose
    public String jb_data;

    /**
     * дата проверки
     */
    @Expose
    private String d_date_check;


    /**
     * дата создания записи
     */
    @Expose
    public String d_date;

    private String dx_created;

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
    @Generated(hash = 308265493)
    private transient UserPointsDao myDao;

    @Generated(hash = 1614219086)
    public UserPoints(String id, String fn_point, long fn_user, String c_user,
            String fn_route, long fn_type, String jb_tel, String jb_email,
            double n_latitude, double n_longitude, String c_notice, boolean b_check,
            String jb_data, String d_date_check, String d_date, String dx_created,
            String objectOperationType, boolean isDelete, boolean isSynchronization,
            String tid, String blockTid) {
        this.id = id;
        this.fn_point = fn_point;
        this.fn_user = fn_user;
        this.c_user = c_user;
        this.fn_route = fn_route;
        this.fn_type = fn_type;
        this.jb_tel = jb_tel;
        this.jb_email = jb_email;
        this.n_latitude = n_latitude;
        this.n_longitude = n_longitude;
        this.c_notice = c_notice;
        this.b_check = b_check;
        this.jb_data = jb_data;
        this.d_date_check = d_date_check;
        this.d_date = d_date;
        this.dx_created = dx_created;
        this.objectOperationType = objectOperationType;
        this.isDelete = isDelete;
        this.isSynchronization = isSynchronization;
        this.tid = tid;
        this.blockTid = blockTid;
    }

    @Generated(hash = 1707024009)
    public UserPoints() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFn_point() {
        return this.fn_point;
    }

    public void setFn_point(String fn_point) {
        this.fn_point = fn_point;
    }

    public long getFn_user() {
        return this.fn_user;
    }

    public void setFn_user(long fn_user) {
        this.fn_user = fn_user;
    }

    public String getC_user() {
        return this.c_user;
    }

    public void setC_user(String c_user) {
        this.c_user = c_user;
    }

    public String getFn_route() {
        return this.fn_route;
    }

    public void setFn_route(String fn_route) {
        this.fn_route = fn_route;
    }

    public long getFn_type() {
        return this.fn_type;
    }

    public void setFn_type(long fn_type) {
        this.fn_type = fn_type;
    }

    public String getJb_tel() {
        return this.jb_tel;
    }

    public void setJb_tel(String jb_tel) {
        this.jb_tel = jb_tel;
    }

    public String getJb_email() {
        return this.jb_email;
    }

    public void setJb_email(String jb_email) {
        this.jb_email = jb_email;
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

    public String getC_notice() {
        return this.c_notice;
    }

    public void setC_notice(String c_notice) {
        this.c_notice = c_notice;
    }

    public boolean getB_check() {
        return this.b_check;
    }

    public void setB_check(boolean b_check) {
        this.b_check = b_check;
    }

    public String getJb_data() {
        return this.jb_data;
    }

    public void setJb_data(String jb_data) {
        this.jb_data = jb_data;
    }

    public String getD_date_check() {
        return this.d_date_check;
    }

    public void setD_date_check(String d_date_check) {
        this.d_date_check = d_date_check;
    }

    public String getD_date() {
        return this.d_date;
    }

    public void setD_date(String d_date) {
        this.d_date = d_date;
    }

    public String getDx_created() {
        return this.dx_created;
    }

    public void setDx_created(String dx_created) {
        this.dx_created = dx_created;
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

    @Generated(hash = 2103900843)
    private transient String point__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1154150153)
    public Points getPoint() {
        String __key = this.fn_point;
        if (point__resolvedKey == null || point__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PointsDao targetDao = daoSession.getPointsDao();
            Points pointNew = targetDao.load(__key);
            synchronized (this) {
                point = pointNew;
                point__resolvedKey = __key;
            }
        }
        return point;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1368760242)
    public void setPoint(Points point) {
        synchronized (this) {
            this.point = point;
            fn_point = point == null ? null : point.getId();
            point__resolvedKey = fn_point;
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
    @Generated(hash = 51998777)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserPointsDao() : null;
    }
}
