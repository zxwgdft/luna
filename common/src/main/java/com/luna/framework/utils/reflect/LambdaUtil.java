package com.luna.framework.utils.reflect;

import com.luna.framework.utils.support.GetFunction;
import com.luna.framework.api.SystemException;
import lombok.extern.slf4j.Slf4j;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author TontoZhou
 * @since 2021/3/17
 */
@Slf4j
public class LambdaUtil {

    private static Map<String, String> fieldNameCache = new ConcurrentHashMap<>();

    /**
     * 通过get function获取字段名称
     *
     * @param fun get function
     * @return get方法对应字段名称
     */
    public static <T> String getFieldName(GetFunction<T> fun) {
        String funName = fun.getClass().getName();
        // 同一行代码生成GetFunction是唯一的，基于此我们可以缓存
        String fieldName = fieldNameCache.get(funName);
        if (fieldName == null) {
            try {
                Method method = fun.getClass().getDeclaredMethod("writeReplace");
                method.setAccessible(Boolean.TRUE);
                SerializedLambda serializedLambda = (SerializedLambda) method.invoke(fun);
                String getName = serializedLambda.getImplMethodName();
                fieldName = NameUtil.removeGetOrSet(getName);
                fieldNameCache.put(funName, fieldName);
            } catch (Exception e) {
                throw new SystemException(SystemException.CODE_ERROR_CODE, String.format("get field name error from [%s] by lambda", fun.toString()), e);
            }
        }
        return fieldName;
    }

    /**
     * 通过lambda避免硬编码对map插值
     */
    public static <T> void putToMap(T source, Map<String, Object> map, GetFunction<T>... funs) {
        for (GetFunction fun : funs) {
            Object value = fun.apply(source);
            map.put(getFieldName(fun), value);
        }
    }


}
