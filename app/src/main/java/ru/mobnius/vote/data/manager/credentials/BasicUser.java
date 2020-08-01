package ru.mobnius.vote.data.manager.credentials;

import ru.mobnius.vote.utils.LongUtil;

/**
 * Объект пользователя, авторизовавшегося в приложении
 */
public class BasicUser {

    /**
     * список ролей у пользователя. Разделителем является точка
     */
    public final String claims;

    private final BasicCredentials credentials;
    private final Object UserId;
    private final String[] roles;

    public final static String CANDIDATE = "candidate";

    public BasicUser(BasicCredentials credentials, Object userId, String claims){
        this.credentials = credentials;
        UserId = userId;
        String trimClaims = claims.replaceAll("^.", "").replaceAll(".$", "");
        roles = trimClaims.split("\\.");
        this.claims = claims;
    }

    public boolean userInRole(String roleName) {
        for(String s : roles){
            if(s.equals(roleName))
                return  true;
        }

        return false;
    }

    /**
     * является кандитаном
     * @return true - является
     */
    public boolean isCandidate() {
        for (String s: roles) {
            if(s.equals(CANDIDATE))
                return true;
        }

        return false;
    }

    public Long getUserId() {
        return LongUtil.convertToLong(UserId);
    }

    /**
     * Возращается объект с данным для авторизации
     * @return данные об авторизации
     */
    public BasicCredentials getCredentials() {
        return credentials;
    }
}
