package ru.mobnius.vote.ui.fragment.tools;

import java.util.Date;

import ru.mobnius.vote.utils.DateUtil;

public class ContactItem {
    public String c_key;
    public String c_value;
    public String d_created;
    public boolean b_default;

    public ContactItem(){
    }

    public ContactItem(String key, String value){
        this.c_value = value;
        this.c_key = key;
        this.d_created = DateUtil.convertDateToUserString(new Date());
    }

    public void setDefault(boolean value){
        this.b_default = value;
    }

    public void setKey(String key){
        this.c_key = key;
    }

    public void setValue(String value) {
        this.c_value = value;
    }
}
