package com.luna.framework.api;

public interface AuthenticationToken {

    default boolean isRememberMe() {
        return false;
    }
}
