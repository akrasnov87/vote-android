package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

@Entity(nameInDb = "ed_registr_pts")
public class RegistrPts {
    /**
     * Идентификатор
     */
    @Id
    @Expose
    public String id;

    @Expose
    public String c_appartament_num;

    @Expose
    public int n_appartament_num;

    @Expose
    public String c_house_num;

    @Expose
    public int n_house_num;

    @Expose
    private String jb_tel;

    @Expose
    private String jb_email;

    @Expose
    public String c_fio;

    /**
     * Адрес
     */
    @Expose
    public String c_address;

    /**
     * Широта
     */
    @Expose
    private double n_latitude;

    /**
     * Долгота
     */
    @Expose
    private double n_longitude;

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
     * Участок
     */
    @Expose
    public long f_subdivision;
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

    @Expose
    private boolean b_disabled;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 690484894)
    private transient RegistrPtsDao myDao;

    @Generated(hash = 835053125)
    public RegistrPts(String id, String c_appartament_num, int n_appartament_num,
            String c_house_num, int n_house_num, String jb_tel, String jb_email,
            String c_fio, String c_address, double n_latitude, double n_longitude,
            long f_division, long f_subdivision, long f_user, boolean b_disabled) {
        this.id = id;
        this.c_appartament_num = c_appartament_num;
        this.n_appartament_num = n_appartament_num;
        this.c_house_num = c_house_num;
        this.n_house_num = n_house_num;
        this.jb_tel = jb_tel;
        this.jb_email = jb_email;
        this.c_fio = c_fio;
        this.c_address = c_address;
        this.n_latitude = n_latitude;
        this.n_longitude = n_longitude;
        this.f_division = f_division;
        this.f_subdivision = f_subdivision;
        this.f_user = f_user;
        this.b_disabled = b_disabled;
    }

    @Generated(hash = 925207867)
    public RegistrPts() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getC_appartament_num() {
        return this.c_appartament_num;
    }

    public void setC_appartament_num(String c_appartament_num) {
        this.c_appartament_num = c_appartament_num;
    }

    public int getN_appartament_num() {
        return this.n_appartament_num;
    }

    public void setN_appartament_num(int n_appartament_num) {
        this.n_appartament_num = n_appartament_num;
    }

    public String getC_house_num() {
        return this.c_house_num;
    }

    public void setC_house_num(String c_house_num) {
        this.c_house_num = c_house_num;
    }

    public int getN_house_num() {
        return this.n_house_num;
    }

    public void setN_house_num(int n_house_num) {
        this.n_house_num = n_house_num;
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

    public String getC_fio() {
        return this.c_fio;
    }

    public void setC_fio(String c_fio) {
        this.c_fio = c_fio;
    }

    public String getC_address() {
        return this.c_address;
    }

    public void setC_address(String c_address) {
        this.c_address = c_address;
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
    @Generated(hash = 991380298)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRegistrPtsDao() : null;
    }
}
