package ru.mobnius.vote.utils;

public class IntUtil {
    /**
     * Преобразование в int
     * @param value значение
     * @return возвращается int
     */
    public static int convertToInt(Object value){
        return Integer.parseInt(String.valueOf(value));
    }
}
