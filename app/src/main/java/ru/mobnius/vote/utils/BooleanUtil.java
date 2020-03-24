package ru.mobnius.vote.utils;

public class BooleanUtil {
    /**
     * Является ли значение boolean переменной
     * @param value входное значение
     * @return true - входное значение является true
     */
    public static boolean isBooleanValue(String value) {
        String lowerCase = value.toLowerCase();
        if(lowerCase == "true" || lowerCase == "false") {
            return true;
        }

        return false;
    }

    /**
     * Является ли значение boolean переменной и равно true
     * @param value входное значение
     * @return true - входное значение является true
     */
    public static boolean isBooleanValueTrue(String value){
        return value.toLowerCase().equals("true");
    }

    /**
     * Преобразование в boolean
     * @param value значение
     * @return возвращается boolean
     */
    public static boolean convertToBoolean(Object value){
        return Boolean.parseBoolean(String.valueOf(value));
    }
}
