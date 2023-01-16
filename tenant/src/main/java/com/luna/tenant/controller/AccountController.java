package com.luna.tenant.controller;

import com.luna.framework.service.ControllerSupport;
import com.luna.tenant.service.AccountService;
import com.luna.tenant.api.LoginResult;
import com.luna.tenant.api.PasswordToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author TontoZhou
 */
@Api(tags = "内部调用服务")
@RestController
@RequestMapping("/tenant/account")
@RequiredArgsConstructor
public class AccountController extends ControllerSupport {

    private final AccountService accountService;

    @ApiOperation("账号认证")
    @PostMapping("/auth/password")
    public LoginResult authByPassword(@RequestBody PasswordToken passwordToken, HttpServletRequest request) {
        return accountService.loginByPassword(passwordToken, request);
    }


}