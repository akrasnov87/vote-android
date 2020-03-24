package ru.mobnius.vote.data.manager.jsonb;

import java.util.Date;

import ru.mobnius.vote.utils.DateUtil;

/**
 * Элемент массива
 */
public class JsonBItem {
    public String c_key;
    public String c_value;
    public String d_created;
    public boolean b_default;

    /**
     * Обновление даты
     */
    public void updateCreated() {
        d_created = DateUtil.convertDateToUserString(new Date());
    }
}
