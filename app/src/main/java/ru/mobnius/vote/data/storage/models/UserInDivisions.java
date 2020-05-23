package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

@Entity(nameInDb = "pd_userindivisions")
class UserInDivisions {

    /**
     * Идентификатор
     */
    @Id
    @Property(nameInDb = "id")
    private Long id;

    /**
     * Отделение
     */
    @Expose
    private long f_division;
    /**
     * Отделение
     */
    @ToOne(joinProperty = "f_division")
    private Divisions division;

    /**
     * Участок
     */
    @Expose
    private long f_subdivision;
    /**
     * Участок
     */
    @ToOne(joinProperty = "f_subdivision")
    private SubDivisions subdivision;

    /**
     * Пользователь
     */
    @Expose
    private long f_user;
    /**
     * Пользователь
     */
    @ToOne(joinProperty = "f_user")
    private Users user;

    private boolean sn_delete;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 314468367)
    private transient UserInDivisionsDao myDao;

    @Generated(hash = 315948132)
    public UserInDivisions(Long id, long f_division, long f_subdivision,
            long f_user, boolean sn_delete) {
        this.id = id;
        this.f_division = f_division;
        this.f_subdivision = f_subdivision;
        this.f_user = f_user;
        this.sn_delete = sn_delete;
    }

    @Generated(hash = 1106762808)
    public UserInDivisions() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getF_division() {
        return this.f_division;
    }

    public void setF_division(long f_division) {
        this.f_division = f_division;
    }

    public long getF_subdivision() {
        return this.f_subdivision;
    }

    public void setF_subdivision(long f_subdivision) {
        this.f_subdivision = f_subdivision;
    }

    public long getF_user() {
        return this.f_user;
    }

    public void setF_user(long f_user) {
        this.f_user = f_user;
    }

    public boolean getSn_delete() {
        return this.sn_delete;
    }

    public void setSn_delete(boolean sn_delete) {
        this.sn_delete = sn_delete;
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

    @Generated(hash = 1695148947)
    private transient Long subdivision__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 609228060)
    public SubDivisions getSubdivision() {
        long __key = this.f_subdivision;
        if (subdivision__resolvedKey == null
                || !subdivision__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SubDivisionsDao targetDao = daoSession.getSubDivisionsDao();
            SubDivisions subdivisionNew = targetDao.load(__key);
            synchronized (this) {
                subdivision = subdivisionNew;
                subdivision__resolvedKey = __key;
            }
        }
        return subdivision;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 430272615)
    public void setSubdivision(@NotNull SubDivisions subdivision) {
        if (subdivision == null) {
            throw new DaoException(
                    "To-one property 'f_subdivision' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.subdivision = subdivision;
            f_subdivision = subdivision.getId();
            subdivision__resolvedKey = f_subdivision;
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
    @Generated(hash = 1681276462)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserInDivisionsDao() : null;
    }
}
