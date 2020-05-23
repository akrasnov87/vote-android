package ru.mobnius.vote.data.manager.packager;

/**
 * Бинарный файл в пакете синхронизации
 */
public class FileBinary {
    public FileBinary(String name, String key, byte[] bytes) {
        this.name = name;
        this.key = key;
        this.bytes = bytes;
    }

    /**
     * имя файла
     */
    public final String name;
    /**
     * ключ файла
     */
    public final String key;
    /**
     * Массив байтов
     */
    public final byte[] bytes;
}
