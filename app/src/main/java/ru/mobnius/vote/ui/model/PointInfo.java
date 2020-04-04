package ru.mobnius.vote.ui.model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import ru.mobnius.vote.data.storage.models.RegistrPts;
import ru.mobnius.vote.utils.DateUtil;
import ru.mobnius.vote.utils.DoubleUtil;
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
    private String mSubscrNumber;

    /**
     * ФИО потребителя
     */
    private String mFio;

    /**
     * Адрес
     */
    private String mAddress;

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
