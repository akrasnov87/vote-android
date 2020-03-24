package ru.mobnius.vote.data;

/**
 * интерфейс выполнения операции
 */
public interface IProgressListener {
    /**
     * Обработчик завершения
     * @param fileName имя файла
     * @param bytes массив байтов
     */
    void onDone(String fileName, byte[] bytes);

    /**
     * Обработчик ошибок
     * @param e ошибка
     */
    void onError(Exception e);
}
