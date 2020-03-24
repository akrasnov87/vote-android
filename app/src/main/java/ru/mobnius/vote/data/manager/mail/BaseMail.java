package ru.mobnius.vote.data.manager.mail;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import ru.mobnius.vote.data.manager.authorization.Authorization;

public abstract class BaseMail {

    public BaseMail() {
        mFrom = Authorization.getInstance().getUser().getCredentials().login;
    }

    @Expose
    public String mGroup;

    @Expose
    public String mFrom;

    @Expose
    public String mTo;

    public String toJsonString() {
        Gson json = new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().create();
        return json.toJson(this);
    }
}
