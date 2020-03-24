package ru.mobnius.vote.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.mobnius.vote.data.manager.Version;

public class DateUtil {
    public static final String USER_FORMAT = "dd.MM.yyyy HH:mm:ss";
    public static final String USER_SHORT_FORMAT = "dd.MM.yyyy";
    public static final String SYSTEM_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    /**
     * Преобразовать дату в пользовательскую строку
     *
     * @param date дата
     * @return возврщается строка
     */
    public static String convertDateToUserString(Date date) {
        return convertDateToUserString(date, USER_FORMAT);
    }

    /**
     * Преобразование строки в дату
     * @param time время в милисекундах
     * @return результат преобразования
     */
    public static Date convertTimeToDate(String time) {
        return new Date(Long.parseLong(time));
    }

    /**
     * Преобразование строки в дату
     * @param date дата
     * @return результат преобразования
     * @throws ParseException исключение при неверном формате
     */
    public static Date convertStringToDate(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(SYSTEM_FORMAT, Locale.getDefault());
        return dateFormat.parse(date);
    }

    /**
     * Дата преобразуется в строку с определнным форматом
     * @param date дата
     * @return возврщается строка
     */
    public static String convertDateToString(Date date) {
        return new SimpleDateFormat(SYSTEM_FORMAT, Locale.getDefault()).format(date);
    }

    /**
     * Дата преобразуется в строку с определнным форматом
     * @param date дата
     * @param format формат даты
     * @return возврщается строка
     */
    public static String convertDateToUserString(Date date, String format) {
        return new SimpleDateFormat(format, Locale.getDefault()).format(date);
    }

    /**
     * Генерация TID
     * @return уникальный идентификатор
     */
    public static int geenerateTid() {
        return Math.abs((int)((new Date().getTime() - Version.BIRTH_DAY.getTime())));
    }
}
