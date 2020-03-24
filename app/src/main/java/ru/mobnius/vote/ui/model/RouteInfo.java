package ru.mobnius.vote.ui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Информация об маршруте
 */
public class RouteInfo {
    /**
     * Дата начала
     */
    private Date mDateStart;
    /**
     * Дата завершения
     */
    private Date mDateEnd;
    /**
     * Примечание к маршруту
     */
    private String mNotice;
    /**
     * Было ли продление или нет
     */
    private boolean mIsExtended;
    /**
     * Дата продления до
     */
    private Date mDateExtended;

    /**
     * История изменения статуса
     */
    private List<RouteInfoHistory> mHistories;

    /**
     * Добавление истории
     * @param statusId идентификатор статуса
     * @param statusName наименование статуса
     * @param date дата
     * @param notice примечание
     */
    public void addHistory(long statusId, String statusName, Date date, String notice) {
        if(mHistories == null) {
            mHistories = new ArrayList<>();
        }
        RouteInfoHistory history = new RouteInfoHistory();
        history.setDate(date);
        history.setNotice(notice);
        history.setStatus(statusName);
        history.setStatusId(statusId);
        mHistories.add(history);
    }

    public Date getDateStart() {
        return mDateStart;
    }

    public void setDateStart(Date dateStart) {
        mDateStart = dateStart;
    }

    public Date getDateEnd() {
        return mDateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        mDateEnd = dateEnd;
    }

    public String getNotice() {
        return mNotice;
    }

    public void setNotice(String notice) {
        mNotice = notice;
    }

    public boolean isExtended() {
        return mIsExtended;
    }

    public void setExtended(boolean extended) {
        mIsExtended = extended;
    }

    public Date getDateExtended() {
        return mDateExtended;
    }

    public void setDateExtended(Date dateExtended) {
        mDateExtended = dateExtended;
    }

    public RouteInfoHistory[] getHistories() {
        Collections.sort(mHistories, new Comparator<RouteInfoHistory>() {
            @Override
            public int compare(RouteInfoHistory o1, RouteInfoHistory o2) {
                return Long.compare(o1.getDate().getTime(), o2.getDate().getTime());
            }
        });

        return mHistories.toArray(new RouteInfoHistory[0]);
    }
}
