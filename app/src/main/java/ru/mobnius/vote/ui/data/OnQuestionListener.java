package ru.mobnius.vote.ui.data;

import ru.mobnius.vote.ui.model.PointInfo;

/**
 * Фрагмент для вывода вопроса
 */
public interface OnQuestionListener {
    /**
     * Обработчик привязки данных
     * @param questionID иден. вопроса
     * @param exclusionAnswerID идентификатор ответа. Если -1, то ранее ответа не было
     * @param lastAnswerId иден. ответа на последний вопрос
     */
    void onQuestionBind(PointInfo pointInfo, long questionID, long exclusionAnswerID, long lastAnswerId);
}
