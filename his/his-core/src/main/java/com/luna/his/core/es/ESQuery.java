package com.luna.his.core.es;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ESQuery {

    /**
     * 空表示默认字段名称
     *
     * @return
     */
    String name() default "";

    ESQueryType type();

    /**
     * 在日期判断时，有时需要用下一天作为边界判断
     * 例如判断前端查询2021-01-01日数据，前端传了个2021-01-01，后端需要转为< 2021-01-02
     *
     * @return
     */
    boolean nextDay() default false;

}
