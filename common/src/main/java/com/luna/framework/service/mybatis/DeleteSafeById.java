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
package com.luna.framework.service.mybatis;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * 默认与deleteById一样
 *
 * @author TontoZhou
 * @since 2018-04-06
 */
public class DeleteSafeById extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql;
        if (tableInfo.isWithLogicDelete()) {
            List<TableFieldInfo> fieldInfos = tableInfo.getFieldList().stream()
                    .filter(TableFieldInfo::isWithUpdateFill)
                    .collect(toList());
            if (CollectionUtils.isNotEmpty(fieldInfos)) {
                String sqlSet = "SET " + SqlScriptUtils.convertIf(fieldInfos.stream()
                        .map(i -> i.getSqlSet(EMPTY)).collect(joining(EMPTY)), "!@org.apache.ibatis.type.SimpleTypeRegistry@isSimpleType(_parameter.getClass())", true)
                        + tableInfo.getLogicDeleteSql(false, false);
                sql = String.format(SqlMethod.LOGIC_DELETE_BY_ID.getSql(), tableInfo.getTableName(), sqlSet, tableInfo.getKeyColumn(),
                        CommonMapper.PARAM_ID, tableInfo.getLogicDeleteSql(true, true));
            } else {
                sql = String.format(SqlMethod.LOGIC_DELETE_BY_ID.getSql(), tableInfo.getTableName(), sqlLogicSet(tableInfo),
                        tableInfo.getKeyColumn(), CommonMapper.PARAM_ID,
                        tableInfo.getLogicDeleteSql(true, true));
            }
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, Object.class);
            return addUpdateMappedStatement(mapperClass, modelClass, CommonMapper.METHOD_DELETE_SAFE_BY_ID, sqlSource);
        } else {
            sql = String.format(SqlMethod.DELETE_BY_ID.getSql(), tableInfo.getTableName(), tableInfo.getKeyColumn(),
                    CommonMapper.PARAM_ID);
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, Object.class);
            return this.addDeleteMappedStatement(mapperClass, CommonMapper.METHOD_DELETE_SAFE_BY_ID, sqlSource);
        }
    }
}
