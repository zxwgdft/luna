package com.luna.framework.spring;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.luna.framework.utils.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 获取Spring Bean的工具类
 * <p>
 * 方便静态获取无须继承接口，但必须在Spring容器启动后才能使用
 *
 * @author TontZhou
 */
@SuppressWarnings("ALL")
@Slf4j
public class SpringBeanHelper implements BeanFactoryPostProcessor, ApplicationContextAware {

    private static ApplicationContext appContext;
    private static ConfigurableListableBeanFactory beanFactory;

    @SuppressWarnings("static-access")
    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = appContext;
    }

    @SuppressWarnings("static-access")
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException
    {
        this.beanFactory = beanFactory;
    }

    /**
     * @param name
     * @return 如果异常或找不到则返回null
     */
    public static Object getBean(String name) {
        try {
            return appContext.getBean(name);
        } catch (Exception e) {
            log.error("获取SpringBean(Name:" + name + ")失败", e);
            return null;
        }
    }

    /**
     * @param name
     * @param requiredType
     * @return 如果异常或找不到则返回null
     */
    public static <T> T getBean(String name, Class<T> requiredType) {
        try {
            return appContext.getBean(name, requiredType);
        } catch (Exception e) {
            log.error("获取SpringBean(Name:" + name + "/Class:" + requiredType.getName() + ")失败", e);
            return null;
        }
    }

    /**
     * 获取某一类的所有bean
     *
     * @param type
     * @return 如果异常或找不到则返回null
     */
    public static <T> Map<String, T> getBeansByType(Class<T> type) {
        try {
            return appContext.getBeansOfType(type);
        } catch (Exception e) {
            log.error("获取SpringBeansMap(Class:" + type.getName() + ")失败", e);
            return null;
        }
    }

    /**
     * 获取某一类的所有bean
     *
     * @param type                 bean的类型
     * @param includeNonSingletons 是否允许非单例
     * @param allowEagerInit       是否初始化lazy-init的bean
     * @return 如果异常或找不到则返回null
     */
    public static <T> Map<String, T> getBeansByType(Class<T> type, boolean includeNonSingletons,
                                                    boolean allowEagerInit) {
        try {
            return appContext.getBeansOfType(type, includeNonSingletons, allowEagerInit);
        } catch (Exception e) {
            log.error("获取SpringBeansMap(Class:" + type.getName() + ")失败", e);
            return null;
        }

    }

    /**
     * 获取某一类的所有bean
     *
     * @param type
     * @return 如果异常或找不到则返回null
     */
    public static <T> T getFirstBeanByType(Class<T> type) {
        try {
            Map<String, T> beansMap = appContext.getBeansOfType(type);
            return getMapFirstValue(beansMap);
        } catch (Exception e) {
            log.error("获取SpringBean(Class:" + type.getName() + ")失败", e);
            return null;
        }
    }

    /**
     * 获取某一类的所有bean中的第一个
     *
     * @param type                 bean的类型
     * @param includeNonSingletons 是否允许非单例
     * @param allowEagerInit       是否初始化lazy-init的bean
     * @return 如果异常或找不到则返回null
     */
    public static <T> T getFirstBeanByType(Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) {
        try {
            Map<String, T> beansMap = appContext.getBeansOfType(type, includeNonSingletons, allowEagerInit);
            return getMapFirstValue(beansMap);
        } catch (Exception e) {
            log.error("获取SpringBean(Class:" + type.getName() + ")失败", e);
            return null;
        }

    }

    private static <T> T getMapFirstValue(Map<String, T> map) {
        for (T t : map.values()) {
            return t;
        }
        return null;
    }


    /**
     * 注册bean
     *
     * @param beanName 注册的bean名称
     * @param clazz 类型
     * @param function bean定义
     * @return 注册的bean实例
     */
    public static <T> T registerBean(String beanName, Class<T> clazz, Function<BeanDefinitionBuilder, AbstractBeanDefinition> function) {
        // 生成bean定义
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        // 执行bean处理函数
        AbstractBeanDefinition beanDefinition = function.apply(beanDefinitionBuilder);
        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) SpringBeanHelper.beanFactory;
        // 判断是否通过beanName注册
        if (StringUtil.isNotEmpty(beanName) && !appContext.containsBean(beanName)) {
            beanFactory.registerBeanDefinition(beanName, beanDefinition);
            return getBean(beanName, clazz);
        } else {
            // 非命名bean注册
            String name = BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, beanFactory);
            return getBean(name, clazz);
        }
    }


    /**
     * 注册bean
     *
     * @param beanName 注册的bean名称
     * @param clazz 类型
     * @param args 构造参数
     * @param property bean属性集
     * @return 注册的bean实例
     */
    public static <T> T registerBean(String beanName, Class<T> clazz, List<Object> args, Map<String, Object> property) {
        return registerBean(beanName, clazz, beanDefinitionBuilder -> {
            // 放入构造参数
            if (CollectionUtils.isNotEmpty(args)) {
                args.forEach(beanDefinitionBuilder::addConstructorArgValue);
            }
            // 放入属性
            if (MapUtils.isNotEmpty(property)) {
                property.forEach(beanDefinitionBuilder::addPropertyValue);
            }
            return beanDefinitionBuilder.getBeanDefinition();
        });

    }
}
