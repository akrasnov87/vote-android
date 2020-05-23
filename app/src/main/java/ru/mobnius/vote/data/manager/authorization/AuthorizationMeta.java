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

    public AuthorizationMeta(int status, String message, String token, String claims, Integer userId) {
        super(status, message);

        mToken = token;
        mClaims = claims;
        mUserId = userId;
    }

    public AuthorizationMeta(int status, String message) {
        this(status, message, null, null, null);
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
}
