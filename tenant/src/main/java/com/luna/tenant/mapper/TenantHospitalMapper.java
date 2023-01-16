package com.luna.tenant.mapper;

import com.luna.framework.service.mybatis.CommonMapper;
import com.luna.tenant.model.TenantHospital;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author TontoZhou
 */
public interface TenantHospitalMapper extends CommonMapper<TenantHospital> {

    @Select("SELECT id FROM tenant_hospital WHERE tenant_id = #{tenantId} AND is_headquarter = true")
    Long getHeadquarterId(@Param("tenantId") long tenantId);
}
