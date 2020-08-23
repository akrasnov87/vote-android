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

    private final String mServerDate;

    public AuthorizationMeta(int status, String message, String token, String claims, Integer userId, String serverDate) {
        super(status, message);

        mToken = token;
        mClaims = claims;
        mUserId = userId;
        mServerDate = serverDate;
    }

    public AuthorizationMeta(int status, String message) {
        this(status, message, null, null, null, null);
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

    public String getServerDate() {
        return mServerDate;
    }
}
