package com.luna.his.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.luna.framework.api.SystemException;
import com.luna.framework.cache.DataCache;
import com.luna.framework.exception.BusinessException;
import com.luna.framework.exception.IllegalRequestException;
import com.luna.framework.utils.StringUtil;
import com.luna.framework.utils.convert.JsonUtil;
import com.luna.framework.utils.convert.SimpleBeanCopyUtil;
import com.luna.his.core.cache.DataCacheConstants;
import com.luna.his.core.cache.TenantDataCacheFactory;
import com.luna.his.core.cache.TenantDataCacheHelper;
import com.luna.his.core.constant.RoleLevel;
import com.luna.his.core.framework.HisServiceSupport;
import com.luna.his.core.permission.Role;
import com.luna.his.core.permission.RoleContainer;
import com.luna.his.sys.mapper.SysRoleMapper;
import com.luna.his.sys.model.SysRole;
import com.luna.his.sys.service.dto.RolePermissionDTO;
import com.luna.his.sys.service.dto.SysRoleCopyDTO;
import com.luna.his.sys.service.dto.SysRoleDTO;
import com.luna.his.sys.service.vo.RolePermissionVO;
import com.luna.his.core.permission.option.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 角色服务
 */
@Slf4j
@Service
public class SysRoleService extends HisServiceSupport<SysRole, SysRoleMapper> implements TenantDataCacheFactory<RoleContainer> {

    /**
     * 获取角色的权限明细
     *
     * @param id
     * @return
     */
    public RolePermissionVO getRolePermission(long id) {
        RoleContainer roleContainer = TenantDataCacheHelper.getRequestCacheData(RoleContainer.class);
        Role role = roleContainer.getRole(id);
        if (role != null) {
            RolePermissionVO rolePermission = SimpleBeanCopyUtil.simpleCopy(role, new RolePermissionVO());
            rolePermission.setOptionValues(role.getSimplifiedOptionValue());
            return rolePermission;
        }
        return null;
    }

    /**
     * 新增角色
     *
     * @param orgRoleDTO
     */
    @Transactional
    public void saveRole(SysRoleDTO orgRoleDTO) {
        SysRole model = new SysRole();
        SimpleBeanCopyUtil.simpleCopy(orgRoleDTO, model);
        model.setId(null);
        model.setIsDefault(false);
        model.setIsAdmin(false);
        model.setDataLevel(RoleLevel.SELF.getCode());
        model.setIsInMyCharge(true);
        save(model);
        // 更新缓存
        TenantDataCacheHelper.dataChanged(RoleContainer.class);
    }

    /**
     * 更新角色
     *
     * @param orgRoleDTO
     */
    public void updateRole(SysRoleDTO orgRoleDTO) {
        Long id = orgRoleDTO.getId();
        if (id == null) {
            throw new IllegalRequestException();
        }
        // 只有名称可以修改，所以暂时不更新缓存
        getSqlMapper().update(null,
                new LambdaUpdateWrapper<SysRole>()
                        .eq(SysRole::getId, id)
                        .set(SysRole::getName, orgRoleDTO.getName())
        );
    }

    /**
     * 复制角色
     *
     * @param roleDTO
     */
    public void copyRole(SysRoleCopyDTO roleDTO) {
        Long id = roleDTO.getCopyId();
        SysRole role = get(id);
        role.setId(null);
        role.setName(roleDTO.getName());
        role.setIsDefault(false);
        role.setIsAdmin(false);

        save(role);
        // 更新缓存
        TenantDataCacheHelper.dataChanged(RoleContainer.class);
    }

    /**
     * 更新角色权限
     *
     * @param dto
     */
    @Transactional
    public void updateRolePermission(RolePermissionDTO dto) {
        long id = dto.getId();
        SysRole role = getWhole(id);
        if (role.getIsAdmin()) {
            throw new BusinessException("管理员角色无法修改权限");
        }

        // 设置拼接权限编码
        role.setPermissions(StringUtil.join(dto.getCodes()));

        // 数据查看权限
        int dataLevel = dto.getDataLevel();
        if (dataLevel == RoleLevel.SELF.getCode()) {
            boolean isInMyCharge = dto.getIsInMyCharge() == null ? false : dto.getIsInMyCharge();
            boolean isVisitedMe = dto.getIsVisitedMe() == null ? false : dto.getIsVisitedMe();
            if (!isInMyCharge && !isVisitedMe) {
                // 必须有一个选择
                throw new IllegalRequestException();
            }
            role.setIsInMyCharge(isInMyCharge);
            role.setIsVisitedMe(isVisitedMe);
        }
        role.setDataLevel(dataLevel);

        // 设置报表限制天数，0代表不限制
        Integer limit = dto.getReportDayLimit();
        if (limit == null || limit < 0) {
            limit = 0;
        }
        role.setReportDayLimit(limit);

        // 基于bitOption的配置
        Map<String, Map<String, Boolean>> optionValues = dto.getOptionValues();
        role.setReportSet(getSetValue(ReportOption.class, optionValues));
        role.setPatientContactSet(getSetValue(PatientContactOption.class, optionValues));
        role.setWarehouseSet(getSetValue(WarehouseOption.class, optionValues));

        // 我的工作台配置
        boolean isDefault = role.getIsDefault();
        int workspaceCode = dto.getWorkspace();
        if (isDefault) {
            // 默认角色的工作台不能修改类型
            if (role.getWorkspace() != workspaceCode) {
                throw new IllegalRequestException();
            }
        }
        Workspace workspace = Workspace.getWorkspaceByCode(workspaceCode);
        if (workspace == null) {
            role.setDashboardSet(0L);
            role.setWorkListSet(0L);
            role.setVisitOperateSet(0L);
        } else {
            role.setDashboardSet(getSetValue(DashboardOption.class, optionValues, workspace.getDashboardOptions()));
            role.setWorkListSet(getSetValue(WorkListOption.class, optionValues, workspace.getWorkListOptions()));
            role.setVisitOperateSet(getSetValue(VisitOperateOption.class, optionValues, workspace.getVisitOperateOptions()));
        }

        updateWhole(role);
        // 更新缓存
        TenantDataCacheHelper.dataChanged(RoleContainer.class);
    }

    /**
     * 获取配置值集（受模板限制的）
     */
    private <T extends BitOption> long getSetValue(Class<T> clazz,
                                                   Map<String, Map<String, Boolean>> optionValuesMap, T[] limitOptions) {
        long setValue = 0;
        if (optionValuesMap != null || (limitOptions != null && limitOptions.length > 0)) {
            String name = clazz.getSimpleName();
            Map<String, Boolean> valueMap = optionValuesMap.get(name);
            if (valueMap != null) {
                for (T bitOption : clazz.getEnumConstants()) {
                    Boolean value = valueMap.get(((Enum) bitOption).name());
                    if (value != null && value) {
                        // 必须在限制的配置项
                        for (T t : limitOptions) {
                            if (t == bitOption) {
                                setValue |= 1L << bitOption.getPosition();
                                break;
                            }
                        }
                    }
                }
            }
        }
        return setValue;
    }

    /**
     * 获取配置值集
     */
    private long getSetValue(Class<? extends BitOption> clazz, Map<String, Map<String, Boolean>> optionValuesMap) {
        long setValue = 0;
        if (optionValuesMap != null) {
            String name = clazz.getSimpleName();
            Map<String, Boolean> valueMap = optionValuesMap.get(name);
            if (valueMap != null) {
                for (BitOption bitOption : clazz.getEnumConstants()) {
                    Boolean value = valueMap.get(((Enum) bitOption).name());
                    if (value != null && value) {
                        setValue |= 1L << bitOption.getPosition();
                    }
                }
            }
        }
        return setValue;
    }

    /**
     * 删除角色（非系统角色）
     *
     * @param id
     */
    public void removeRole(Long id) {
        getSqlMapper().delete(
                new LambdaQueryWrapper<SysRole>()
                        .eq(SysRole::getId, id)
                        .eq(SysRole::getIsDefault, false)
                        .eq(SysRole::getTenantId, getHisUserSession().getTenantId())
        );
        // 更新缓存
        TenantDataCacheHelper.dataChanged(RoleContainer.class);
    }


    @Override
    public String getCacheId() {
        return DataCacheConstants.CACHE_ROLE;
    }

    @Override
    public DataCache<RoleContainer> createDataCache(long tenantId) {
        return new SysRoleDataCache(this, tenantId);
    }

    /**
     * 初始化租户的角色数据
     *
     * @return 管理员角色ID
     */
    public long initTenantRole(long tenantId) {
        try (InputStream inputStream = getClass().getResourceAsStream("/configJson/Role.json")) {
            Long adminId = null;
            List<SysRole> roles = JsonUtil.parseJsonList(inputStream, SysRole.class);
            for (SysRole role : roles) {
                role.setTenantId(tenantId);
                save(role);
                if (role.getIsAdmin()) {
                    adminId = role.getId();
                }
            }
            return adminId;
        } catch (IOException e) {
            log.error("初始化租户角色数据异常", e);
            throw new SystemException(SystemException.CODE_ERROR_CODE, "初始化租户角色数据异常");
        }
    }

}