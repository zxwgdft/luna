package com.luna.tenant.config;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.luna.framework.service.ServiceSupportManager;
import com.luna.framework.service.mybatis.CommonIdentifierGenerator;
import com.luna.framework.service.mybatis.SelectSimpleList;
import com.luna.framework.service.mybatis.SelectWholeById;
import com.luna.framework.service.mybatis.UpdateWholeById;
import com.luna.framework.spring.SpringBeanHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author TontoZhou
 * @since 2019/12/10
 */
@Slf4j
@Configuration
public class TenantConfiguration {


    //---------------------------------------
    //
    // 以下注入为系统通用实例，与具体业务和项目无关
    //
    //---------------------------------------

    /**
     * spring bean 获取帮助类
     */
    @Bean
    public SpringBeanHelper springBeanHolder() {
        return new SpringBeanHelper();
    }

    /**
     * 基于mybatis plus和业务封装的支持类管理启用
     */
    @Bean
    public ServiceSupportManager getServiceSupportManager() {
        return new ServiceSupportManager();
    }

    /**
     * 扩展mybatis plus 通用方法
     */
    @Bean
    public ISqlInjector getCommonSqlInjector() {
        return new DefaultSqlInjector() {
            @Override
            public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
                List<AbstractMethod> list = super.getMethodList(mapperClass, tableInfo);
                list.add(new UpdateWholeById());
                list.add(new SelectWholeById());
                list.add(new SelectSimpleList());
                return list;
            }
        };
    }

    /**
     * 自定义主键生成器
     */
    @Bean
    public IdentifierGenerator getIdentifierGenerator() {
        return new CommonIdentifierGenerator();
    }

}
