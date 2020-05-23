package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.UUID;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

import org.greenrobot.greendao.annotation.NotNull;

@SuppressWarnings("unused")
@Entity(nameInDb = "ad_mobile_indicators")
public class MobileIndicators implements IEntityTo {

    public MobileIndicators(){
        id = UUID.randomUUID().toString();
    }

    @Generated(hash = 557345448)
    public MobileIndicators(boolean b_isonline, String c_network_type,
            String d_date, long fn_user, String id, long n_battery_level,
            long n_phone_memory, long n_ram, long n_sd_card_memory, long n_time,
            long n_used_phone_memory, long n_used_ram, long n_used_sd_card_memory,
            String objectOperationType, boolean isDelete, boolean isSynchronization,
            String tid, String blockTid, String dx_created) {
        this.b_isonline = b_isonline;
        this.c_network_type = c_network_type;
        this.d_date = d_date;
        this.fn_user = fn_user;
        this.id = id;
        this.n_battery_level = n_battery_level;
        this.n_phone_memory = n_phone_memory;
        this.n_ram = n_ram;
        this.n_sd_card_memory = n_sd_card_memory;
        this.n_time = n_time;
        this.n_used_phone_memory = n_used_phone_memory;
        this.n_used_ram = n_used_ram;
        this.n_used_sd_card_memory = n_used_sd_card_memory;
        this.objectOperationType = objectOperationType;
        this.isDelete = isDelete;
        this.isSynchronization = isSynchronization;
        this.tid = tid;
        this.blockTid = blockTid;
        this.dx_created = dx_created;
    }

    public boolean getB_isonline() {
        return this.b_isonline;
    }

    public void setB_isonline(boolean b_isonline) {
        this.b_isonline = b_isonline;
    }

    public String getC_network_type() {
        return this.c_network_type;
    }

    public void setC_network_type(String c_network_type) {
        this.c_network_type = c_network_type;
    }

    public String getD_date() {
        return this.d_date;
    }

    public void setD_date(String d_date) {
        this.d_date = d_date;
    }

    public long getFn_user() {
        return this.fn_user;
    }

    public void setFn_user(long fn_user) {
        this.fn_user = fn_user;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getN_battery_level() {
        return this.n_battery_level;
    }

    public void setN_battery_level(long n_battery_level) {
        this.n_battery_level = n_battery_level;
    }

    public long getN_phone_memory() {
        return this.n_phone_memory;
    }

    public void setN_phone_memory(long n_phone_memory) {
        this.n_phone_memory = n_phone_memory;
    }

    public long getN_ram() {
        return this.n_ram;
    }

    public void setN_ram(long n_ram) {
        this.n_ram = n_ram;
    }

    public long getN_sd_card_memory() {
        return this.n_sd_card_memory;
    }

    public void setN_sd_card_memory(long n_sd_card_memory) {
        this.n_sd_card_memory = n_sd_card_memory;
    }

    public long getN_time() {
        return this.n_time;
    }

    public void setN_time(long n_time) {
        this.n_time = n_time;
    }

    public long getN_used_phone_memory() {
        return this.n_used_phone_memory;
    }

    public void setN_used_phone_memory(long n_used_phone_memory) {
        this.n_used_phone_memory = n_used_phone_memory;
    }

    public long getN_used_ram() {
        return this.n_used_ram;
    }

    public void setN_used_ram(long n_used_ram) {
        this.n_used_ram = n_used_ram;
    }

    public long getN_used_sd_card_memory() {
        return this.n_used_sd_card_memory;
    }

    public void setN_used_sd_card_memory(long n_used_sd_card_memory) {
        this.n_used_sd_card_memory = n_used_sd_card_memory;
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
    @Generated(hash = 1394616954)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMobileIndicatorsDao() : null;
    }

    /**
     * Состояние подключения к сети интернет
     */
    @Expose
    public boolean b_isonline;

    /**
     * Тип сети
     */
    @Expose
    public String c_network_type;

    /**
     * Дата события
     */
    @Expose
    public String d_date;

    /**
     * Пользователь
     */
    @Expose
    public long fn_user;
    /**
     * Пользователь
     */
    @ToOne(joinProperty = "fn_user")
    private Users user;

    /**
     * Идентификатор
     */
    @Id
    @Expose
    private String id;

    /**
     * Уровень заряда батареи
     */
    @Expose
    public long n_battery_level;

    /**
     * Размер внутренней памяти
     */
    @Expose
    public long n_phone_memory;

    /**
     * Размер ОЗУ
     */
    @Expose
    public long n_ram;

    /**
     * Размер внешней памяти
     */
    @Expose
    public long n_sd_card_memory;

    /**
     * Смещение времени
     */
    @Expose
    private long n_time;

    /**
     * Размер используемой внутренней памяти
     */
    @Expose
    public long n_used_phone_memory;

    /**
     * Размер используемого ОЗУ
     */
    @Expose
    public long n_used_ram;

    /**
     * Размер используемой внешей памяти
     */
    @Expose
    public long n_used_sd_card_memory;

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

    private String dx_created;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 2126366333)
    private transient MobileIndicatorsDao myDao;

    @Generated(hash = 251390918)
    private transient Long user__resolvedKey;
}
