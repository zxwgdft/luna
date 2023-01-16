/*
 * Copyright (c) 2011-2021, baomidou (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.luna.his.core.framework;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.luna.framework.service.mybatis.CommonMapper;
import com.luna.his.core.BaseModel;
import com.luna.his.core.HisConstants;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 根据 ID 更新对象中不为NULL的字段，如果是基于BaseModel则更新时需要判断租户
 *
 * @auther TontoZhou
 * @since 2018-04-06
 */
public class UpdateSelectionById extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql;
        if (BaseModel.class.isAssignableFrom(tableInfo.getEntityType())) {
            String formatSql = "<script>\nUPDATE %s %s WHERE %s=#{%s} AND %s=#{%s} %s\n</script>";
            final String additional = optlockVersion(tableInfo) + tableInfo.getLogicDeleteSql(true, true);
            sql = String.format(formatSql, tableInfo.getTableName(),
                    sqlSet(tableInfo.isWithLogicDelete(), false, tableInfo, false, ENTITY, ENTITY_DOT),
                    tableInfo.getKeyColumn(), ENTITY_DOT + tableInfo.getKeyProperty(),
                    HisConstants.COLUMN_TENANT_ID, ENTITY_DOT + HisConstants.PROPERTY_TENANT_ID,
                    additional);
        } else {
            String formatSql = "<script>\nUPDATE %s %s WHERE %s=#{%s} %s\n</script>";
            final String additional = optlockVersion(tableInfo) + tableInfo.getLogicDeleteSql(true, true);
            sql = String.format(formatSql, tableInfo.getTableName(),
                    sqlSet(tableInfo.isWithLogicDelete(), false, tableInfo, false, ENTITY, ENTITY_DOT),
                    tableInfo.getKeyColumn(), ENTITY_DOT + tableInfo.getKeyProperty(), additional);

        }

        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return addUpdateMappedStatement(mapperClass, modelClass, CommonMapper.METHOD_UPDATE_SELECTION_BY_ID, sqlSource);
    }

}
