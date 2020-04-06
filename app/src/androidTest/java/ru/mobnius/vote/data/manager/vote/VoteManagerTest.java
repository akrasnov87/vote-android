package ru.mobnius.vote.data.manager.vote;

import org.junit.Before;
import org.junit.Test;

import ru.mobnius.vote.ManagerGenerate;
import ru.mobnius.vote.data.storage.models.Answer;
import ru.mobnius.vote.data.storage.models.Question;

import static org.junit.Assert.*;

public class VoteManagerTest extends ManagerGenerate {

    private VoteManager mVoteManager;

    public static Question getQuestion(int id, String text, String description) {
        Question question = new Question();
        question.c_title = text;
        question.c_text = description;
        question.id = (long)id;
        question.n_order = id;

        return question;
    }

    public static Answer getAnswer(int id, String text, long question, long nextQuestion, String action) {
        Answer answer = new Answer();
        answer.id = (long)id;
        answer.c_action = action;
        answer.c_text = text;
        answer.f_next_question = nextQuestion;
        answer.f_question = question;
        answer.n_order = id;

        return answer;
    }

    @Before
    public void setUp() {
        getDaoSession().getQuestionDao().deleteAll();
        getDaoSession().getAnswerDao().deleteAll();

        getDaoSession().getQuestionDao().insert(getQuestion(1, "question 1", ""));
        getDaoSession().getQuestionDao().insert(getQuestion(2, "question 2", ""));
        getDaoSession().getQuestionDao().insert(getQuestion(3, "question 3", ""));
        getDaoSession().getQuestionDao().insert(getQuestion(4, "question 4", ""));

        getDaoSession().getAnswerDao().insert(getAnswer(1, "answer 1", 1, 2, "COMMENT"));
        getDaoSession().getAnswerDao().insert(getAnswer(2, "answer 2", 1, 3, "COMMENT"));

        getDaoSession().getAnswerDao().insert(getAnswer(3, "answer 3", 2, 0, ""));
        getDaoSession().getAnswerDao().insert(getAnswer(5, "answer 5", 2, 4, ""));

        getDaoSession().getAnswerDao().insert(getAnswer(6, "answer 6", 3, 0, ""));
        getDaoSession().getAnswerDao().insert(getAnswer(7, "answer 7", 3, 4, ""));

        getDaoSession().getAnswerDao().insert(getAnswer(4, "answer 4", 4, 0, ""));

        mVoteManager = new VoteManager();
    }

    @Test
    public void questionTest() {
        mVoteManager.addQuestion(1, 1, 0);
        mVoteManager.addQuestion(2, 5, 1);
        mVoteManager.addQuestion(4, 4, 2);

        assertEquals(mVoteManager.getList().length, 3);

        assertEquals(mVoteManager.getLastQuestionID(), 4);
        assertEquals(mVoteManager.getPrevQuestionID(2), 1);
        assertTrue(mVoteManager.isQuestionExists(2));
        assertFalse(mVoteManager.isQuestionExists(3));
        assertEquals(mVoteManager.getQuestionAnswer(2), 5);
        assertTrue(mVoteManager.isAnswerExists(2, 5));
        assertFalse(mVoteManager.isAnswerExists(2, 4));

        mVoteManager.removeQuestion(2);
        assertFalse(mVoteManager.isQuestionExists(2));
    }
}