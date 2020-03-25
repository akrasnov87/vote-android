package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToOne;

import ru.mobnius.vote.data.storage.models.IEntityTo;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

@Entity(nameInDb = "pd_users")
public class Users implements IEntityTo {

    public String getFullName() {
        return String.format("%s %s %s", c_lastname, c_firstname, c_patronymic);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getF_parent() {
        return this.f_parent;
    }

    public void setF_parent(long f_parent) {
        this.f_parent = f_parent;
    }

    public String getC_login() {
        return this.c_login;
    }

    public void setC_login(String c_login) {
        this.c_login = c_login;
    }

    public String getC_firstname() {
        return this.c_firstname;
    }

    public void setC_firstname(String c_firstname) {
        this.c_firstname = c_firstname;
    }

    public String getC_lastname() {
        return this.c_lastname;
    }

    public void setC_lastname(String c_lastname) {
        this.c_lastname = c_lastname;
    }

    public String getC_patronymic() {
        return this.c_patronymic;
    }

    public void setC_patronymic(String c_patronymic) {
        this.c_patronymic = c_patronymic;
    }

    public String getC_email() {
        return this.c_email;
    }

    public void setC_email(String c_email) {
        this.c_email = c_email;
    }

    public String getC_tel() {
        return this.c_tel;
    }

    public void setC_tel(String c_tel) {
        this.c_tel = c_tel;
    }

    public String getC_description() {
        return this.c_description;
    }

    public void setC_description(String c_description) {
        this.c_description = c_description;
    }

    public boolean getB_disabled() {
        return this.b_disabled;
    }

    public void setB_disabled(boolean b_disabled) {
        this.b_disabled = b_disabled;
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

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1896223229)
    public Users getParent() {
        long __key = this.f_parent;
        if (parent__resolvedKey == null || !parent__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UsersDao targetDao = daoSession.getUsersDao();
            Users parentNew = targetDao.load(__key);
            synchronized (this) {
                parent = parentNew;
                parent__resolvedKey = __key;
            }
        }
        return parent;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1082262845)
    public void setParent(@NotNull Users parent) {
        if (parent == null) {
            throw new DaoException(
                    "To-one property 'f_parent' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.parent = parent;
            f_parent = parent.getId();
            parent__resolvedKey = f_parent;
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
    @Generated(hash = 1562339706)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUsersDao() : null;
    }

    /**
     * Идентификатор
     */
    @Id
    @Expose
    @Property(nameInDb = "id")
    public Long id;

    /**
     * Родитель
     */
    public long f_parent;

    @ToOne(joinProperty = "f_parent")
    public Users parent;

    /**
     * Логин
     */
    @Expose
    public String c_login;

    /**
     * Имя
     */
    @Expose
    public String c_firstname;

    /**
     * Фамилия
     */
    @Expose
    public String c_lastname;

    /**
     * Отчество
     */
    @Expose
    public String c_patronymic;

    /**
     * Адрес эл. почты
     */
    @Expose
    public String c_email;

    /**
     * Телефон
     */
    @Expose
    public String c_tel;

    /**
     * Описание
     */
    @Expose
    public String c_description;

    /**
     * Отключен
     */
    @Expose
    public boolean b_disabled;

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
    @Generated(hash = 1073488616)
    private transient UsersDao myDao;

    @Generated(hash = 1432811130)
    public Users(Long id, long f_parent, String c_login, String c_firstname,
            String c_lastname, String c_patronymic, String c_email, String c_tel,
            String c_description, boolean b_disabled, String objectOperationType,
            boolean isDelete, boolean isSynchronization, String tid,
            String blockTid) {
        this.id = id;
        this.f_parent = f_parent;
        this.c_login = c_login;
        this.c_firstname = c_firstname;
        this.c_lastname = c_lastname;
        this.c_patronymic = c_patronymic;
        this.c_email = c_email;
        this.c_tel = c_tel;
        this.c_description = c_description;
        this.b_disabled = b_disabled;
        this.objectOperationType = objectOperationType;
        this.isDelete = isDelete;
        this.isSynchronization = isSynchronization;
        this.tid = tid;
        this.blockTid = blockTid;
    }

    @Generated(hash = 2146996206)
    public Users() {
    }

    @Generated(hash = 1293412156)
    private transient Long parent__resolvedKey;
}
