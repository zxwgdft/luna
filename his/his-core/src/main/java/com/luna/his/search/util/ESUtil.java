package com.luna.his.search.util;

import co.elastic.clients.elasticsearch._types.mapping.*;
import com.luna.framework.utils.reflect.Entity;
import com.luna.framework.utils.reflect.EntityField;
import com.luna.framework.utils.reflect.NameUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author TontoZhou
 */
public class ESUtil {

    /**
     * 获取index名称
     *
     * @param clazz index对应实体类
     * @return
     */
    public static String getIndexName(Class<?> clazz) {
        return NameUtil.hump2underline(Entity.getEntity(clazz).getEntityName());
    }

    /**
     * 分割转列表
     *
     * @param value
     * @return
     */
    public static List<String> splitToList(String value) {
        return splitToList(value, ",");
    }

    /**
     * 分割转列表
     *
     * @param value
     * @param separator
     * @return
     */
    public static List<String> splitToList(String value, String separator) {
        if (value == null || value.length() == 0) {
            return Collections.EMPTY_LIST;
        }
        String[] arr = value.split(separator);
        return Arrays.asList(arr);
    }


    /**
     * 简单获取字段对应ES中类型，该方法用于普遍情况下的字段处理（例如上百字段的病人数据），
     * 有特殊字段的需要在该方法后覆盖相应properties
     *
     * @param entityClass
     * @return
     */
    public static TypeMapping.Builder getTypeMapping(Class<?> entityClass) {
        Entity entity = Entity.getEntity(entityClass);
        TypeMapping.Builder tmBuilder = new TypeMapping.Builder();

        for (EntityField field : entity.getEntityFields()) {
            Class<?> type = field.getType();
            if (field.isArray()) {
                type = field.getCollectionType();
            } else if (field.isCollection()) {
                type = field.getArrayType();
            }

            if (type == String.class) {
                tmBuilder.properties(field.getName(), new KeywordProperty.Builder().build()._toProperty());
            } else if (type == Integer.class) {
                tmBuilder.properties(field.getName(), new IntegerNumberProperty.Builder().build()._toProperty());
            } else if (type == Long.class) {
                tmBuilder.properties(field.getName(), new LongNumberProperty.Builder().build()._toProperty());
            } else if (type == Date.class) {
                tmBuilder.properties(field.getName(), new DateProperty.Builder().format("epoch_millis").build()._toProperty());
            } else {
                tmBuilder.properties(field.getName(), new KeywordProperty.Builder().build()._toProperty());
            }
        }

        return tmBuilder;
    }

    /**
     * 获取模糊查询通配值
     *
     * @param str
     * @return
     */
    public static String getLikeValue(String str) {
        return "*" + str + "*";
    }

    /**
     * 获取模糊查询（左模糊）通配值
     *
     * @param str
     * @return
     */
    public static String getLikeLeftValue(String str) {
        return "*" + str;
    }

    /**
     * 获取模糊查询（右模糊）通配值
     *
     * @param str
     * @return
     */
    public static String getLikeRightValue(String str) {
        return str + "*";
    }
}
