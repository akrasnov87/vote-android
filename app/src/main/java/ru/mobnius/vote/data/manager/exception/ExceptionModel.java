package ru.mobnius.vote.data.manager.exception;

import android.content.Context;
import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import java.util.Date;
import java.util.UUID;

import ru.mobnius.vote.data.manager.DbOperationType;
import ru.mobnius.vote.data.manager.configuration.PreferencesManager;
import ru.mobnius.vote.data.storage.models.ClientErrors;
import ru.mobnius.vote.utils.DateUtil;
import ru.mobnius.vote.utils.HardwareUtil;
import ru.mobnius.vote.utils.StringUtil;
import ru.mobnius.vote.utils.VersionUtil;

/**
 * модель ошибки
 */
public class ExceptionModel {

    public static ExceptionModel getInstance(Date date, String message, String group, int code) {
        return new ExceptionModel(date, message, group, code);
    }

    @Expose
    private String id;

    /**
     * Текст сообщения об ошибке
     */
    @Expose
    private String message;

    @Expose
    private int code;

    @Expose
    private String group;

    /**
     * Дата возникновения ошибки
     */
    private Date date;

    private ExceptionModel(Date date, String message, String group, int code) {
        this.id = DateUtil.convertDateToString(date);
        this.date = date;
        this.message = message;
        this.group = group;
        this.code = code;
    }

    public Date getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public String getId() {
        return id;
    }

    public int getCode() {
        return code;
    }

    public String getGroup() {
        return group;
    }

    /**
     * Имя файла для хранения исключения
     * @return
     */
    public String getFileName(){
        return String.format("%s.exc", this.id);
    }

    /**
     * Получение кода ошибки
     * @param isDebug включен ли режим отладки
     * @return код ошибки
     */
    public String getExceptionCode(boolean isDebug) {
        return String.format("%s%s%s", getGroup(), ExceptionUtils.codeToString(getCode()), isDebug ? "D" : "E");
    }

    /**
     * Получение дополнительных данных
     * @param context контекст
     * @return строка с данными в формате JSON
     */
    public String getJSONData(Context context) {
        return "{\"c_imei\":\"" + HardwareUtil.getIMEI(context) + "\",\"d_current_date\":\"" + DateUtil.convertDateToString(new Date()) + "\", \"c_md5\":\"" + StringUtil.md5(getMessage()) + "\"}";
    }

    @NonNull
    @Override
    public String toString() {
        Gson json = new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().create();
        return json.toJson(this);
    }

    /**
     * Преобразование в запись БД
     * @param context контекст
     * @param userID Пользователь
     * @return Объект
     */
    public ClientErrors toDbItem(Context context, long userID){
        ClientErrors clientError = new ClientErrors();
        boolean isDebug = PreferencesManager.getInstance() != null && PreferencesManager.getInstance().isDebug();
        clientError.c_code = getExceptionCode(isDebug);
        clientError.c_message = getMessage();
        clientError.c_platform = "ANDROID";
        clientError.d_created = getId();
        clientError.c_version = VersionUtil.getVersionName(context);
        clientError.fn_user = userID;
        clientError.jb_data = getJSONData(context);
        clientError.objectOperationType = DbOperationType.CREATED;
        clientError.id = UUID.randomUUID().toString();

        return clientError;
    }
}
