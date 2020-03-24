package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(nameInDb = "ed_output_meter_readings")
public class OutputMeterReadings implements IEntityTo {
    /**
     * Идентификатор
     */
    @Id
    @Expose
    public String id;

    @Expose
    public String fn_meter_reading;

    @ToOne(joinProperty = "fn_meter_reading")
    public InputMeterReadings meterReading;

    @Expose
    public String fn_route;

    @ToOne(joinProperty = "fn_route")
    public Points route;

    @Expose
    public String fn_point;

    @ToOne(joinProperty = "fn_point")
    public Points point;

    @Expose
    public String fn_user_point;

    @ToOne(joinProperty = "fn_user_point")
    public UserPoints userPoint;

    @Expose
    public String fn_result;

    @ToOne(joinProperty = "fn_result")
    public Results result;

    @Expose
    public double n_value;

    @Expose
    public String d_date;

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

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 98463702)
    private transient OutputMeterReadingsDao myDao;

    @Generated(hash = 515201092)
    public OutputMeterReadings(String id, String fn_meter_reading, String fn_route,
            String fn_point, String fn_user_point, String fn_result, double n_value,
            String d_date, String objectOperationType, boolean isDelete,
            boolean isSynchronization, String tid, String blockTid) {
        this.id = id;
        this.fn_meter_reading = fn_meter_reading;
        this.fn_route = fn_route;
        this.fn_point = fn_point;
        this.fn_user_point = fn_user_point;
        this.fn_result = fn_result;
        this.n_value = n_value;
        this.d_date = d_date;
        this.objectOperationType = objectOperationType;
        this.isDelete = isDelete;
        this.isSynchronization = isSynchronization;
        this.tid = tid;
        this.blockTid = blockTid;
    }

    @Generated(hash = 328632131)
    public OutputMeterReadings() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFn_meter_reading() {
        return this.fn_meter_reading;
    }

    public void setFn_meter_reading(String fn_meter_reading) {
        this.fn_meter_reading = fn_meter_reading;
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

    public String getFn_result() {
        return this.fn_result;
    }

    public void setFn_result(String fn_result) {
        this.fn_result = fn_result;
    }

    public double getN_value() {
        return this.n_value;
    }

    public void setN_value(double n_value) {
        this.n_value = n_value;
    }

    public String getD_date() {
        return this.d_date;
    }

    public void setD_date(String d_date) {
        this.d_date = d_date;
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

    @Generated(hash = 1691412782)
    private transient String meterReading__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1195376238)
    public InputMeterReadings getMeterReading() {
        String __key = this.fn_meter_reading;
        if (meterReading__resolvedKey == null
                || meterReading__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            InputMeterReadingsDao targetDao = daoSession.getInputMeterReadingsDao();
            InputMeterReadings meterReadingNew = targetDao.load(__key);
            synchronized (this) {
                meterReading = meterReadingNew;
                meterReading__resolvedKey = __key;
            }
        }
        return meterReading;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2055382066)
    public void setMeterReading(InputMeterReadings meterReading) {
        synchronized (this) {
            this.meterReading = meterReading;
            fn_meter_reading = meterReading == null ? null : meterReading.getId();
            meterReading__resolvedKey = fn_meter_reading;
        }
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

    @Generated(hash = 1347497366)
    private transient String result__resolvedKey;

    @Generated(hash = 603420700)
    private transient String route__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1306757196)
    public Results getResult() {
        String __key = this.fn_result;
        if (result__resolvedKey == null || result__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ResultsDao targetDao = daoSession.getResultsDao();
            Results resultNew = targetDao.load(__key);
            synchronized (this) {
                result = resultNew;
                result__resolvedKey = __key;
            }
        }
        return result;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1153698949)
    public void setResult(Results result) {
        synchronized (this) {
            this.result = result;
            fn_result = result == null ? null : result.getId();
            result__resolvedKey = fn_result;
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

    public String getFn_route() {
        return this.fn_route;
    }

    public void setFn_route(String fn_route) {
        this.fn_route = fn_route;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 711248306)
    public Points getRoute() {
        String __key = this.fn_route;
        if (route__resolvedKey == null || route__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PointsDao targetDao = daoSession.getPointsDao();
            Points routeNew = targetDao.load(__key);
            synchronized (this) {
                route = routeNew;
                route__resolvedKey = __key;
            }
        }
        return route;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 145510293)
    public void setRoute(Points route) {
        synchronized (this) {
            this.route = route;
            fn_route = route == null ? null : route.getId();
            route__resolvedKey = fn_route;
        }
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 482027981)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getOutputMeterReadingsDao() : null;
    }
}
