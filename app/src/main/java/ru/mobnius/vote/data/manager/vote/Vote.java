package ru.mobnius.vote.data.manager.vote;

public class Vote {
    public long answerId;
    public long questionId;

    public Vote(long question, long answer) {
        answerId = answer;
        questionId = question;
    }
}
