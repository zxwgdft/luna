package com.luna.his.config;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.luna.framework.service.ServiceSupportManager;
import com.luna.framework.service.mybatis.CommonIdentifierGenerator;
import com.luna.framework.service.mybatis.SelectSimpleList;
import com.luna.framework.service.mybatis.SelectWholeById;
import com.luna.framework.spring.SpringBeanHelper;
import com.luna.his.core.cache.TenantDataCacheHelper;
import com.luna.his.core.cache.TenantDataCacheManager;
import com.luna.his.core.framework.DeleteSafeById;
import com.luna.his.core.framework.UpdateSelectionById;
import com.luna.his.core.framework.UpdateWholeById;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Configuration
public class CommonConfiguration {

    /**
     * 租户数据缓存管理器（redis实现）
     */
    @Bean
    public TenantDataCacheManager getDataCacheFactory(RedisTemplate<String, String> stringRedisTemplate) {
        return new TenantDataCacheManager(stringRedisTemplate);
    }

    /**
     * 租户数据缓存帮助类
     *
     * @return
     */
    @Bean
    public TenantDataCacheHelper getTenantDataCacheHelper() {
        return new TenantDataCacheHelper();
    }

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
                list.add(new UpdateSelectionById());
                list.add(new SelectWholeById());
                list.add(new SelectSimpleList());
                list.add(new DeleteSafeById());
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


    /**
     * 线程池，这里主要处理带有sql、rpc等操作的业务，所以等待时间占比较高，
     * 可以根据占比设置一个较高的线程池数量，这里设置了4倍线程池
     */
    @Bean
    public ExecutorService getExecutorService() {
        int processorSize = Runtime.getRuntime().availableProcessors();
        return new ThreadPoolExecutor(processorSize * 4, processorSize * 4, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(4096), // 使用有界队列，避免OOM
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        log.warn("发生任务丢弃状况，runnable={}", r.getClass());
                    }
                });
    }

}
