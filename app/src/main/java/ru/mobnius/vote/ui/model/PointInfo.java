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

/**
 * Информация о токе маршрута
 */
public class PointInfo {

    /**
     * Конструктор
     * @param registrPts учетный показатель
     */
    public PointInfo(RegistrPts registrPts) {
        mMeters = new ArrayList<>();
        mSubscrNumber = registrPts.c_subscr;
        mFio = registrPts.c_fio;
        mAddress = registrPts.c_address;
        mSubDivisionName = registrPts.getSubdivision().c_name;
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

    /**
     * Участок
     */
    private String mSubDivisionName;

    /**
     * Показания
     */
    private List<MeterReadingsInfo> mMeters;

    /**
     * Получение списка показаний
     * @return список показаний
     */
    public MeterReadingsInfo[] getMeters() {

        Collections.sort(mMeters, new Comparator<MeterReadingsInfo>() {
            @Override
            public int compare(MeterReadingsInfo o1, MeterReadingsInfo o2) {
                return Integer.compare(o2.getOrder(), o1.getOrder());
            }
        });

        return mMeters.toArray(new MeterReadingsInfo[0]);
    }

    public String getSubDivisionName() {
        return mSubDivisionName;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getFio() {
        return mFio;
    }

    public String getSubscrNumber() {
        return mSubscrNumber;
    }

    /**
     * Информация о показаниях
     */
    public class MeterReadingsInfo {

        public MeterReadingsInfo(Date datePrev, double valuePrev, String timeZoneName, int order) {
            mDatePrev = datePrev;
            mValuePrev = valuePrev;
            mTimeZoneName = timeZoneName;
            mOrder = order;
        }

        /**
         * Сортировка
         */
        private int mOrder;

        /**
         * Дата предыдущего показания
         */
        private Date mDatePrev;

        /**
         * Предыдущее показание
         */
        private double mValuePrev;

        /**
         * Тарифная зона
         */
        private String mTimeZoneName;

        public Date getDatePrev() {
            return mDatePrev;
        }

        public double getValuePrev() {
            return mValuePrev;
        }

        public String getTimeZoneName() {
            return mTimeZoneName;
        }

        public int getOrder() {
            return mOrder;
        }

        public String prevValueToString() {
            return DoubleUtil.toStringValue(getValuePrev());
        }
    }
}
