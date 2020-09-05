package ru.mobnius.vote;

public interface Command {
    String NONE = "NONE";
    String FINISH = "FINISH";
    String CONTACT = "CONTACT";
    String VOTING = "VOTING";
    String VOTE_IN_HOME = "VOTE_IN_HOME";
    String COMMENT = "COMMENT";
    String RATING = "RATING";
}
