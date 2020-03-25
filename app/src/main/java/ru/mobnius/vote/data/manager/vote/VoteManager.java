package ru.mobnius.vote.data.manager.vote;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VoteManager implements Serializable {
    private List<Vote> mList;

    public VoteManager() {
        mList = new ArrayList<>();
    }

    public void addQuestion(int question, int answer) {
        mList.add(new Vote(question, answer));
    }

    public void removeQuestion(int question) {

    }

    public Vote[] getList() {
        return mList.toArray(new Vote[0]);
    }

    public void save() {

    }
}
