package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

@SuppressWarnings("unused")
@Entity(nameInDb = "sd_subdivisions")
public class SubDivisions {

    /**
     * Наименование
     */
    @Expose
    public String c_name;

    /**
     * Отделение
     */
    @Expose
    public long f_division;
    /**
     * Отделение
     */
    @ToOne(joinProperty = "f_division")
    private Divisions division;

    /**
     * Идентификатор
     */
    @Id
    @Property(nameInDb = "id")
    public Long id;

    /**
     * Код
     */
    @Expose
    private long n_code;

    /**
     * отключено
     */
    @Expose
    private boolean b_disabled;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1741942699)
    private transient SubDivisionsDao myDao;

    @Generated(hash = 1787600562)
    public SubDivisions(String c_name, long f_division, Long id, long n_code,
            boolean b_disabled) {
        this.c_name = c_name;
        this.f_division = f_division;
        this.id = id;
        this.n_code = n_code;
        this.b_disabled = b_disabled;
    }

    @Generated(hash = 1669291273)
    public SubDivisions() {
    }

    public String getC_name() {
        return this.c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public long getF_division() {
        return this.f_division;
    }

    public void setF_division(long f_division) {
        this.f_division = f_division;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getN_code() {
        return this.n_code;
    }

    public void setN_code(long n_code) {
        this.n_code = n_code;
    }

    public boolean getB_disabled() {
        return this.b_disabled;
    }

    public void setB_disabled(boolean b_disabled) {
        this.b_disabled = b_disabled;
    }

    @Generated(hash = 1181341186)
    private transient Long division__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 32668470)
    public Divisions getDivision() {
        long __key = this.f_division;
        if (division__resolvedKey == null || !division__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DivisionsDao targetDao = daoSession.getDivisionsDao();
            Divisions divisionNew = targetDao.load(__key);
            synchronized (this) {
                division = divisionNew;
                division__resolvedKey = __key;
            }
        }
        return division;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1860193696)
    public void setDivision(@NotNull Divisions division) {
        if (division == null) {
            throw new DaoException(
                    "To-one property 'f_division' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.division = division;
            f_division = division.getId();
            division__resolvedKey = f_division;
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
    @Generated(hash = 1205186073)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSubDivisionsDao() : null;
    }
}
