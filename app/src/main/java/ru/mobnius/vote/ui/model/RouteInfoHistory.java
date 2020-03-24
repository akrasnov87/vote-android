package ru.mobnius.vote.ui.model;

import java.util.Date;

/**
 * История изменения статуса
 */
public class RouteInfoHistory {
    /**
     * Наименование статуса
     */
    private String mStatus;
    /**
     * Идентификатор статуса
     */
    private long mStatusId;
    /**
     * Дата создания статуса
     */
    private Date mDate;
    /**
     * Примечание
     */
    private String mNotice;

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public long getStatusId() {
        return mStatusId;
    }

    public void setStatusId(long statusId) {
        mStatusId = statusId;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getNotice() {
        return mNotice;
    }

    public void setNotice(String notice) {
        mNotice = notice;
    }
}
