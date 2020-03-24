package ru.mobnius.vote.data.manager.authorization;

import ru.mobnius.vote.data.Meta;

/**
 * Класс расширения мета информации для авторизации
 */
public class AuthorizationMeta extends Meta {
    /**
     * токен авторизации
     */
    private final String mToken;
    /**
     * список ролей
     */
    private final String mClaims;
    /**
     * идентификатор пользователя
     */
    private final Integer mUserId;

    /**
     * Полный текст ошибки
     */
    private String mFullMessage;

    public AuthorizationMeta(int status, String message, String token, String claims, Integer userId) {
        super(status, message);

        mToken = token;
        mClaims = claims;
        mUserId = userId;
    }

    public AuthorizationMeta(int status, String message, String fullMessage) {
        this(status, message, null, null, null);

        mFullMessage = fullMessage;
    }

    public String getToken() {
        return mToken;
    }

    public String getClaims() {
        return mClaims;
    }

    public Integer getUserId() {
        return mUserId;
    }

    public String getFullMessage() {
        return mFullMessage;
    }
}
