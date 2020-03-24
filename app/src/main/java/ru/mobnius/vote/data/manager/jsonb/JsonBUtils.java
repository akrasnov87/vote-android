package ru.mobnius.vote.data.manager.jsonb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.utils.StringUtil;

public class JsonBUtils {

    /**
     * Преобразование объекта в JSON
     * @param jsonBItems массив номеров
     * @return строка
     */
    public static String convertToJson(List<JsonBItem> jsonBItems){
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(jsonBItems);
    }

    /**
     * Преобразование строки в список номеров телефона
     * @param jsonString входная строка
     * @return массив с телефонами
     */
    public static List<JsonBItem> convertToList(String jsonString){
        if(!StringUtil.isEmptyOrNull(jsonString)){
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                List<JsonBItem> jsonBItems = new ArrayList<>();
                for (int i =0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JsonBItem jsonBItem = new JsonBItem();
                    jsonBItem.c_value = jsonObject.getString("c_value");
                    jsonBItem.c_key = jsonObject.getString("c_key");
                    jsonBItem.d_created = jsonObject.getString("d_created");
                    jsonBItem.b_default = jsonObject.getBoolean("b_default");

                    jsonBItems.add(jsonBItem);
                }
                return jsonBItems;
            } catch (JSONException e) {
                Logger.error("Ошибка чтения контактных данных.", e);
            }
        }

        return null;
    }

    /**
     * Преобразование в строку
     * @param jsonBItems номера телефонов
     * @return строка
     */
    public static String toUserString(List<JsonBItem> jsonBItems) {
        if(jsonBItems != null){
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < jsonBItems.size(); i++){
                JsonBItem jsonBItem = jsonBItems.get(i);
                builder.append(String.format("%s - %s", jsonBItem.c_key, jsonBItem.c_value));
                if(i + 1 < jsonBItems.size()){
                    builder.append(", ");
                }
            }
            return builder.toString();
        }
        return "";
    }
}
