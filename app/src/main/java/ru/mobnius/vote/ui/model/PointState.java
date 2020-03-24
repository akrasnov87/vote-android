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
}
