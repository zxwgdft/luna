package com.luna.his.controller.core;

import com.luna.constant.GlobalConstants;
import com.luna.framework.security.WebSecurityManager;
import com.luna.his.core.HisUserSession;
import com.luna.his.core.service.LoginUserService;
import com.luna.his.core.service.dto.LoginUser;
import com.luna.tenant.api.LoginResult;
import com.luna.tenant.api.PasswordToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 认证服务接口
 *
 * @author TontoZhou
 */
@Api(tags = "登录认证服务")
@RestController
@RequestMapping("")
public class AuthenticateController {

    @Autowired
    private WebSecurityManager webSecurityManager;
    @Autowired
    private LoginUserService loginUserService;

    @ApiOperation("医生认证")
    @PostMapping(value = "/his/login")
    public LoginResult login(@RequestBody PasswordToken loginUser, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // token 由前端自己负责存储和传递
        String token = webSecurityManager.authorize(loginUser, request, response);
        return new LoginResult(token, "", GlobalConstants.USER_TYPE_EMPLOYEE);
    }

    @ApiOperation("获取用户信息")
    @GetMapping(value = "/his/user/info")
    public LoginUser getUser() {
        return loginUserService.getLoginUser();
    }

    @ApiOperation("设置用户当前工作诊所")
    @ApiResponse(description = "新的token")
    @PostMapping(value = "/his/user/set/hospital")
    public String setCurHospital(@RequestParam Long id) {
        return loginUserService.setCurHospital(id);
    }


}