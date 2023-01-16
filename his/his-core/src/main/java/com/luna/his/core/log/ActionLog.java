package com.luna.his.core.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作日志切入点注解
 *
 * @author TontoZhou
 * @since 2020/3/19
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionLog {

    /**
     * 模块名称
     *
     * @return
     */
    Module module();

    /**
     * 操作类型
     *
     * @return
     */
    ActionType actionType();

    /**
     * 操作名称
     *
     * @return
     */
    String action();

    /**
     * 是否存在内容（需要记录的业务数据）
     *
     * @return
     */
    boolean hasContent() default false;
}
