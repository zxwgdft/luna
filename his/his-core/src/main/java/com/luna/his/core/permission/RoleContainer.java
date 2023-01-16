package com.luna.his.core.permission;

import java.util.Map;

/**
 * @author TontoZhou
 * @since 2021/3/29
 */
public class RoleContainer {

    private Map<Long, Role> roleMap;

    public RoleContainer(Map<Long, Role> roleMap) {
        this.roleMap = roleMap;
    }

    public Role getRole(Long roleId) {
        return roleId == null ? null : roleMap.get(roleId);
    }


}
