package ru.mobnius.vote.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import ru.mobnius.vote.data.storage.FieldNames;

/**
 * Класс для обработки JSONObject и создания из него SQL запроса на обновление записи
 */
public class SqlUpdateFromJSONObject {
    final String params;
    final String tableName;
    final String[] fields;
    final String pkColumn;

    /**
     * Конструктор
     * @param object объект для обработки
     * @param tableName имя таблицы
     * @param pkColumn имя первичного ключа
     */
    public SqlUpdateFromJSONObject(JSONObject object, String tableName, String pkColumn){
        this.tableName = tableName;
        this.pkColumn = pkColumn;

        StringBuilder builder = new StringBuilder();
        ArrayList<String> tempFields = new ArrayList<>();
        Iterator<String> keys = object.keys();
        String fieldName;
        while (keys.hasNext()){
            fieldName = keys.next();
            tempFields.add(fieldName);
            if(fieldName.equals(pkColumn)){
                continue;
            }
            builder.append(fieldName + "  = ?, ");

        }
        fields = tempFields.toArray(new String[0]);
        params = builder.substring(0, builder.length() - 2);
    }

    /**
     * запрос в БД для обновления
     * @param appendField добавить дополнительные поля
     * @return возвращается запрос
     */
    public String convertToQuery(boolean appendField) {
        String appendStr = "";
        if(appendField){
            appendStr= " and (" + FieldNames.OBJECT_OPERATION_TYPE + " = ? OR " + FieldNames.OBJECT_OPERATION_TYPE + " = ?)";
        }
        return "UPDATE " + tableName + " set " + params + " where " + pkColumn + " = ?" + (appendField ? appendStr : "");
    }

    /**
     * Получение объекта для передачи в запрос
     * @param object объект для обработки
     * @return Массив значений полей
     * @param appendField добавить дополнительные поля
     */
    public Object[] getValues(JSONObject object, boolean appendField) throws JSONException {
        ArrayList<Object> values = new ArrayList<>(fields.length);

        Object pk = null;

        for(int i =0; i < fields.length; i++){
            if(pkColumn.equals(fields[i])){
                pk = object.get(fields[i]);
                continue;
            }
            values.add(object.get(fields[i]));
        }

        values.add(pk);
        if(appendField){
            values.add(null);
            values.add("");
        }

        return values.toArray();
    }
}