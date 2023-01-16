package com.luna.framework.utils.others;

import com.luna.framework.utils.reflect.Entity;
import com.luna.framework.utils.reflect.NameUtil;
import com.luna.framework.utils.reflect.EntityField;

public class SqlUtil {

    public static String generateBatchInsertSql(Class<?> modelClass) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO `").append(modelClass.getSimpleName()).append("` (");
        Entity entity = Entity.getEntity(modelClass);
        for (EntityField field : entity.getEntityFields()) {
            sb.append("`").append(NameUtil.hump2underline(field.getName())).append("`,");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(") VALUES <foreach collection=\"list\" item=\"item\" separator=\",\">(");
        for (EntityField field : entity.getEntityFields()) {
            sb.append("#{item.").append(field.getName()).append("},");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")</foreach>");
        return sb.toString();
    }

    public static String generateSelectSql(Class<?> modelClass, String prefix) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        Entity entity = Entity.getEntity(modelClass);
        for (EntityField field : entity.getEntityFields()) {
            String fieldName = field.getName();
            String columnName = NameUtil.hump2underline(fieldName);
            if (prefix != null) {
                sb.append(prefix).append('.');
            }
            if (fieldName.equals(columnName)) {
                sb.append(columnName).append(",");
            } else {
                sb.append(columnName).append(" ").append(fieldName).append(",");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" FROM ").append(NameUtil.hump2underline(modelClass.getSimpleName())).append(" ").append(prefix == null ? "" : prefix);
        return sb.toString();
    }
}
