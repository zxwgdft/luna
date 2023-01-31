package com.luna.tenant.mapper;

import com.luna.framework.service.mybatis.CommonMapper;
import com.luna.tenant.model.Server;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author TontoZhou
 */
public interface ServerMapper extends CommonMapper<Server> {

    @Select("SELECT a.id FROM `server` a INNER JOIN `tenant` b ON a.id = b.server WHERE b.id = #{tenantId}")
    String getServerIdByTenant(@Param("tenantId") long tenantId);

}
