package ru.mobnius.vote.ui.model;

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

    private String mColor;
    private String mBgColor;

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

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
    }

    public String getBgColor() {
        return mBgColor;
    }

    public void setBgColor(String bgColor) {
        mBgColor = bgColor;
    }
}
