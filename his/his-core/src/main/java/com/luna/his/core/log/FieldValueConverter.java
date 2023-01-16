package com.luna.his.core.log;

/**
 * 日志字段值转化器
 *
 * @author TontoZhou
 */
public interface FieldValueConverter {

    /**
     * 字段值转化
     *
     * @param fieldVal 字段值
     * @param obj      字段所在对象
     * @param logField 字段注解
     * @return
     */
    Object convert(Object fieldVal, Object obj, LogField logField);

    /**
     * 无转化器，用于注解默认值
     */
    class NoneConverter implements FieldValueConverter {
        @Override
        public Object convert(Object fieldVal, Object obj, LogField logField) {
            throw new RuntimeException("方法不应该被执行");
        }
    }

}
