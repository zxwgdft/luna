package com.luna.his.controller.internal;

import com.luna.framework.service.ControllerSupport;
import com.luna.his.api.AuthenticatedUser;
import com.luna.his.api.HospitalCreateParam;
import com.luna.his.api.InternalRequestPath;
import com.luna.his.api.ManagerCreateParam;
import com.luna.his.core.service.LoginUserService;
import com.luna.his.core.service.TenantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;

/**
 * @author TontoZhou
 */
@Api(tags = "内部服务管理")
@RestController
@RequestMapping
@RequiredArgsConstructor
public class InternalController extends ControllerSupport {

    private final TenantService tenantService;
    private final LoginUserService loginUserService;
    private final ExecutorService executorService;

    @ApiOperation("租户诊所初始化")
    @PostMapping(InternalRequestPath.TENANT_HOSPITAL_INIT)
    public void initTenant(@RequestBody HospitalCreateParam param) {
        executorService.execute(() -> {
            tenantService.createHospital(param);
        });
    }

    @ApiOperation("租户管理员初始化")
    @PostMapping(InternalRequestPath.TENANT_MANAGER_INIT)
    public void initTenant(@RequestBody ManagerCreateParam param) {
        tenantService.createManager(param);
    }

    @ApiOperation("获取用户token")
    @PostMapping(InternalRequestPath.GET_USER_TOKEN)
    public String getUserToken(@RequestBody AuthenticatedUser user) {
        // 认证成功后获取用户token并进行相应处理
        return loginUserService.getUserToken(user);
    }


}