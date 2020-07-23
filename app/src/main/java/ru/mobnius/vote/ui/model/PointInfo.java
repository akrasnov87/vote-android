package ru.mobnius.vote.ui.model;

import org.json.JSONException;
import org.json.JSONObject;

import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.utils.StringUtil;

/**
 * Информация о токе маршрута
 */
public class PointInfo {

    /**
     * Конструктор
     * @param data учетный показатель
     */
    public PointInfo(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            mAppartament = jsonObject.getString("c_appartament_num");
            mAddress = jsonObject.getString("c_address");
        } catch (JSONException e) {
            Logger.error(e);
        }
    }

    /**
     * Номер ЛС
     */
    private String mAppartament;

    /**
     * Адрес
     */
    private String mAddress;

    private String mNotice;

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getAppartament() {
        return mAppartament;
    }

    public String getNotice() {
        return StringUtil.normalString(mNotice);
    }

    public void setNotice(String notice) {
        mNotice = notice;
    }

}
