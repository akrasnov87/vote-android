package ru.mobnius.vote.data.manager.vote;

public class Vote {
    public long answerId;
    public long questionId;

    private String mComment;
    private String mJbTel;

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
}
