package ru.mobnius.vote.data.manager.vote;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Для сохранения состояния
 */
public class VoteManager implements Serializable {
    private List<Vote> mList;

    public VoteManager() {
        mList = new ArrayList<>();
    }

    public void addQuestion(long question, long answer) {
        mList.add(new Vote(question, answer));
    }

    public void removeQuestion(long question) {
        Vote vote = null;
        if(mList.size() > 0) {
            for(Vote v : mList) {
                if(v.questionId == question) {
                    vote = v;
                    break;
                }
            }
            if(vote != null) {
                mList.remove(vote);
            }
        }
    }

    /**
     * Доступен ли вопрос
     * @param question идентификтаор вопроса
     * @return
     */
    public boolean isQuestionExists(long question) {
        if(mList.size() > 0) {
            for(Vote v : mList) {
                return v.questionId == question;
            }
        }

        return false;
    }

    /**
     * Доступность ответа
     * @param question вопрос
     * @param answer ответ
     * @return
     */
    public boolean isAnswerExists(long question, long answer) {
        if(mList.size() > 0) {
            for(Vote v : mList) {
                return v.questionId == question && v.answerId == answer;
            }
        }

        return false;
    }

    public Vote[] getList() {
        return mList.toArray(new Vote[0]);
    }
}
