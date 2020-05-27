package ru.mobnius.vote.data.manager.vote;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.mobnius.vote.data.storage.models.Answer;
import ru.mobnius.vote.data.storage.models.Results;

/**
 * Для сохранения состояния
 */
public class VoteManager implements Serializable {
    private List<Vote> mList;

    public VoteManager() {
        mList = new ArrayList<>();
    }

    /**
     * добавление вопроса
     * @param questionID идентификтаор вопроса
     * @param answerID идентификатор ответа
     * @param order сортировка
     */
    public void addQuestion(long questionID, long answerID, int order) {
        Vote vote = new Vote(questionID, answerID);
        vote.setOrder(order);
        mList.add(vote);
    }

    /**
     * Удаление последнего вопроса
     * @param questionID идентификтаор вопроса
     */
    public void removeQuestion(long questionID) {
        Vote vote = null;
        if(mList.size() > 0) {
            for(Vote v : mList) {
                if(v.questionId == questionID) {
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
     * Идентификтаор последнего вопроса
     */
    public long getLastQuestionID() {
        if(mList.size() > 0) {
            return mList.get(mList.size() - 1).questionId;
        }

        return  -1;
    }

    /**
     * Предыдущий вопрос
     * @param currentQuestionID идентификатор текущего вопроса
     */
    public long getPrevQuestionID(long currentQuestionID) {
        if(mList.size() > 0) {
            long prevID = -1;
            for(Vote v : mList) {
                if(v.questionId == currentQuestionID) {
                    return prevID;
                } else {
                    prevID = v.questionId;
                }
            }
        }
        return  -1;
    }

    /**
     * Доступен ли вопрос
     * @param questionID идентификтаор вопроса
     */
    public boolean isQuestionExists(long questionID) {
        if(mList.size() > 0) {
            for(Vote v : mList) {
                if(v.questionId == questionID) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Вернуть ответ на вопрос
     * @param questionID идентификтаор вопроса
     */
    public long getQuestionAnswer(long questionID) {
        if(mList.size() > 0) {
            for(Vote v : mList) {
                if(v.questionId == questionID){
                    return v.answerId;
                }
            }
        }

        return -1;
    }

    /**
     * Доступность ответа
     * @param questionID вопрос
     * @param answer ответ
     */
    public boolean isAnswerExists(long questionID, long answer) {
        if(mList.size() > 0) {
            for(Vote v : mList) {
                if(v.questionId == questionID && v.answerId == answer) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Получение списка результатов
     */
    public Vote[] getList() {
        return mList.toArray(new Vote[0]);
    }

    /**
     * Импорт результатов головования в текущий объект
     * @param results результаты
     */
    public void importFromResult(Results[] results) {
        for(Results result : results) {
            Vote vote = new Vote(result.fn_question, result.fn_answer);
            vote.setOrder(result.n_order);
            vote.setComment(result.c_notice);
            vote.setJbTel(result.jb_data);
            mList.add(vote);
        }
    }

    /**
     * Доступна ли команда
     * @param answer ответ
     * @param command список команд
     * @return true - команда доступна
     */
    public boolean isExistsCommand(Answer answer, String command) {
        return answer.c_action.contains(command);
    }

    /**
     * ОБновление вопроса
     * @param questionId идентификатор вопроса
     * @param comment комментарий
     * @param tel номер телефона
     */
    public void updateQuestion(long questionId, String comment, String tel) {
        if(mList.size() > 0) {
            for(Vote v : mList) {
                if(v.questionId == questionId) {
                    if(comment != null && !comment.isEmpty()) {
                        v.setComment(comment);
                    }

                    if(tel != null && !tel.isEmpty()) {
                        v.setJbTel(tel);
                    }
                }
            }
        }
    }

    /**
     * Получение номера телефона
     * @param questionId идентификатор вопроса
     */
    public String getTel(long questionId) {
        if(mList.size() > 0) {
            for(Vote v : mList) {
                if(v.questionId == questionId) {
                    return v.getJbTel();
                }
            }
        }
        return null;
    }

    /**
     * Получение комментария
     * @param questionId идентификатор вопроса
     */
    public String getComment(long questionId) {
        if(mList.size() > 0) {
            for(Vote v : mList) {
                if(v.questionId == questionId) {
                    return v.getComment();
                }
            }
        }
        return null;
    }

    public void clear() {
        mList.clear();
    }
}
