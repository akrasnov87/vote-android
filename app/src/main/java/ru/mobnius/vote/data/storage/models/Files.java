package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.UUID;

@Entity(nameInDb = "files")
public class Files implements IEntityTo {

    public Files() {
        id = UUID.randomUUID().toString();
    }

    @Generated(hash = 2065194649)
    public Files(String id, String c_name, String c_mime, String c_extension,
            int n_size, String d_date, String folder, String objectOperationType,
            boolean isDelete, boolean isSynchronization, String tid,
            String blockTid, String jb_data, String dx_created) {
        this.id = id;
        this.c_name = c_name;
        this.c_mime = c_mime;
        this.c_extension = c_extension;
        this.n_size = n_size;
        this.d_date = d_date;
        this.folder = folder;
        this.objectOperationType = objectOperationType;
        this.isDelete = isDelete;
        this.isSynchronization = isSynchronization;
        this.tid = tid;
        this.blockTid = blockTid;
        this.jb_data = jb_data;
        this.dx_created = dx_created;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getC_name() {
        return this.c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getC_mime() {
        return this.c_mime;
    }

    public void setC_mime(String c_mime) {
        this.c_mime = c_mime;
    }

    public String getC_extension() {
        return this.c_extension;
    }

    public void setC_extension(String c_extension) {
        this.c_extension = c_extension;
    }

    public int getN_size() {
        return this.n_size;
    }

    public void setN_size(int n_size) {
        this.n_size = n_size;
    }

    public String getD_date() {
        return this.d_date;
    }

    public void setD_date(String d_date) {
        this.d_date = d_date;
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

    /**
     * Идентификатор
     */
    @Id
    @Expose
    public String id;

    /**
     * Имя файла
     */
    @Expose
    public String c_name;

    /**
     * Тип mime
     */
    @Expose
    public String c_mime;

    /**
     * Расширение
     */
    @Expose
    public String c_extension;

    /**
     * Размер файла
     */
    public int n_size;

    /**
     * Дата создания
     */
    @Expose
    public String d_date;

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
}
