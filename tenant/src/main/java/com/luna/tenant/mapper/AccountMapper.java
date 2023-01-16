package com.luna.tenant.mapper;

import com.luna.framework.service.mybatis.CommonMapper;
import com.luna.tenant.model.Account;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface AccountMapper extends CommonMapper<Account> {

    /**
     * 更新用户最近一次登录时间
     */
    @Update("UPDATE account SET last_login_time = now() WHERE id = #{id}")
    int updateLastLoginTime(@Param("id") String id);

    /**
     * 查询有没有其他用户使用该账号
     */
    @Update("SELECT COUNT(id) FROM account WHERE account = #{account} AND (`type` != #{userType} OR user_id != #{userId})")
    int countOtherUserByAccount(@Param("account") String account, @Param("userType") int userType, @Param("userId") long userId);
}
