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
            mFoodKit = jsonObject.has("jb_food_kit") ? People.getPeoples(jsonObject.getString("jb_food_kit")) : null;
            mOther = jsonObject.has("jb_other") ? People.getPeoples(jsonObject.getString("jb_other")) : null;
            mER = jsonObject.has("jb_er") ? People.getPeoples(jsonObject.getString("jb_er")) : null;
            mBudget = jsonObject.has("jb_budget") ? People.getPeoples(jsonObject.getString("jb_budget")) : null;
            mSignature = jsonObject.has("n_signature") ? jsonObject.getInt("n_signature") : null;
            mPeoples = jsonObject.has("c_peoples") ? jsonObject.getString("c_peoples") : "";
        } catch (JSONException e) {
            Logger.error(e);
        }
    }

    private Integer mPriority;

    /**
     * Номер ЛС
     */
    private String mAppartament;

    /**
     * Адрес
     */
    private String mAddress;

    private String mNotice;

    private People[] mFoodKit;
    private People[] mOther;
    private People[] mER;
    private People[] mBudget;
    private Integer mSignature;
    private String mPeoples;

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

    public Integer getPriority() {
        return mPriority;
    }

    public void setPriority(Integer priority) {
        mPriority = priority;
    }

    public People[] getFoodKit() {
        return mFoodKit;
    }

    public People[] getOther() {
        return mOther;
    }

    public People[] getER() {
        return mER;
    }

    public People[] getBudget() {
        return mBudget;
    }

    public Integer getSignature() {
        return mSignature;
    }

    public String getDescription() {
        StringBuilder builder = new StringBuilder();

        if(getFoodKit() != null && getFoodKit().length > 0) {
            builder.append("<p>Продуктовый набор:<br />");
            for(People p : getFoodKit()) {
                builder.append(String.format("- %s %s %s", p.firstName, p.lastName, p.patronymic) + "<br />");
            }

            builder.append("</p>");
        }

        if(getOther() != null && getOther().length > 0) {
            builder.append("<p>Иное:<br />");
            for(People p : getOther()) {
                builder.append(String.format("- %s %s %s", p.firstName, p.lastName, p.patronymic) + "<br />");
            }

            builder.append("</p>");
        }

        if(getER() != null && getER().length > 0) {
            builder.append("<p>Сторонники ЕР:<br />");
            for(People p : getER()) {
                builder.append(String.format("- %s %s %s", p.firstName, p.lastName, p.patronymic) + "<br />");
            }

            builder.append("</p>");
        }

        if(getBudget() != null && getBudget().length > 0) {
            builder.append("<p>Бюджетники:<br />");
            for(People p : getBudget()) {
                builder.append(String.format("- %s %s %s", p.firstName, p.lastName, p.patronymic) + "<br />");
            }

            builder.append("</p>");
        }

        if(getSignature() != null && getSignature() > 0) {
            builder.append("<p>Ставили подпись в 2018 году</p>");
        }

        return builder.toString();
    }

    public String getUserName() {
        switch (getPriority()) {
            case 1:
                if(getFoodKit().length > 0) {
                    return String.format("%s %s %s", getFoodKit()[0].firstName, getFoodKit()[0].lastName, getFoodKit()[0].patronymic);
                }
                return null;

            case 2:
                if(getOther().length > 0) {
                    return String.format("%s %s %s", getOther()[0].firstName, getOther()[0].lastName, getOther()[0].patronymic);
                }
                return null;

            case 3:
                if(getER().length > 0) {
                    return String.format("%s %s %s", getER()[0].firstName, getER()[0].lastName, getER()[0].patronymic);
                }
                return null;

            case 4:
                if(getBudget().length > 0) {
                    return String.format("%s %s %s", getBudget()[0].firstName, getBudget()[0].lastName, getBudget()[0].patronymic);
                }
                return null;

            default:
                return null;
        }
    }

    public String getPeoples() {
        return mPeoples;
    }
}
