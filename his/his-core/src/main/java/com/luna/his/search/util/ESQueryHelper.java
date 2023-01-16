package com.luna.his.search.util;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.WildcardQuery;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.json.JsonData;
import com.luna.framework.api.PageParam;
import com.luna.framework.api.PageResult;
import com.luna.framework.api.SystemException;
import com.luna.framework.utils.TimeUtil;
import com.luna.framework.utils.reflect.Entity;
import com.luna.framework.utils.reflect.EntityField;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 通过反射动态生成较简单通用的ES查询
 * </p>
 *
 * @author TontZhou
 */
@Slf4j
public class ESQueryHelper {

    private static Map<String, Builder> buildCache = new ConcurrentHashMap<>();


    /**
     * 转为我们定义的PageResult，如果需要更多搜索返回的信息（例如高亮等则不应该用该方法）
     *
     * @param result
     * @param pageParam
     * @param <T>
     * @return
     */
    public static <T> PageResult<T> getPageResult(HitsMetadata<T> result, PageParam pageParam) {
        List<Hit<T>> hits = result.hits();
        if (hits == null || hits.size() == 0) {
            return new PageResult<T>(pageParam.getPage(), pageParam.getLimit(), result.total().value(), Collections.EMPTY_LIST);
        }
        List<T> data = new ArrayList<>(hits.size());
        for (Hit<T> item : hits) {
            data.add(item.source());
        }
        return new PageResult<T>(pageParam.getPage(), pageParam.getLimit(), result.total().value(), data);
    }


    /**
     * 根据注解构建查询条件
     *
     * @param queryParam
     * @return
     */
    public static BoolQuery.Builder buildQuery(Object queryParam) {
        return buildQuery(new BoolQuery.Builder(), queryParam);
    }

    /**
     * 根据注解构建查询条件（当前criteria，而不是新增，如需要应该在调用前手动调用and()、or()）
     *
     * @param queryWrapper
     * @param queryParam   查询条件参数
     * @return
     */
    public static BoolQuery.Builder buildQuery(BoolQuery.Builder queryWrapper, Object queryParam) {
        if (queryParam == null) {
            return queryWrapper;
        }
        Class<?> clazz = queryParam.getClass();
        if (clazz.isArray()) {
            int length = Array.getLength(queryParam);
            for (int i = 0; i < length; i++) {
                Object param = Array.get(queryParam, i);
                getBuilder(param.getClass()).build(queryWrapper, param);
            }
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection coll = (Collection) queryParam;
            for (Object param : coll) {
                getBuilder(param.getClass()).build(queryWrapper, param);
            }
        } else {
            getBuilder(queryParam.getClass()).build(queryWrapper, queryParam);
        }

        return queryWrapper;
    }

    /**
     * 获取查询条件构建起
     */
    private static Builder getBuilder(Class<?> queryParamClass) {
        String key = queryParamClass.getName();
        Builder builder = buildCache.get(key);
        if (builder == null) {
            // 同步创建Builder
            synchronized (buildCache) {
                builder = buildCache.get(key);
                if (builder == null) {
                    builder = new Builder(queryParamClass);
                    buildCache.put(key, builder);
                }
            }
        }
        return builder;
    }

    /**
     * 根据QueryWrapper动态SQL规则转化查询条件
     */
    private static void buildCriteria(BoolQuery.Builder queryBuilder, String property, ESQueryType type, Object val) throws IOException {
        if (val == null) {
            return;
        }

        if (val instanceof String) {
            String valStr = ((String) val).trim();
            if (valStr.length() == 0) {
                return;
            }
            val = valStr;
        }

        final Object value = val;

        if (type == ESQueryType.EQUAL) {
            queryBuilder.must(q -> q.match(getMatchQuery(property, value)));
        } else if (type == ESQueryType.NOT_EQUAL) {
            queryBuilder.mustNot(q -> q.match(getMatchQuery(property, value)));
        } else if (type == ESQueryType.GREAT_THAN) {
            queryBuilder.must(q -> q.range(RangeQuery.of(i -> i.field(property).gt(JsonData.of(value)))));
        } else if (type == ESQueryType.GREAT_EQUAL) {
            queryBuilder.must(q -> q.range(RangeQuery.of(i -> i.field(property).gte(JsonData.of(value)))));
        } else if (type == ESQueryType.LESS_THAN) {
            queryBuilder.must(q -> q.range(RangeQuery.of(i -> i.field(property).lt(JsonData.of(value)))));
        } else if (type == ESQueryType.LESS_EQUAL) {
            queryBuilder.must(q -> q.range(RangeQuery.of(i -> i.field(property).lte(JsonData.of(value)))));
        } else if (type == ESQueryType.LIKE) {
            queryBuilder.must(q -> q.wildcard(WildcardQuery.of(i -> i.field(property).value(ESUtil.getLikeValue(value.toString())))));
        } else if (type == ESQueryType.LIKE_LEFT) {
            queryBuilder.must(q -> q.wildcard(WildcardQuery.of(i -> i.field(property).value(ESUtil.getLikeLeftValue(value.toString())))));
        } else if (type == ESQueryType.LIKE_RIGHT) {
            queryBuilder.must(q -> q.wildcard(WildcardQuery.of(i -> i.field(property).value(ESUtil.getLikeRightValue(value.toString())))));
        } else if (type == ESQueryType.BETWEEN) {
            if (value instanceof DateRange) {
                DateRange range = (DateRange) value;
                if (range.isDate()) {
                    // 日期的话自动处理边界
                    Date start = TimeUtil.toDate(range.getStart().getTime());
                    Date end = TimeUtil.getDateBefore(range.getEnd().getTime(), -1);
                    queryBuilder.must(q -> q.range(RangeQuery.of(i -> i.field(property).gte(JsonData.of(start.getTime())).lt(JsonData.of(end.getTime()))

                    )));
                } else {
                    queryBuilder.must(q -> q.range(RangeQuery.of(i -> i.field(property).gte(JsonData.of(range.getStart().getTime())).lte(JsonData.of(range.getEnd().getTime())))));
                }
            } else if (value instanceof NumberRange) {
                NumberRange range = (NumberRange) value;
                queryBuilder.must(q -> q.range(RangeQuery.of(i -> {
                    i.field(property);
                    if (range.isIncludeLower()) {
                        i.gte(JsonData.of(range.getStart()));
                    } else {
                        i.gt(JsonData.of(range.getStart()));
                    }

                    if (range.isIncludeUpper()) {
                        i.lte(JsonData.of(range.getEnd()));
                    } else {
                        i.lt(JsonData.of(range.getEnd()));
                    }

                    return i;
                })));
            } else {
                log.error("尚不支持类：{}的解析", value.getClass());
                throw new SystemException(SystemException.CODE_ERROR_CODE);
            }
        }
    }

    private static MatchQuery getMatchQuery(String property, Object value) {
        if (value instanceof Date) {
            return MatchQuery.of(i -> i.field(property).query(((Date) value).getTime()));
        } else {
            return MatchQuery.of(i -> i.field(property).query(value.toString()));
        }
    }

//    private static Pattern columnNamePattern = Pattern.compile("^\\w+$");

    /**
     * 查询对象构建器
     * <ul>
     * 规则
     * <li>基于{@link ESQuery}构建简单查询条件（AND）</li>
     * <li></li>
     * <li></li>
     * </ul>
     *
     * @author TontoZhou
     * @since 2018年3月15日
     */
    private static class Builder {

        private ArrayList<BuildUnit> buildUnits;

        private Builder(Class<?> queryClass) {
            this.buildUnits = new ArrayList<>();
            for (EntityField entityField : Entity.getEntity(queryClass).getEntityFields()) {
                ESQuery condition = entityField.getAnnotation(ESQuery.class);
                if (condition != null) {
                    String name = condition.name();
                    // 默认使用方法对应的field名作为column
                    if (name.length() == 0) {
                        name = entityField.getName();
                    }
                    BuildUnit unit = new BuildUnit(name, entityField.getGetMethod(), condition);
                    buildUnits.add(unit);
                }
            }
        }

        private BoolQuery.Builder build(BoolQuery.Builder queryBuilder, Object queryParam) {
//            if (queryParam instanceof SortParam) {
//                SortParam sortParam = (SortParam) queryParam;
//                String sort = sortParam.getSort();
//                if (sort != null && sort.length() > 0) {
//                    sort = NameUtil.hump2underline(sort);
//                    if (!columnNamePattern.matcher(sort).matches()) {
//                        throw new SystemException(SystemException.CODE_ERROR_DATA, "非法的排序字段：" + sort);
//                    }
//                }
//            }

            for (BuildUnit bu : buildUnits) {
                ESQueryType type = bu.type;
                String property = bu.name;
                try {
                    Object value = bu.getMethod.invoke(queryParam);
                    if (bu.nextDay && value instanceof Date) {
                        Date date = (Date) value;
                        value = TimeUtil.getDateBefore(date.getTime(), -1);
                    }
                    buildCriteria(queryBuilder, property, type, value);
                } catch (Exception e) {
                    log.error("build QueryWrapper error!", e);
                    continue;
                }

            }
            return queryBuilder;
        }
    }

    /**
     * 构建单元
     *
     * @author TontoZhou
     * @since 2018年3月15日
     */
    private static class BuildUnit {

        private ESQueryType type;
        private String name;
        private Method getMethod;
        private boolean nextDay;

        private BuildUnit(String name, Method getMethod, ESQuery condition) {
            this.name = name;
            this.type = condition.type();
            this.getMethod = getMethod;
            this.nextDay = condition.nextDay();
        }
    }


}
