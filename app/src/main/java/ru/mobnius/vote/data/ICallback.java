package ru.mobnius.vote.data;

/**
 * Универсальный обработчик функций обратного вызова
 */
public interface ICallback {
    /**
     * результат обработки обратного вызова
     * @param meta результат
     */
    void onResult(Meta meta);
}
