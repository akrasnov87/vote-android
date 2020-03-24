package ru.mobnius.vote.ui.model;

import java.util.Date;

import ru.mobnius.vote.utils.DoubleUtil;

public class Meter {
    private Date mDate;
    private Date mDatePrev;
    private double mValue;
    private double mValuePrev;
    private int mOrder;
    private String mName;

    private String mInputMeterReadingsId;

    private String mOutputMeterReadingsId;

    public Meter(String name, double valuePrev, Date datePrev, double value, Date date, int order) {
        mDate = date;
        mDatePrev = datePrev;

        mValue = value;
        mValuePrev = valuePrev;
        mName = name;

        mOrder = order;
    }

    public Date getDate() {
        return mDate;
    }

    public Date getDatePrev() {
        return mDatePrev;
    }

    public double getValue() {
        return mValue;
    }

    public double getValuePrev() {
        return mValuePrev;
    }

    public int getOrder() {
        return mOrder;
    }

    public String getName() {
        return mName;
    }

    public String getInputMeterReadingsId() {
        return mInputMeterReadingsId;
    }

    public void setInputMeterReadingsId(String id) {
        mInputMeterReadingsId = id;
    }

    public String getOutputMeterReadingsId() {
        return mOutputMeterReadingsId;
    }

    public void setOutputMeterReadingsId(String outputMeterReadingsId) {
        mOutputMeterReadingsId = outputMeterReadingsId;
    }

    public String getValueToString() {
        return toValueString(getValue());
    }

    public String getPrevValueToString() {
        return toValueString(getValuePrev());
    }

    public String toValueString(double value) {
        return DoubleUtil.toStringValue(value);
    }
}
