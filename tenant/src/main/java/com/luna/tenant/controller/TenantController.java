package com.luna.tenant.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.luna.framework.api.PageParam;
import com.luna.framework.api.PageResult;
import com.luna.framework.service.ControllerSupport;
import com.luna.tenant.model.Server;
import com.luna.tenant.model.Tenant;
import com.luna.tenant.model.TenantHospital;
import com.luna.tenant.service.TenantHospitalService;
import com.luna.tenant.service.TenantService;
import com.luna.tenant.service.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @ApiOperation("获取租户")
    @GetMapping("/get")
    public Tenant getTenant(@RequestParam Long id) {
        return tenantService.get(id);
    }

    @ApiOperation("查询租户列表")
    @PostMapping("/find/page")
    public PageResult<Tenant> findTenantPage(TenantQuery query) {
        return tenantService.findPage(query);
    }

    @ApiOperation("租户创建")
    @PostMapping("/create")
    public void createTenant(@RequestBody @Valid TenantDTO tenantDTO, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        tenantService.addTenant(tenantDTO);
    }

    @ApiOperation("租户更新")
    @PostMapping("/update")
    public void updateTenant(@RequestBody @Valid TenantUpdateDTO tenantDTO, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        tenantService.updateTenant(tenantDTO);
    }

    @ApiOperation("锁定租户")
    @PostMapping("/lock")
    public void lockTenant(@RequestParam Long id) {
        tenantService.lockTenant(id, true);
    }

    @ApiOperation("解锁租户")
    @PostMapping("/unlock")
    public void unlockTenant(@RequestParam Long id) {
        tenantService.lockTenant(id, false);
    }

    @ApiOperation("租户创建诊所管理员")
    @PostMapping("/create/manager")
    public void createManager(@RequestBody TenantManagerDTO param, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        tenantService.createManager(param);
    }

    @ApiOperation("租户创建诊所")
    @PostMapping("/create/hospital")
    public void createHospital(@RequestBody TenantHospitalDTO param, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        tenantHospitalService.createHospital(param);
    }

    @ApiOperation("获取租户诊所列表")
    @GetMapping("/hospital/list")
    public List<TenantHospital> getTenantHospitalList(@RequestParam Long tenantId) {
        return tenantHospitalService.findList(new LambdaQueryWrapper<TenantHospital>().eq(TenantHospital::getTenantId, tenantId));
    }
}