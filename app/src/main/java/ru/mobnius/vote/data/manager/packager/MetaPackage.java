package ru.mobnius.vote.data.manager.packager;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

/**
 * метаинформация пакета
 */
public class MetaPackage {
    @Expose
    public int stringSize;
    @Expose
    public int binarySize;
    @Expose
    public MetaAttachment[] attachments;
    @Expose
    public boolean transaction;
    @Expose
    public String dataInfo;
    @Expose
    public String version;
    @Expose
    public final String id;

    public MetaPackage(String tid){
        id = tid;
    }

    public String toJsonString() {
        Gson json = new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().create();
        return json.toJson(this);
    }
}
