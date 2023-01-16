package com.luna.his.core.permission;

import com.luna.framework.security.SecurityManager;
import com.luna.his.core.HisUserSession;
import com.luna.his.core.cache.TenantDataCacheHelper;
import com.luna.his.core.permission.option.BitOption;

/**
 * 权限工具类
 */
public class PermissionUtil {

    /**
     * 判断当前用户是否有该权限
     * <p>
     * 注意：必须有当前用户
     *
     * @param permissionCode 权限编码
     * @return 是否有权限
     */
    public static boolean hasPermission(String permissionCode) {
        return hasPermission((HisUserSession) SecurityManager.getCurrentUserSession(), permissionCode);
    }

    /**
     * 判断用户是否有该权限
     *
     * @param userSession    用户
     * @param permissionCode 权限编码
     * @return 是否有权限
     */
    public static boolean hasPermission(HisUserSession userSession, String permissionCode) {
        RoleContainer roleContainer = TenantDataCacheHelper.getRequestCacheData(RoleContainer.class);
        if (roleContainer != null) {
            for (Long roleId : userSession.getRoleIds()) {
                if (roleContainer.getRole(roleId).hasPermission(permissionCode)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 获取基于BitOption实现的当前用户的权限
     *
     * @param bitOption 配置
     * @return
     */
    public static boolean hasPermissionByBitOption(BitOption bitOption) {
        return hasPermissionByBitOption((HisUserSession) SecurityManager.getCurrentUserSession(), bitOption);
    }

    /**
     * 获取基于BitOption实现的用户的权限
     *
     * @param userSession 用户
     * @param bitOption   配置
     * @return
     */
    public static boolean hasPermissionByBitOption(HisUserSession userSession, BitOption bitOption) {
        RoleContainer roleContainer = TenantDataCacheHelper.getRequestCacheData(RoleContainer.class);
        Class clazz = bitOption.getClass();
        String key = ((Enum) bitOption).name();
        for (Long id : userSession.getRoleIds()) {
            Role role = roleContainer.getRole(id);
            if (role != null) {
                if (role.getOptionValue(clazz, key)) {
                    return true;
                }
            }
        }
        return false;
    }

}
