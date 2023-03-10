package com.luna.framework.service;

import com.luna.framework.service.mybatis.CommonMapper;
import com.luna.framework.spring.SpringBeanHelper;
import com.luna.framework.utils.reflect.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 业务支持类容器，启动时为{@link ServiceSupport}自动注入SqlMapper
 *
 * @author TontoZhou
 */
@SuppressWarnings("ALL")
@Order(1)
@Slf4j
public class ServiceSupportManager implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {

        /**
         * 根据泛型类型为service support注入相应的sqlMapper
         */

        Map<String, CommonMapper> commonMappers = SpringBeanHelper.getBeansByType(CommonMapper.class);
        Map<String, ServiceSupport> serviceSupports = SpringBeanHelper.getBeansByType(ServiceSupport.class);

        Map<Class<?>, CommonMapper> mapperMap = new HashMap<>();

        for (Entry<String, CommonMapper> entry : commonMappers.entrySet()) {
            CommonMapper mapper = entry.getValue();
            Class<?> genericType = ReflectUtil.getSuperClassArgument(mapper.getClass(), CommonMapper.class, 0);

            if (genericType == null || genericType == Object.class) {
                log.warn("[" + mapper.getClass().getName() + "]的实现类没有明确定义[" + CommonMapper.class.getName() + "]的泛型");
                continue;
            }

            CommonMapper oldMapper = mapperMap.get(genericType);
            if (oldMapper != null) {
                log.warn("实体类[" + genericType.getName() + "]存在多个CommonMapper实现类，[" + oldMapper.getClass().getName() + "]将被覆盖");
            }

            mapperMap.put(genericType, mapper);
        }

        for (Entry<String, ServiceSupport> entry : serviceSupports.entrySet()) {
            ServiceSupport support = entry.getValue();
            Class<?> genericType = ReflectUtil.getSuperClassArgument(support.getClass(), ServiceSupport.class, 0);

            if (genericType == null || genericType == Object.class) {
                log.warn("[" + support.getClass().getName() + "]的实现类没有明确定义[" + ServiceSupport.class.getName() + "]的泛型，无法为其注册CommonMapper");
                continue;
            }

            CommonMapper mapper = mapperMap.get(genericType);
            if (mapper == null) {
                log.warn("实体类[" + genericType.getName() + "]没有对应的[" + CommonMapper.class.getName() + "]的实现类");
                continue;
            } else {
                support.setSqlMapper(mapper);
                log.debug("===>为[" + support.getClass().getName() + "]注入CommonMapper");
            }

            support.init();
            log.debug("<===[" + support.getClass().getName() + "]初始化成功");
        }
    }


}
