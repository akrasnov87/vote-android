package ru.mobnius.vote.ui.fragment.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import ru.mobnius.vote.ui.model.Meter;

public class DocumentUtil {

    public final static String NAME_NOTICE = "notice";

    public static DocumentUtil getInstance(Meter[] meters, String notice) {
        // TODO: 28/01/2020 нужно скорректировать
        HashMap<String, Object> values = new HashMap<>();
        HashMap<String, Object> values2 = new HashMap<>();
        values.put(NAME_NOTICE, notice);
        values2.put(NAME_NOTICE, notice);

        List<DocumentUtil.MeterReading> list = new ArrayList<>();

        List<DocumentUtil.MeterReading> list2 = new ArrayList<>();
        for(Meter meter : meters) {
            list.add(new MeterReading(meter.getInputMeterReadingsId(), meter.getValue()));
            list2.add(new MeterReading(meter.getInputMeterReadingsId(), meter.getValue()));
        }
        DocumentUtil serializable = new DocumentUtil(list, values);
        serializable.setCompareDocument(new DocumentUtil(list2, values2));

        if(meters.length > 0) {
            Date currentDate = meters[0].getDate();
            if (currentDate == null) {
                currentDate = new Date();
            }
            serializable.setDate(currentDate);

            serializable.setPrevDate(meters[0].getDatePrev());
        }

        return serializable;
    }

    /**
     * Текущая дата показания
     */
    private Date mDate;

    /**
     * Пред. дата показания
     */
    private Date mPrevDate;

    /**
     * дополнителные данные
     */
    private HashMap<String, Object> mValues;

    /**
     * Показания
     */
    private List<MeterReading> mMeters;

    private DocumentUtil mOriginalDocument;

    /**
     * Конструктор
     * @param meters показания
     * @param values дополнительные значения
     */
    public DocumentUtil(List<MeterReading> meters, HashMap<String, Object> values) {
        mMeters = new ArrayList<>(meters.size());
        mMeters.addAll(meters);
        mValues = values;
    }

    /**
     * Получение показаний
     * @return список показаний
     */
    public MeterReading[] getMeters() {
        return mMeters.toArray(new MeterReading[0]);
    }

    /**
     * Были ли изменения в документе
     * @return true - изменения в документе присутствовали
     */
    public boolean isChanged() {
        if(mOriginalDocument != null) {

            for(String key : mValues.keySet()) {
                if (!Objects.equals(mValues.get(key), mOriginalDocument.mValues.get(key))) {
                    return true;
                }
            }

            for(int i = 0; i < getMeters().length; i++) {
                if(getMeters()[i].getValue() != mOriginalDocument.getMeters()[i].getValue()) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Добавляем оригинальные данные для документа
     * @param originalDocument объект
     */
    public void setCompareDocument(DocumentUtil originalDocument) {
        mOriginalDocument = originalDocument;
    }

    /**
     * получение дополнительных параметров
     * @return
     */
    public HashMap<String, Object> getValues() {
        return mValues;
    }

    public String getStringValue(String key) {
        return (String)getValues().get(key);
    }

    public Date getDate() {
        return mDate;
    }

    public Date getPrevDate() {
        return mPrevDate;
    }

    protected void setDate(Date date) {
        mDate = date;
    }

    protected void setPrevDate(Date prevDate) {
        mPrevDate = prevDate;
    }

    /**
     * Обновление показания
     * @param meterId идентификатор показания
     * @param value значение
     */
    public void updateMeterReading(String meterId, Double value) {
        for (MeterReading meterReading : mMeters) {
            if(meterReading.getId().equals(meterId)) {
                meterReading.mValue = value == null ? 0.0 : value;
                return;
            }
        }
    }

    public void updateValue(String key, Object value) {
        mValues.put(key, value);
    }

    /**
     * Показание
     */
    public static class MeterReading {
        private String mId;
        private double mValue;

        public MeterReading(String id, double value) {
            mId = id;
            mValue = value;
        }

        /**
         * Идентификатор
         * @return
         */
        public String getId() {
            return mId;
        }

        /**
         * Значение
         * @return
         */
        public double getValue() {
            return mValue;
        }
    }
}
