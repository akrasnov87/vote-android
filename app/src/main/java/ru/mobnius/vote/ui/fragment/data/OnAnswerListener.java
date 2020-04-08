package ru.mobnius.vote.ui.fragment.data;

import ru.mobnius.vote.data.storage.models.Answer;

public interface OnAnswerListener {
    /**
     *
     * @param type тип результата команды
     * @param answer ответ
     * @param result результат если есть
     */
    void onAnswerCommand(String type, Answer answer, Object result);
}
