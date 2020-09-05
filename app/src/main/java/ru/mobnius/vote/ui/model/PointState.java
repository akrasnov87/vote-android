package ru.mobnius.vote.ui.model;

import ru.mobnius.vote.utils.StringUtil;

/**
 * Состояние задания
 */
public class PointState {

    /**
     * Было выполнено или нет
     */
    private boolean mDone;

    /**
     * Было синхронизировано или нет
     */
    private boolean mSync;

    private String[] mColor;

    private Integer mRating;
    private String mData;

    public boolean isDone() {
        return mDone;
    }

    public void setDone(boolean done) {
        mDone = done;
    }

    public boolean isSync() {
        return mSync;
    }

    public void setSync(boolean sync) {
        mSync = sync;
    }

    public String[] getColor() {
        return mColor;
    }

    public void setColor(String color) {
        color = StringUtil.normalString(color);
        if(color.length() > 0) {
            mColor = color.split(",");
        }
    }

    public Integer getRating() {
        return mRating;
    }

    public void setRating(Integer rating) {
        mRating = rating;
    }

    public String getData() {
        return mData;
    }

    public void setData(String data) {
        mData = data;
    }
}
