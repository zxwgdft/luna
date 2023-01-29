package com.luna.his.org.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.luna.framework.cache.DataCache;
import com.luna.framework.utils.StringUtil;
import com.luna.his.core.permission.Role;
import com.luna.his.core.permission.RoleContainer;
import com.luna.his.core.permission.option.*;
import com.luna.his.org.model.OrgRole;

import java.util.*;

/**
 * @author TontoZhou
 */
public class OrgRoleDataCache implements DataCache<RoleContainer> {

    private OrgRoleService roleService;
    private long tenantId;

    public OrgRoleDataCache(OrgRoleService roleService, long tenantId) {
        this.roleService = roleService;
        this.tenantId = tenantId;
    }

    @Override
    public RoleContainer loadData() {
        List<OrgRole> list = roleService.findList(new LambdaQueryWrapper<OrgRole>().eq(OrgRole::getTenantId, tenantId));
        Map<Long, Role> roleMap = new HashMap<>((int) (list.size() / 0.75 + 1));
        for (OrgRole item : list) {
            Role role = new Role();
            role.setId(item.getId());
            role.setIsDefault(item.getIsDefault());
            role.setIsAdmin(item.getIsAdmin());
            role.setDataLevel(item.getDataLevel());
            role.setIsInMyCharge(item.getIsInMyCharge());
            role.setIsVisitedMe(item.getIsVisitedMe());
            role.setReportDayLimit(item.getReportDayLimit());
            role.setWorkspace(item.getWorkspace());

            String permissions = item.getPermissions();
            if (StringUtil.isNotEmpty(permissions)) {
                String[] codes = permissions.split(",");
                Set<String> set = new HashSet<>((int) (codes.length / 0.75 + 1));
                for (String code : permissions.split(",")) {
                    set.add(code);
                }
                role.setCodes(set);
            }

            role.setBitOptions(ReportOption.class, item.getReportSet());
            role.setBitOptions(PatientContactOption.class, item.getPatientContactSet());
            role.setBitOptions(WarehouseOption.class, item.getWarehouseSet());
            role.setBitOptions(DashboardOption.class, item.getDashboardSet());
            role.setBitOptions(WorkListOption.class, item.getWorkListSet());
            role.setBitOptions(VisitOperateOption.class, item.getVisitOperateSet());

            roleMap.put(item.getId(), role);
        }
        return new RoleContainer(roleMap);
    }


    // 不需要处理，会在封装类中处理
    @Override
    public RoleContainer getData() {
        return null;
    }

}
