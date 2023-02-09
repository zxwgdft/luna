package com.luna.tenant.controller;

import com.luna.framework.service.ControllerSupport;
import com.luna.tenant.api.LoginResult;
import com.luna.tenant.api.PasswordToken;
import com.luna.tenant.service.AuthenticateService;
import com.luna.tenant.service.dto.LoginUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author TontoZhou
 */
@Api(tags = "账号相关服务")
@RestController
@RequestMapping("/tenant")
@RequiredArgsConstructor
public class LoginController extends ControllerSupport {

    private final AuthenticateService authenticateService;

    @ApiOperation("账号认证")
    @PostMapping("/login")
    public LoginResult authByPassword(@RequestBody PasswordToken passwordToken, HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        return authenticateService.loginByPassword(passwordToken, request, response);
    }


    @ApiOperation("获取用户信息")
    @GetMapping("/user/info")
    public LoginUser getUserInfo() {
        return authenticateService.getCurrentUserInfo();
    }

}