package ru.mobnius.vote.data.manager.mail;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import ru.mobnius.vote.data.manager.packager.PackageUtil;

public class StringMail extends BaseMail {

    public static StringMail getInstance(byte[] buffer) {
        try {
            String result = PackageUtil.readString(buffer, false);
            return new Gson().fromJson(result, StringMail.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public StringMail(String message) {
        super();
        mBody = message;
    }

    @Expose
    private final String mBody;
}
