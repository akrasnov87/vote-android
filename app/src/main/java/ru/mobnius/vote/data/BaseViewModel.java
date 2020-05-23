package ru.mobnius.vote.data;



abstract class BaseViewModel<T>  {

    private T mModel;

    public void setModel(T model) {
        mModel = model;
    }

    public T getModel() {
        return mModel;
    }
}
