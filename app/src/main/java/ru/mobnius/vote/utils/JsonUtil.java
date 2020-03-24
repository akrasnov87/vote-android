package ru.mobnius.vote.utils;

public class JsonUtil {
    public final static String EMPTY = "{}";

    public static boolean isEmpty(String json) {
        return json.equals(EMPTY);
    }
}
