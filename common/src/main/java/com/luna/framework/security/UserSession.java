package com.luna.framework.security;

import java.io.Serializable;

public interface UserSession<T extends Serializable> extends Serializable {

    /**
     * 用户ID
     *
     * @return
     */
    T getUserId();

    /**
     * 用户名称
     *
     * @return
     */
    String getUserName();


}
