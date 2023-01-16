package com.luna.framework.service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在单表查询时只需返回简单的、主要的字段给前端时，可通过该注释标明返回哪个简单类型
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SimpleViewObject {

    /**
     * 返回的简单类型
     *
     * @return 类型
     */
    Class value();

}
