package com.luna.framework.service.mybatis;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.luna.framework.service.annotation.SimpleViewField;
import com.luna.framework.utils.StringUtil;
import com.luna.framework.utils.reflect.Entity;
import com.luna.framework.utils.reflect.EntityField;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.List;

/**
 * 查询简单字段数据对象列表的方法实现
 *
 * @author TontoZhou
 * @since 2021/4/13
 */
public class SelectSimpleList extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlMethod sqlMethod = SqlMethod.SELECT_LIST;
        String sql = String.format(sqlMethod.getSql(), sqlFirst(), sqlSelectSimpleColumns(tableInfo), tableInfo.getTableName(),
                sqlWhereEntityWrapper(true, tableInfo), sqlOrderBy(tableInfo), sqlComment());
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addSelectMappedStatementForTable(mapperClass, CommonMapper.METHOD_SELECT_SIMPLE_LIST, sqlSource, tableInfo);
    }

    /**
     * SQL 查询所有表字段
     *
     * @param tableInfo 表信息
     * @return sql 脚本
     */
    protected String sqlSelectSimpleColumns(TableInfo tableInfo) {
        Entity entity = Entity.getEntity(tableInfo.getEntityType());
        StringBuilder sb = new StringBuilder();
        List<TableFieldInfo> tableFields = tableInfo.getFieldList();
        for (EntityField entityField : entity.getEntityFields()) {
            if (entityField.getAnnotation(SimpleViewField.class) != null) {
                String column = getColumnName(entityField, tableFields, tableInfo);
                if (column != null && column.length() >0) {
                    sb.append(column).append(COMMA);
                }
            }
        }

        if (sb.length() > 0) {
            String selectColumns = sb.deleteCharAt(sb.length() - 1).toString();
            return convertChooseEwSelect(selectColumns);
        } else {
            return sqlSelectColumns(tableInfo, true);
        }
    }

    private String getColumnName(EntityField entityField, List<TableFieldInfo> tableFields, TableInfo tableInfo) {
        String property = entityField.getName();
        if (StringUtil.equals(tableInfo.getKeyProperty(), property)) {
            return tableInfo.getKeySqlSelect();
        }
        for (TableFieldInfo tableField : tableFields) {
            if (StringUtil.equals(tableField.getProperty(), property)) {
                return tableField.getSqlSelect();
            }
        }
        return null;
    }

}