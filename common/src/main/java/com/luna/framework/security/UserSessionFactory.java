package com.luna.framework.security;

public interface UserSessionFactory {

    UserSession createUserSession(UserClaims userClaims);

}
