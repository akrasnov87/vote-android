package ru.mobnius.vote.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.ui.fragment.tools.ContactItem;

public class JsonUtil {
    public final static String EMPTY = "{}";

    public static boolean isEmpty(String json) {
        return json.equals(EMPTY);
    }


    public static String convertToJson(List<ContactItem> contactItems){
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(contactItems);
    }

    public static List<ContactItem> convertToContacts(String jsonString){
        if(!jsonString.isEmpty()){
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                List<ContactItem> contactItems = new ArrayList<>();
                for (int i =0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    ContactItem contactItem = new ContactItem();
                    contactItem.c_value = jsonObject.getString("c_value");
                    contactItem.c_key = jsonObject.getString("c_key");
                    contactItem.d_created = jsonObject.getString("d_created");
                    contactItem.b_default = jsonObject.getBoolean("b_default");

                    contactItems.add(contactItem);
                }
                return contactItems;
            } catch (JSONException e) {
                Logger.error("Ошибка чтения контактных данных.", e);
            }
        }

        return null;
    }
}
