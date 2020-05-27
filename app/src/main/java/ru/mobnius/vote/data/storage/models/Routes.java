package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

@SuppressWarnings("unused")
@Entity(nameInDb = "cd_routes")
public class Routes {

    /**
     * Примечание
     */
    @Expose
    public String c_notice;

    /**
     * Номер задания
     */
    @Expose
    public String c_number;

    /**
     * Дата создания
     */
    @Expose
    public String d_date;

    /**
     * Дата завершения выполнения
     */
    @Expose
    public String d_date_end;

    /**
     * Дата начала выполнения
     */
    @Expose
    public String d_date_start;

    /**
     * продлен
     */
    @Expose
    public boolean b_extended;

    /**
     * продлен до
     */
    @Expose
    public String d_extended;

    /**
     * тип маршрута
     */
    @Expose
    public long f_type;

    @ToOne(joinProperty = "f_type")
    private RouteTypes type;

    /**
     * Идентификатор
     */
    @Id
    @Expose
    public String id;

    @Expose
    private String jb_data;

    private String dx_created;

    public int n_count;

    public int n_order;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 415398192)
    private transient RoutesDao myDao;

    @Generated(hash = 393336380)
    public Routes(String c_notice, String c_number, String d_date,
            String d_date_end, String d_date_start, boolean b_extended,
            String d_extended, long f_type, String id, String jb_data,
            String dx_created, int n_count, int n_order) {
        this.c_notice = c_notice;
        this.c_number = c_number;
        this.d_date = d_date;
        this.d_date_end = d_date_end;
        this.d_date_start = d_date_start;
        this.b_extended = b_extended;
        this.d_extended = d_extended;
        this.f_type = f_type;
        this.id = id;
        this.jb_data = jb_data;
        this.dx_created = dx_created;
        this.n_count = n_count;
        this.n_order = n_order;
    }

    @Generated(hash = 771875763)
    public Routes() {
    }

    public String getC_notice() {
        return this.c_notice;
    }

    public void setC_notice(String c_notice) {
        this.c_notice = c_notice;
    }

    public String getC_number() {
        return this.c_number;
    }

    public void setC_number(String c_number) {
        this.c_number = c_number;
    }

    public String getD_date() {
        return this.d_date;
    }

    public void setD_date(String d_date) {
        this.d_date = d_date;
    }

    public String getD_date_end() {
        return this.d_date_end;
    }

    public void setD_date_end(String d_date_end) {
        this.d_date_end = d_date_end;
    }

    public String getD_date_start() {
        return this.d_date_start;
    }

    public void setD_date_start(String d_date_start) {
        this.d_date_start = d_date_start;
    }

    public boolean getB_extended() {
        return this.b_extended;
    }

    public void setB_extended(boolean b_extended) {
        this.b_extended = b_extended;
    }

    public String getD_extended() {
        return this.d_extended;
    }

    public void setD_extended(String d_extended) {
        this.d_extended = d_extended;
    }

    public long getF_type() {
        return this.f_type;
    }

    public void setF_type(long f_type) {
        this.f_type = f_type;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getN_count() {
        return this.n_count;
    }

    public void setN_count(int n_count) {
        this.n_count = n_count;
    }

    public int getN_order() {
        return this.n_order;
    }

    public void setN_order(int n_order) {
        this.n_order = n_order;
    }

    @Generated(hash = 506996655)
    private transient Long type__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1808695676)
    public RouteTypes getType() {
        long __key = this.f_type;
        if (type__resolvedKey == null || !type__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RouteTypesDao targetDao = daoSession.getRouteTypesDao();
            RouteTypes typeNew = targetDao.load(__key);
            synchronized (this) {
                type = typeNew;
                type__resolvedKey = __key;
            }
        }
        return type;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1241683467)
    public void setType(@NotNull RouteTypes type) {
        if (type == null) {
            throw new DaoException(
                    "To-one property 'f_type' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.type = type;
            f_type = type.getId();
            type__resolvedKey = f_type;
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
    @Generated(hash = 1367887565)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRoutesDao() : null;
    }
}
