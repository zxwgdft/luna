package com.luna.his.org.mapper;

import com.luna.his.api.TenantManager;
import com.luna.his.org.model.Employee;
import com.luna.framework.service.mybatis.CommonMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface EmployeeMapper extends CommonMapper<Employee> {
    @Select("SELECT id,name,cellphone,`account` FROM org_employee WHERE tenant_id = #{tenantId} AND is_manager = 1")
    List<TenantManager> findManagerByTenant(@Param("tenantId") Long tenantId);
}