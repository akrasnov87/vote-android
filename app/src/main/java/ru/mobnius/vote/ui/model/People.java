package ru.mobnius.vote.ui.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.utils.StringUtil;

public class People {
    public String firstName;
    public String lastName;
    public String patronymic;
    public String orgName;
    public Integer birthYear;
    public String phone;

    public static People[] getPeoples(String json) {
        if(StringUtil.isEmptyOrNull(json)) {
            return new People[0];
        } else {
            List<People> peopleList = new ArrayList<>();
            try {
                JSONArray array = new JSONArray(json);
                for(int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    People people = new People();
                    people.birthYear = jsonObject.has("n_birth_year") ? null : jsonObject.getInt("n_birth_year");
                    people.firstName = StringUtil.normalString(jsonObject.getString("c_first_name"));
                    people.lastName = StringUtil.normalString(jsonObject.getString("c_last_name"));
                    people.patronymic = StringUtil.normalString(jsonObject.getString("c_patronymic"));
                    people.orgName = StringUtil.normalString(jsonObject.getString("c_org"));
                    people.phone = StringUtil.normalString(jsonObject.getString("c_phone"));
                    peopleList.add(people);
                }
            } catch (JSONException e) {
                Logger.error(e);
            }

            return peopleList.toArray(new People[0]);
        }
    }
}
