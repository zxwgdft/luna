package com.luna.framework.security;

import com.luna.framework.api.AuthenticationToken;

public interface AuthenticationListener {

    void onSuccess(AuthenticationToken token, UserSession userSession);

    void onFailure(AuthenticationToken token, Exception exception);

}
