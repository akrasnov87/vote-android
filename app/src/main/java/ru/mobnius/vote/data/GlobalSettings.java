package ru.mobnius.vote.data;
// TODO: 04.06.2019 Существует временно потом нужно его менять
/**
 * Настройки
 *
 */
public class GlobalSettings {
    /**
     * Применять ли статическую скорось передачи данных
     * По умолчанию использовать false. При тестирование удобно true
     */
    public static final boolean STATUS_TRANSFER_SPEED = false;

    /**
     * Тут может быть два значения test или dev. По умолчанию всегда dev
     */
    public static String ENVIRONMENT = "test";

    public static final String DEFAULT_USER_NAME = "1801-01";
    public static final String DEFAULT_USER_PASSWORD = "8842";
    public static final Object DEFAULT_USER_ID = 180101;
}
