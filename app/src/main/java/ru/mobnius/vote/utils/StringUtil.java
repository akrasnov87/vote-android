package ru.mobnius.vote.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;

public class StringUtil {
    private static final String NULL = "null";
    /**
     * Корректировка строки
     * @param txt входная строка
     * @return результат
     */
    public static String normalString(String txt){
        if(txt == null) {
            return "";
        }
        return txt.equals(NULL) ? "" : txt;
    }

    /**
     * Преобразование байтов в КБ, МБ, ГБ
     * @param size размер
     * @return строка
     */
    public static String getSize(long size) {
        String s = "";
        double kb = (double) size / 1024;
        double mb = kb / 1024;
        double gb = mb / 1024;
        double tb = gb / 1024;
        if(size < 1024) {
            s = size + " байт";
        } else if(size < 1024 * 1024) {
            s =  String.format(new Locale("ru","RU"), "%.2f", kb) + " КБ";
        } else if(size < 1024 * 1024 * 1024) {
            s = String.format(new Locale("ru","RU"), "%.2f", mb) + " МБ";
        } else if(size < (long) 1024 * (long) 1024 * (long) 1024 * (long) 1024) {
            s = String.format(new Locale("ru","RU"), "%.2f", gb) + " ГБ";
        } else {
            s = String.format(new Locale("ru","RU"), "%.2f", tb) + " ТБ";
        }
        return s;
    }

    /**
     * строка является пустой или равна null
     * @param input входная строка
     * @return результат сравнения
     */
    public static boolean isEmptyOrNull(String input){
        String normal = normalString(input);
        return normal.isEmpty();
    }

    /**
     * Заполение разделителями
     * @param count количество
     * @param separator разделитель
     * @return возвращается строка
     */
    public static String fullSpace(int count, String separator) {
        if(count > 0 && !isEmptyOrNull(separator)) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < count; i++) {
                builder.append(separator);
            }
            return builder.toString();
        } else {
            return "";
        }
    }

    /**
     * Получение md5-хеш кода
     * @param inputString входная строка
     * @return хеш-код
     */
    public static String md5(String inputString) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(inputString.getBytes());
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * Преобразование HashMap в String
     * @param obj входной объект
     * @param paramName имя параметра
     * @return результат
     */
    @SuppressWarnings("unchecked")
    public static String hashMapToString(Object obj, String paramName){
        HashMap<String, Object> hashMap = (HashMap<String, Object>)obj;
        if(hashMap != null) {
            Object result = hashMap.get(paramName);
            if(result != null) {
                return String.valueOf(result);
            }
        }
        return null;
    }

    /**
     * Получение mime по имени файла
     *
     * @param name имя файла
     * @return MIME-тип
     */
    public static String getMimeByName(String name) {
        String extension = getExtension(name);

        switch (extension) {
            case ".jpg":
                return "image/jpeg";

            case ".png":
                return "image/png";

            case ".webp":
                return "image/webp";

            case ".mp3":
                return "audio/mpeg";

            case ".mp4":
                return "video/mp4";

            default:
                return "application/octet-stream";
        }
    }

    /**
     * Получение расширения файла
     *
     * @param name имя файла
     * @return расширение
     */
    public static String getExtension(String name) {
        if (name != null && !name.isEmpty()) {
            int strLength = name.lastIndexOf(".");
            if (strLength >= 0) {
                String ext = name.substring(strLength + 1).toLowerCase();
                if (ext.isEmpty()) {
                    return null;
                } else {
                    return "." + ext;
                }
            }
        }
        return null;
    }

    /**
     * Преобразование исключения в строку
     *
     * @param e исключение
     * @return строка
     */
    public static String exceptionToString(Throwable e) {
        Writer writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }
}
