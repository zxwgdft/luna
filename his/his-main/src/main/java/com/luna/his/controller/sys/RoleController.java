package com.luna.his.controller.sys;

import com.luna.framework.service.ControllerSupport;
import com.luna.his.org.model.OrgRole;
import com.luna.his.org.service.OrgRoleService;
import com.luna.his.org.service.dto.RolePermissionDTO;
import com.luna.his.org.service.dto.RoleQuery;
import com.luna.his.org.service.dto.RoleCopyDTO;
import com.luna.his.org.service.dto.RoleDTO;
import com.luna.his.org.service.vo.RolePermissionVO;
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
@Api(tags = "角色管理")
@RestController
@RequestMapping("/his/sys/role")
@RequiredArgsConstructor
public class RoleController extends ControllerSupport {

    private final OrgRoleService roleService;

    @ApiOperation("获取简单信息的角色列表")
    @PostMapping("/find/list/simple")
    public List<OrgRole> findSimpleList(@RequestBody RoleQuery query) {
        return roleService.findSimpleList(query);
    }

    @ApiOperation("获取角色权限明细")
    @GetMapping("/get/permission")
    public RolePermissionVO getRolePermission(@RequestParam Long id) {
        return roleService.getRolePermission(id);
    }

    @ApiOperation("角色保存")
    @PostMapping("/save")
    public void save(@Valid @RequestBody RoleDTO roleDTO, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        roleService.saveRole(roleDTO);
    }

    @ApiOperation("角色修改")
    @PostMapping("/update")
    public void update(@Valid @RequestBody RoleDTO roleDTO, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        roleService.updateRole(roleDTO);
    }

    @ApiOperation("角色复制")
    @PostMapping("/copy")
    public void copy(@Valid @RequestBody RoleCopyDTO roleDTO, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        roleService.copyRole(roleDTO);
    }

    @ApiOperation("授予权限")
    @PostMapping("/grant/permission")
    public void grantPermission(@Valid @RequestBody RolePermissionDTO permissionDTO, BindingResult bindingResult) {
        validErrorHandler(bindingResult);
        roleService.updateRolePermission(permissionDTO);
    }

    @ApiOperation("角色删除")
    @PostMapping("/remove")
    public void delete(@RequestParam Long id) {
        roleService.removeRole(id);
    }

}