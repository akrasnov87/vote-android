package ru.mobnius.vote.ui.model;

import android.location.Location;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.FileManager;
import ru.mobnius.vote.data.storage.models.Attachments;
import ru.mobnius.vote.utils.BitmapUtil;
import ru.mobnius.vote.utils.DateUtil;
import ru.mobnius.vote.utils.LocationUtil;

/**
 * Вспомогательный класс для работы с изображениями для галереи
 */
public class Image implements Serializable {

    protected Image() {

    }

    /**
     * Получение экземпляра Image
     * @param dataManager провайдер для работы с данными
     * @param attachment вложение, Attachments
     * @return Image
     * @throws ParseException
     * @throws IOException
     */
    public static Image getInstance(DataManager dataManager, Attachments attachment) throws ParseException, IOException {
        Image image = new Image();
        image.mId = attachment.id;
        image.mDate = DateUtil.convertStringToDate(attachment.d_date);
        image.mLongitude = attachment.n_longitude;
        image.mLatitude = attachment.n_latitude;
        image.mName = attachment.c_name;
        image.mResultId = attachment.fn_result;
        image.mType = attachment.fn_type;
        image.mNotice = attachment.c_notice;
        return image;
    }

    /**
     * Создание экземпляра Image
     * @param name наименование изображения
     * @param type тип изображения, AttachmentTypes
     * @param resultId результат, Results
     * @param notice примечание
     * @param bytes данные
     * @param location местоположение, Location
     * @return Image
     */
    public static Image getInstance(String name, long type, String resultId, String notice, byte[] bytes, Location location) {
        Image image = new Image();
        image.mId = UUID.randomUUID().toString();
        image.mName = name;
        image.mType = type;
        image.mResultId = resultId;
        image.mNotice = notice;
        if(location != null) {
            image.mDate = new Date(location.getTime());
            image.mLatitude = location.getLatitude();
            image.mLongitude = location.getLongitude();
        }

        return image;
    }

    /**
     * Идентификатор
     */
    private String mId;
    /**
     * Наименование
     */
    private String mName;
    /**
     * Дата формирования
     */
    private Date mDate;
    /**
     * Результата, Results
     */
    private String mResultId;
    private String mNotice;
    /**
     * Тип изображения, AttachmentTypes
     */
    private long mType;
    private double mLatitude;
    private double mLongitude;

    /**
     * Событие изменения
     */
    private boolean mIsChanged;

    public void setLocation(Location location) {
        mDate = new Date();
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
    }

    public Date getDate() {
        return mDate == null ? new Date() : mDate;
    }

    public Location getLocation() {
        Location location = LocationUtil.getLocation(mLongitude, mLatitude);
        location.setTime(getDate().getTime());
        return location;
    }

    public String getName() {
        return mName;
    }

    public String getResultId() {
        return mResultId;
    }

    public byte[] getThumbs() {
        byte[] big = getBytes();
        FileManager fileManager = FileManager.getInstance();
        if(getResultId() != null) {
            if(fileManager.exists(FileManager.ATTACHMENTS, mName)) {
                try {
                    return fileManager.readPath(FileManager.ATTACHMENTS, mName);
                } catch (IOException ignored) {
                    return big;
                }
            }else {
                try {
                    return fileManager.readPath(FileManager.CACHES, mName);
                } catch (IOException ignored) {
                    return big;
                }
            }
        }
        return big;
    }

    public byte[] getBytes() {
        FileManager fileManager = FileManager.getInstance();
        if(getResultId() != null) {
            if(fileManager.exists(FileManager.FILES, mName)) {
                try {
                    return fileManager.readPath(FileManager.FILES, mName);
                } catch (IOException ignored) {
                    return null;
                }
            }else {
                try {
                    return fileManager.readPath(FileManager.TEMP_PICTURES, mName);
                } catch (IOException ignored) {
                    return null;
                }
            }
        }
        return null;
    }

    public long getType() {
        return mType;
    }

    public String getNotice() {
        return mNotice;
    }

    public String getId() {
        return mId;
    }

    public void remove(DataManager dataManager) {
        dataManager.removeAttachment(mId);
    }

    public void update(DataManager dataManager, long type, String notice) {
        mType = type;
        mNotice = notice;
        mIsChanged = true;
        dataManager.updateAttachment(mId, type, notice);
    }

    public boolean isChanged() {
        return mIsChanged;
    }

    public void resetChange() {
        mIsChanged = false;
    }

    public void setResultId(String resultId) {
        mResultId = resultId;
    }
}
