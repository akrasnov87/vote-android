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

@SuppressWarnings("unused")
@Entity(nameInDb = "pd_users")
public class Users implements IEntityTo {
    /**
     * Идентификатор
     */
    @Id
    @Expose
    @Property(nameInDb = "id")
    private Long id;

    /**
     * Родитель
     */
    private long f_parent;

    @ToOne(joinProperty = "f_parent")
    private Users parent;

    /**
     * Логин
     */
    @Expose
    private String c_login;

    @Expose
    private String c_fio;

    /**
     * Адрес эл. почты
     */
    @Expose
    private String c_email;

    /**
     * Телефон
     */
    @Expose
    private String c_tel;

    /**
     * Описание
     */
    @Expose
    private String c_description;

    /**
     * Отключен
     */
    @Expose
    private boolean b_disabled;

    @Expose
    public Integer n_uik;

    @Expose
    public Integer f_subdivision;

    /**
     * Тип операции надл объектом
     */
    private String objectOperationType;

    /**
     * Запись была удалена или нет
     */
    private boolean isDelete;

    /**
     * Была произведена синхронизация или нет
     */
    private boolean isSynchronization;

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
    @Generated(hash = 1073488616)
    private transient UsersDao myDao;

    @Generated(hash = 1488733906)
    public Users(Long id, long f_parent, String c_login, String c_fio,
            String c_email, String c_tel, String c_description, boolean b_disabled,
            Integer n_uik, Integer f_subdivision, String objectOperationType,
            boolean isDelete, boolean isSynchronization, String tid,
            String blockTid) {
        this.id = id;
        this.f_parent = f_parent;
        this.c_login = c_login;
        this.c_fio = c_fio;
        this.c_email = c_email;
        this.c_tel = c_tel;
        this.c_description = c_description;
        this.b_disabled = b_disabled;
        this.n_uik = n_uik;
        this.f_subdivision = f_subdivision;
        this.objectOperationType = objectOperationType;
        this.isDelete = isDelete;
        this.isSynchronization = isSynchronization;
        this.tid = tid;
        this.blockTid = blockTid;
    }

    @Generated(hash = 2146996206)
    public Users() {
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

    public String getC_fio() {
        return this.c_fio;
    }

    public void setC_fio(String c_fio) {
        this.c_fio = c_fio;
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

    public Integer getN_uik() {
        return this.n_uik;
    }

    public void setN_uik(Integer n_uik) {
        this.n_uik = n_uik;
    }

    public Integer getF_subdivision() {
        return this.f_subdivision;
    }

    public void setF_subdivision(Integer f_subdivision) {
        this.f_subdivision = f_subdivision;
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

    @Generated(hash = 1293412156)
    private transient Long parent__resolvedKey;

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
}
