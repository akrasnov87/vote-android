package ru.mobnius.vote.data.manager.exception;

/**
 * Интерфейс перехвата ошибок
 */
public interface IExceptionIntercept {
    /**
     * Обработчик перехвата ошибок
     */
    void onExceptionIntercept();

    /**
     * Группа ошибки из IExceptionGroup
     * @return строка
     */
    String getExceptionGroup();

    /**
     * Числовой код ошибки из IExceptionCode
     * @return строка
     */
    int getExceptionCode();
}
