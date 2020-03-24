package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.UUID;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

@Entity(nameInDb = "attachments")
public class Attachments implements IEntityTo {

    public Attachments() {
        id = UUID.randomUUID().toString();
    }

    @Generated(hash = 160270283)
    public Attachments(String d_date, String fn_result, String fn_route,
            long fn_type, String id, double n_latitude, double n_longitude,
            String c_notice, String fn_file, String folder,
            String objectOperationType, boolean isDelete, boolean isSynchronization,
            String tid, String blockTid, String jb_data, String dx_created,
            String c_name) {
        this.d_date = d_date;
        this.fn_result = fn_result;
        this.fn_route = fn_route;
        this.fn_type = fn_type;
        this.id = id;
        this.n_latitude = n_latitude;
        this.n_longitude = n_longitude;
        this.c_notice = c_notice;
        this.fn_file = fn_file;
        this.folder = folder;
        this.objectOperationType = objectOperationType;
        this.isDelete = isDelete;
        this.isSynchronization = isSynchronization;
        this.tid = tid;
        this.blockTid = blockTid;
        this.jb_data = jb_data;
        this.dx_created = dx_created;
        this.c_name = c_name;
    }

    public String getD_date() {
        return this.d_date;
    }

    public void setD_date(String d_date) {
        this.d_date = d_date;
    }

    public String getFn_result() {
        return this.fn_result;
    }

    public void setFn_result(String fn_result) {
        this.fn_result = fn_result;
    }

    public String getFn_route() {
        return this.fn_route;
    }

    public void setFn_route(String fn_route) {
        this.fn_route = fn_route;
    }

    public long getFn_type() {
        return this.fn_type;
    }

    public void setFn_type(long fn_type) {
        this.fn_type = fn_type;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getC_notice() {
        return this.c_notice;
    }

    public void setC_notice(String c_notice) {
        this.c_notice = c_notice;
    }

    public String getFn_file() {
        return this.fn_file;
    }

    public void setFn_file(String fn_file) {
        this.fn_file = fn_file;
    }

    public String getFolder() {
        return this.folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
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

    public String getC_name() {
        return this.c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

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

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 613039749)
    public Results getRoute() {
        String __key = this.fn_route;
        if (route__resolvedKey == null || route__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ResultsDao targetDao = daoSession.getResultsDao();
            Results routeNew = targetDao.load(__key);
            synchronized (this) {
                route = routeNew;
                route__resolvedKey = __key;
            }
        }
        return route;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1486388934)
    public void setRoute(Results route) {
        synchronized (this) {
            this.route = route;
            fn_route = route == null ? null : route.getId();
            route__resolvedKey = fn_route;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2067803801)
    public AttachmentTypes getType() {
        long __key = this.fn_type;
        if (type__resolvedKey == null || !type__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AttachmentTypesDao targetDao = daoSession.getAttachmentTypesDao();
            AttachmentTypes typeNew = targetDao.load(__key);
            synchronized (this) {
                type = typeNew;
                type__resolvedKey = __key;
            }
        }
        return type;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1072833871)
    public void setType(@NotNull AttachmentTypes type) {
        if (type == null) {
            throw new DaoException(
                    "To-one property 'fn_type' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.type = type;
            fn_type = type.getId();
            type__resolvedKey = fn_type;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 715862082)
    public Files getFile() {
        String __key = this.fn_file;
        if (file__resolvedKey == null || file__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FilesDao targetDao = daoSession.getFilesDao();
            Files fileNew = targetDao.load(__key);
            synchronized (this) {
                file = fileNew;
                file__resolvedKey = __key;
            }
        }
        return file;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 39298584)
    public void setFile(Files file) {
        synchronized (this) {
            this.file = file;
            fn_file = file == null ? null : file.getId();
            file__resolvedKey = fn_file;
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
    @Generated(hash = 2056440887)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAttachmentsDao() : null;
    }

    /**
     * Дата создания
     */
    @Expose
    public String d_date;

    /**
     * Результат
     */
    @Expose
    public String fn_result;

    @ToOne(joinProperty = "fn_result")
    public Results result;

    /**
     * Маршрут
     */
    @Expose
    public String fn_route;

    @ToOne(joinProperty = "fn_route")
    public Results route;

    /**
     * тип изображения
     */
    @Expose
    public long fn_type;

    @ToOne(joinProperty = "fn_type")
    public AttachmentTypes type;

    /**
     * Идентификатор
     */
    @Id
    @Expose
    public String id;

    /**
     * Широта
     */
    @Expose
    public double n_latitude;

    /**
     * Долгота
     */
    @Expose
    public double n_longitude;

    /**
     * Примечание
     */
    @Expose
    public String c_notice;

    @Expose
    public String fn_file;

    @ToOne(joinProperty = "fn_file")
    public Files file;

    /**
     * Каталог в котором храняться изображения
     */
    public String folder;

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

    @Expose
    public String jb_data;

    public String dx_created;

    public String c_name;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 772926432)
    private transient AttachmentsDao myDao;

    @Generated(hash = 1347497366)
    private transient String result__resolvedKey;

    @Generated(hash = 603420700)
    private transient String route__resolvedKey;

    @Generated(hash = 506996655)
    private transient Long type__resolvedKey;

    @Generated(hash = 1260399539)
    private transient String file__resolvedKey;
}
