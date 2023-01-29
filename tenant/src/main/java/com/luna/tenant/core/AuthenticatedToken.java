package com.luna.tenant.core;

import com.luna.framework.api.AuthenticationToken;
import com.luna.tenant.model.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author TontoZhou
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticatedToken implements AuthenticationToken {

    private Account account;
    private boolean isRememberMe;

    public boolean isRememberMe() {
        return isRememberMe;
    }

}
