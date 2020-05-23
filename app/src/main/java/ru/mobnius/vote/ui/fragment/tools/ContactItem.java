package ru.mobnius.vote.ui.fragment.tools;

public class ContactItem {
    public String c_key;
    public String c_value;
    public String d_created;
    public boolean b_default;

    public ContactItem(){
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
