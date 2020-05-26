package ru.mobnius.vote.data.manager.packager;

import com.google.gson.annotations.Expose;

/**
 * Вложенный файл
 */
public class MetaAttachment {
    /**
     * размер вложения
     */
    @Expose
    private final int size;

    /**
     * имя файла или относительный путь
     */
    @Expose
    private final String name;

    /**
     * ключ вложения
     */
    @Expose
    private final String key;

    /**
     * конструктор
     * @param size размер
     * @param name имя файла
     * @param key ключ
     */
    public MetaAttachment(int size, String name, String key) {
        this.size = size;
        this.key = key;
        this.name = name;
    }
}
