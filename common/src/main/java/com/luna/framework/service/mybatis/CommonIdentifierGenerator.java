package com.luna.framework.service.mybatis;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.luna.framework.utils.UUIDUtil;

public class CommonIdentifierGenerator extends DefaultIdentifierGenerator {

    @Override
    public String nextUUID(Object entity) {
        return UUIDUtil.createUUID();
    }
}
