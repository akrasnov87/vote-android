package ru.mobnius.vote.data.manager.vote;

public class Vote {
    public int answerId;
    public int questionId;

    public Vote(int question, int answer) {
        answerId = answer;
        questionId = question;
    }
}
