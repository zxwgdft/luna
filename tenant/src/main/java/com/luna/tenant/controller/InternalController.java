package com.luna.tenant.controller;

import com.luna.framework.service.ControllerSupport;
import com.luna.tenant.api.*;
import com.luna.tenant.client.AccountClient;
import com.luna.tenant.client.TenantClient;
import com.luna.tenant.model.Account;
import com.luna.tenant.service.AccountService;
import com.luna.tenant.service.TenantHospitalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author TontoZhou
 */
@Api(tags = "内部调用服务")
@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class InternalController extends ControllerSupport implements AccountClient, TenantClient {

    private final AccountService accountService;
    private final TenantHospitalService tenantHospitalService;

    @Override
    @ApiOperation("租户初始化")
    @PostMapping("/account/create")
    public void createAccount(@RequestBody AccountCreate accountCreate) {
        accountService.createUserAccount(accountCreate);
    }

    @Override
    @ApiOperation(value = "创建用户账号")
    @PostMapping(value = "/account/update")
    public void updateAccount(@RequestBody AccountUpdate accountUpdate) {
        accountService.updateUserAccount(accountUpdate);
    }

    @Override
    @ApiOperation(value = "更新用户账号密码")
    @PostMapping(value = "/password/update")
    public void updatePassword(PasswordUpdate passwordUpdate) {
        accountService.updateUserPassword(passwordUpdate);
    }

    @Override
    @ApiOperation(value = "更新用户账号密码")
    @PostMapping(value = "/account/delete")
    public void deleteAccount(AccountDelete accountDelete) {
        accountService.deleteUserAccount(accountDelete);
    }

    @Override
    @ApiOperation(value = "通过账号密码认证用户")
    @PostMapping(value = "/auth/password")
    public AccountUser authByPassword(@RequestBody PasswordToken passwordToken) {
        Account account = accountService.authAccountByPassword(passwordToken);
        return new AccountUser(account.getUserId(), account.getType(), account.getTenantId());
    }

    @Override
    @ApiOperation(value = "初始化诊所数据成功处理")
    @PostMapping(value = "/tenant/hospital/init/success")
    public void createHospitalSuccessHandler(@RequestParam Long hospitalId) {
        tenantHospitalService.handleHospitalInitialized(hospitalId, true);
    }

    @Override
    @ApiOperation(value = "初始化诊所数据失败处理")
    @PostMapping(value = "/tenant/hospital/init/fail")
    public void createHospitalFailHandler(@RequestParam Long hospitalId) {
        tenantHospitalService.handleHospitalInitialized(hospitalId, false);
    }
}