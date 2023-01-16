package com.luna.his.core.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.luna.his.core.constant.NoneEnum;

/**
 * @author TontoZhou
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogField {

    // 同name，用于只有一个值情况
    String value() default "";

    // 字段中文名称
    String name() default "";

    /**
     * 字段名，默认情况为字段定义名称
     */
    String field() default "";

    /**
     * 字段类型
     */
    FieldType fieldType() default FieldType.NONE;

    /**
     * 是否是多个值拼接（id拼接情况）
     */
    boolean isMulti() default false;


    /**
     * 是否过滤值相等的情况
     */
    boolean isFilterEqual() default true;

    /**
     * 日期格式类型
     *
     * @return
     */
    String dateFormat() default "yyyy-MM-dd HH:mm:ss";

    /**
     * 值转化器，优先级高于字段类型
     *
     * @return
     */
    Class<? extends FieldValueConverter> converter() default FieldValueConverter.NoneConverter.class;


    Class<? extends Enum> enumType() default NoneEnum.class;

}
