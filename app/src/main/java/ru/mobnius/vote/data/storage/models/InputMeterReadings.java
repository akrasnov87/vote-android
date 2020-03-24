package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(nameInDb = "ed_input_meter_readings")
public class InputMeterReadings {
    /**
     * Идентификатор
     */
    @Id
    @Expose
    public String id;

    @Expose
    public String f_point;

    @ToOne(joinProperty = "f_point")
    public Points point;

    @Expose
    public double n_value_prev;

    @Expose
    public String d_date_prev;

    @Expose
    public Long f_time_zone;

    @ToOne(joinProperty = "f_time_zone")
    public TimeZones timeZone;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 659778777)
    private transient InputMeterReadingsDao myDao;

    @Generated(hash = 507959028)
    public InputMeterReadings(String id, String f_point, double n_value_prev,
            String d_date_prev, Long f_time_zone) {
        this.id = id;
        this.f_point = f_point;
        this.n_value_prev = n_value_prev;
        this.d_date_prev = d_date_prev;
        this.f_time_zone = f_time_zone;
    }

    @Generated(hash = 2015201973)
    public InputMeterReadings() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getF_point() {
        return this.f_point;
    }

    public void setF_point(String f_point) {
        this.f_point = f_point;
    }

    public double getN_value_prev() {
        return this.n_value_prev;
    }

    public void setN_value_prev(double n_value_prev) {
        this.n_value_prev = n_value_prev;
    }

    public String getD_date_prev() {
        return this.d_date_prev;
    }

    public void setD_date_prev(String d_date_prev) {
        this.d_date_prev = d_date_prev;
    }

    public Long getF_time_zone() {
        return this.f_time_zone;
    }

    public void setF_time_zone(Long f_time_zone) {
        this.f_time_zone = f_time_zone;
    }

    @Generated(hash = 2103900843)
    private transient String point__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1094102953)
    public Points getPoint() {
        String __key = this.f_point;
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
    @Generated(hash = 1965492810)
    public void setPoint(Points point) {
        synchronized (this) {
            this.point = point;
            f_point = point == null ? null : point.getId();
            point__resolvedKey = f_point;
        }
    }

    @Generated(hash = 2107931269)
    private transient Long timeZone__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1194595033)
    public TimeZones getTimeZone() {
        Long __key = this.f_time_zone;
        if (timeZone__resolvedKey == null || !timeZone__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TimeZonesDao targetDao = daoSession.getTimeZonesDao();
            TimeZones timeZoneNew = targetDao.load(__key);
            synchronized (this) {
                timeZone = timeZoneNew;
                timeZone__resolvedKey = __key;
            }
        }
        return timeZone;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1322703428)
    public void setTimeZone(TimeZones timeZone) {
        synchronized (this) {
            this.timeZone = timeZone;
            f_time_zone = timeZone == null ? null : timeZone.getId();
            timeZone__resolvedKey = f_time_zone;
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
    @Generated(hash = 1351921240)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getInputMeterReadingsDao() : null;
    }
}
