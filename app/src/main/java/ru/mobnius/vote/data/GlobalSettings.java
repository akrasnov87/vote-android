package ru.mobnius.vote.data;

import java.util.Date;
import java.util.GregorianCalendar;

import ru.mobnius.vote.utils.BitmapUtil;

/**
 * Настройки
 * @// TODO: 04.06.2019 Существует временно потом нужно его менять
 */
public class GlobalSettings {
    /**
     * Применять ли статическую скорось передачи данных
     * По умолчанию использовать false. При тестирование удобно true
     */
    public static boolean STATUS_TRANSFER_SPEED = false;

    public static final String DEFAULT_USER_NAME = "inspector";
    public static final String DEFAULT_USER_PASSWORD = "inspector0";
    public static final Object DEFAULT_USER_ID = 4;
}
