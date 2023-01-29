package com.luna.tenant.client;

import com.luna.tenant.api.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "luna-tenant", contextId = "tenant-account")
public interface AccountClient {

    @ApiOperation(value = "创建用户账号")
    @PostMapping(value = "/internal/account/create")
    void createAccount(@RequestBody AccountCreate accountCreate);

    @ApiOperation(value = "更新用户账号信息")
    @PostMapping(value = "/internal/account/update")
    void updateAccount(@RequestBody AccountUpdate accountUpdate);

    @ApiOperation(value = "更新用户账号密码")
    @PostMapping(value = "/internal/password/update")
    void updatePassword(@RequestBody PasswordUpdate passwordUpdate);

    @ApiOperation(value = "更新用户账号密码")
    @PostMapping(value = "/internal/account/delete")
    void deleteAccount(@RequestBody AccountDelete accountDelete);

    @ApiOperation(value = "通过账号密码认证用户")
    @PostMapping(value = "/internal/auth/password")
    AccountUser authByPassword(@RequestBody PasswordToken passwordToken);

}
