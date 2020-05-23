package ru.mobnius.vote.ui.model;

import ru.mobnius.vote.data.storage.models.RegistrPts;
import ru.mobnius.vote.utils.StringUtil;

/**
 * Информация о токе маршрута
 */
public class PointInfo {

    /**
     * Конструктор
     * @param registrPts учетный показатель
     */
    public PointInfo(RegistrPts registrPts) {
        mSubscrNumber = registrPts.c_subscr;
        mFio = registrPts.c_fio;
        mAddress = registrPts.c_address;
    }

    /**
     * Номер ЛС
     */
    private final String mSubscrNumber;

    /**
     * ФИО потребителя
     */
    private final String mFio;

    /**
     * Адрес
     */
    private final String mAddress;

    private String mNotice;

    public String getAddress() {
        return mAddress;
    }

    public String getFio() {
        return mFio;
    }

    public String getSubscrNumber() {
        return mSubscrNumber;
    }

    public String getNotice() {
        return StringUtil.normalString(mNotice);
    }

    public void setNotice(String notice) {
        mNotice = notice;
    }

}
