package ru.mobnius.vote.data.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.utils.DateUtil;

public abstract class ItemsManager<K, V> {
    @Expose
    private String mKey;
    @Expose
    private String mDate;
    @Expose
    protected List<K> mItems;

    /**
     * Конструктор
     *
     * @param key ключ
     */
    public ItemsManager(String key) {
        mKey = key;
        mDate = DateUtil.convertDateToString(new Date());
        mItems = new ArrayList<>(3);
    }

    /**
     * Конструктор
     *
     * @param key         ключ
     * @param deSerialize строка для обработки
     */
    public ItemsManager(String key, String deSerialize) {
        this(key);
        deSerialize(deSerialize);
    }

    /**
     * Добавление элемента
     *
     * @param item элемент
     */
    public void addItem(K item) {
        mItems.add(item);
    }

    /**
     * Удаление элемента
     *
     * @param item элемент
     */
    public void removeItem(K item) {
        mItems.remove(item);
    }

    /**
     * Обновление записи
     * @param key ключ
     * @param value значение
     */
    public abstract void updateItem(String key, V value);

    /**
     * Получение элемента
     *
     * @param name наименование
     * @return Элемент
     */
    public K getItem(String name) {
        for (K item : mItems) {
            if (((IItemManager)item).getName().equals(name)) {
                return item;
            }
        }

        return null;
    }

    /**
     * Список
     *
     * @return записи
     */
    public abstract K[] getItems();

    /**
     * Преобразовать в строку
     *
     * @return строка
     */
    public String serialize() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(this);
    }

    /**
     * Преобразование настройки в объект
     *
     * @param value входной текст
     */
    public void deSerialize(String value) {
        try {
            JSONObject jsonObject = new JSONObject(value);
            this.mKey = jsonObject.getString("mKey");
            this.mDate = jsonObject.getString("mDate");
            JSONArray jsonArray = jsonObject.getJSONArray("mItems");
            this.mItems = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                this.mItems.add(getItemObject(object));
            }
        } catch (JSONException e) {
            Logger.error(e);
        }
    }

    protected abstract K getItemObject(JSONObject jsonObject) throws JSONException;

    /**
     * Ключ настройки
     *
     * @return ключ
     */
    public String getKey() {
        return mKey;
    }

    /**
     * Дата создания/формирования настройки
     *
     * @return Дата создания
     * @throws ParseException
     */
    public Date getDate() throws ParseException {
        return DateUtil.convertStringToDate(mDate);
    }

    public interface IItemManager {
        String getName();
    }
}
