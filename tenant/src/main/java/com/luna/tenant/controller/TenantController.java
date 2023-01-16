package com.luna.tenant.controller;

import com.luna.framework.service.ControllerSupport;
import com.luna.tenant.service.TenantHospitalService;
import com.luna.tenant.service.TenantService;
import com.luna.tenant.service.dto.TenantDTO;
import com.luna.tenant.service.dto.TenantHospitalDTO;
import com.luna.tenant.service.dto.TenantManagerDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author TontoZhou
 */
@Api(tags = "租户管理")
@RestController
@RequestMapping("/tenant")
@RequiredArgsConstructor
public class TenantController extends ControllerSupport {

    private final TenantService tenantService;
    private final TenantHospitalService tenantHospitalService;

    @ApiOperation("租户创建")
    @PostMapping("/create")
    public void createTenant(@RequestBody @Valid TenantDTO tenantDTO, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        tenantService.addTenant(tenantDTO);
    }

    @ApiOperation("租户创建系统管理员")
    @PostMapping("/create/manager")
    public void createManager(@RequestBody TenantManagerDTO param, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        tenantService.createManager(param);
    }

    @ApiOperation("租户创建系统管理员")
    @PostMapping("/create/hospital")
    public void createHospital(@RequestBody TenantHospitalDTO param, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        tenantHospitalService.createHospital(param);
    }

}