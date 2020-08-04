package ru.mobnius.vote.data.manager.vote;

import java.io.Serializable;

public class Vote implements Serializable {
    public final long answerId;
    public final long questionId;

    private String mComment;
    private String mJbTel;
    private Integer mRating;

    private int mOrder;

    public Vote(long question, long answer) {
        answerId = answer;
        questionId = question;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    public String getJbTel() {
        return mJbTel;
    }

    public void setJbTel(String jbTel) {
        mJbTel = jbTel;
    }

    public int getOrder() {
        return mOrder;
    }

    public void setOrder(int order) {
        mOrder = order;
    }

    public Integer getRating() {
        return mRating;
    }

    public void setRating(Integer rating) {
        mRating = rating;
    }
}
