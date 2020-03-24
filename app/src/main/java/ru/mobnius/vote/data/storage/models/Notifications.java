package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

@Entity(nameInDb = "cd_notifications")
public class Notifications implements IEntityTo {

    /**
     * Идентификатор
     */
    @Id
    @Property(nameInDb = "id")
    public Long id;

    /**
     * Адресат
     */
    @Expose
    public long fn_user_to;
    /**
     * Адресат
     */
    @ToOne(joinProperty = "fn_user_to")
    public Users userTo;

    /**
     * Заголовок
     */
    @Expose
    public String c_title;

    /**
     * Сообщение
     */
    @Expose
    public String c_message;

    /**
     * Параметры
     */
    @Expose
    public String c_data;

    /**
     * Создан
     */
    @Expose
    public String d_date;

    /**
     * Изменен
     */
    @Expose
    public String d_changed;

    /**
     * От кого
     */
    @Expose
    public String c_author;

    /**
     * От кого
     */
    @Expose
    public long fn_user_from;

    /**
     * От кого
     */
    @ToOne(joinProperty = "fn_user_from")
    public Users userFrom;

    /**
     * Прочитан
     */
    @Expose
    public boolean b_readed;

    /**
     * Доставлено
     */
    @Expose
    public boolean b_sended;

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

    public String dx_created;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 198336900)
    private transient NotificationsDao myDao;

    @Generated(hash = 1975153786)
    public Notifications(Long id, long fn_user_to, String c_title, String c_message,
            String c_data, String d_date, String d_changed, String c_author,
            long fn_user_from, boolean b_readed, boolean b_sended,
            String objectOperationType, boolean isDelete, boolean isSynchronization,
            String tid, String blockTid, String dx_created) {
        this.id = id;
        this.fn_user_to = fn_user_to;
        this.c_title = c_title;
        this.c_message = c_message;
        this.c_data = c_data;
        this.d_date = d_date;
        this.d_changed = d_changed;
        this.c_author = c_author;
        this.fn_user_from = fn_user_from;
        this.b_readed = b_readed;
        this.b_sended = b_sended;
        this.objectOperationType = objectOperationType;
        this.isDelete = isDelete;
        this.isSynchronization = isSynchronization;
        this.tid = tid;
        this.blockTid = blockTid;
        this.dx_created = dx_created;
    }

    @Generated(hash = 685557990)
    public Notifications() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getFn_user_to() {
        return this.fn_user_to;
    }

    public void setFn_user_to(long fn_user_to) {
        this.fn_user_to = fn_user_to;
    }

    public String getC_title() {
        return this.c_title;
    }

    public void setC_title(String c_title) {
        this.c_title = c_title;
    }

    public String getC_message() {
        return this.c_message;
    }

    public void setC_message(String c_message) {
        this.c_message = c_message;
    }

    public String getC_data() {
        return this.c_data;
    }

    public void setC_data(String c_data) {
        this.c_data = c_data;
    }

    public String getD_date() {
        return this.d_date;
    }

    public void setD_date(String d_date) {
        this.d_date = d_date;
    }

    public String getD_changed() {
        return this.d_changed;
    }

    public void setD_changed(String d_changed) {
        this.d_changed = d_changed;
    }

    public String getC_author() {
        return this.c_author;
    }

    public void setC_author(String c_author) {
        this.c_author = c_author;
    }

    public long getFn_user_from() {
        return this.fn_user_from;
    }

    public void setFn_user_from(long fn_user_from) {
        this.fn_user_from = fn_user_from;
    }

    public boolean getB_readed() {
        return this.b_readed;
    }

    public void setB_readed(boolean b_readed) {
        this.b_readed = b_readed;
    }

    public boolean getB_sended() {
        return this.b_sended;
    }

    public void setB_sended(boolean b_sended) {
        this.b_sended = b_sended;
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

    public String getDx_created() {
        return this.dx_created;
    }

    public void setDx_created(String dx_created) {
        this.dx_created = dx_created;
    }

    @Generated(hash = 453348867)
    private transient Long userTo__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 442850034)
    public Users getUserTo() {
        long __key = this.fn_user_to;
        if (userTo__resolvedKey == null || !userTo__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UsersDao targetDao = daoSession.getUsersDao();
            Users userToNew = targetDao.load(__key);
            synchronized (this) {
                userTo = userToNew;
                userTo__resolvedKey = __key;
            }
        }
        return userTo;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 470915419)
    public void setUserTo(@NotNull Users userTo) {
        if (userTo == null) {
            throw new DaoException(
                    "To-one property 'fn_user_to' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.userTo = userTo;
            fn_user_to = userTo.getId();
            userTo__resolvedKey = fn_user_to;
        }
    }

    @Generated(hash = 778528812)
    private transient Long userFrom__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 228021139)
    public Users getUserFrom() {
        long __key = this.fn_user_from;
        if (userFrom__resolvedKey == null || !userFrom__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UsersDao targetDao = daoSession.getUsersDao();
            Users userFromNew = targetDao.load(__key);
            synchronized (this) {
                userFrom = userFromNew;
                userFrom__resolvedKey = __key;
            }
        }
        return userFrom;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 945131041)
    public void setUserFrom(@NotNull Users userFrom) {
        if (userFrom == null) {
            throw new DaoException(
                    "To-one property 'fn_user_from' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.userFrom = userFrom;
            fn_user_from = userFrom.getId();
            userFrom__resolvedKey = fn_user_from;
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
    @Generated(hash = 349072857)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getNotificationsDao() : null;
    }
}
