package ru.mobnius.vote.ui.fragment.data;

/**
 * Фрагмент для вывода вопроса
 */
public interface onQuestionListener {
    /**
     * Обработчик привязки данных
     * @param questionID иден. вопроса
     * @param exclusionAnswerID идентификатор ответа. Если -1, то ранее ответа не было
     */
    void onQuestionBind(long questionID, long exclusionAnswerID);
}
