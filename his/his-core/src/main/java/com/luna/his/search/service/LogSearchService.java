package com.luna.his.search.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.mapping.FieldType;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.luna.framework.api.PageResult;
import com.luna.framework.exception.BusinessException;
import com.luna.framework.security.WebSecurityManager;
import com.luna.framework.utils.TimeUtil;
import com.luna.framework.utils.reflect.LambdaUtil;
import com.luna.his.core.HisUserSession;
import com.luna.his.search.entity.LogLogin;
import com.luna.his.search.entity.LogOperate;
import com.luna.his.search.service.query.LogLoginQuery;
import com.luna.his.search.service.query.LogOperateQuery;
import com.luna.his.search.util.DateRange;
import com.luna.his.search.util.ESConstants;
import com.luna.his.search.util.ESQueryHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

/**
 * 日志搜索服务
 * <p>
 * 由于日志数据会很多，可通过分租户建立索引方式分流数据
 *
 * @author TontoZhou
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LogSearchService {

    private final ElasticsearchClient elasticsearchClient;

    public PageResult<LogLogin> searchLoginLog(LogLoginQuery queryParam) {
        // 日期跨度不能超过一周
        DateRange dateRange = queryParam.getOperateTime();
        boolean allowed = false;
        if (dateRange != null) {
            Date start = dateRange.getStart();
            Date end = dateRange.getEnd();
            if (start != null && end != null) {
                if (end.getTime() - start.getTime() <= 7 * TimeUtil.MILLIS_IN_DAY) {
                    allowed = true;
                }
            }
        }

        if (!allowed) {
            throw new BusinessException("查询日期间隔不能大于7天");
        }

        HisUserSession userSession = (HisUserSession) WebSecurityManager.getCurrentUserSession();
        // 必须要过滤租户
        queryParam.setTenantId(userSession.getTenantId());
        // 通过帮助类构建常规查询对象
        BoolQuery.Builder boolQuery = ESQueryHelper.buildQuery(queryParam);

        SortOptions sort = new SortOptions.Builder().field(
                builder -> builder.field(LambdaUtil.getFieldName(LogLogin::getOperateTime)).order(SortOrder.Desc)).build();

        SearchRequest request = SearchRequest.of(r -> r.index(ESConstants.INDEX_LOG_LOGIN).query(q -> q.bool(boolQuery.build())).from(queryParam.getOffset()).size(queryParam.getLimit()));

        if (log.isDebugEnabled()) {
            log.debug("ElasticSearch查询语句：{}", request.toString());
        }

        try {
            SearchResponse<LogLogin> response = elasticsearchClient.search(request, LogLogin.class);
            return ESQueryHelper.getPageResult(response.hits(), queryParam);
        } catch (IOException e) {
            log.error("查询登录日志数据异常", e);
            throw new BusinessException("查询登录日志数据异常");
        }
    }

    public PageResult<LogOperate> searchOperateLog(LogOperateQuery queryParam) {
        // 日期跨度不能超过一周
        DateRange dateRange = queryParam.getOperateTime();
        boolean allowed = false;
        if (dateRange != null) {
            Date start = dateRange.getStart();
            Date end = dateRange.getEnd();
            if (start != null && end != null) {
                if (end.getTime() - start.getTime() <= 7 * TimeUtil.MILLIS_IN_DAY) {
                    allowed = true;
                }
            }
        }

        if (!allowed) {
            throw new BusinessException("查询日期间隔不能大于7天");
        }

        HisUserSession userSession = (HisUserSession) WebSecurityManager.getCurrentUserSession();
        // 必须要过滤租户
        queryParam.setTenantId(userSession.getTenantId());
        // 通过帮助类构建常规查询对象
        BoolQuery.Builder boolQuery = ESQueryHelper.buildQuery(queryParam);

        SortOptions sort = new SortOptions.Builder()
                .field(builder -> builder.field(LambdaUtil.getFieldName(LogOperate::getOperateTime)).unmappedType(FieldType.Keyword).order(SortOrder.Desc)).build();

        SearchRequest request =
                SearchRequest.of(r -> r.index(ESConstants.INDEX_LOG_OPERATE).query(q -> q.bool(boolQuery.build())).from(queryParam.getOffset()).size(queryParam.getLimit()));

        if (log.isDebugEnabled()) {
            log.debug("ElasticSearch查询语句：{}", request.toString());
        }

        try {
            SearchResponse<LogOperate> response = elasticsearchClient.search(request, LogOperate.class);
            return ESQueryHelper.getPageResult(response.hits(), queryParam);
        } catch (IOException e) {
            log.error("查询登录日志数据异常", e);
            throw new BusinessException("查询登录日志数据异常");
        }
    }

    public void saveLoginLog(LogLogin logData) {
        // TODO 是否需要分租户或分时间处理
        try {
            elasticsearchClient.index(i -> i.index(ESConstants.INDEX_LOG_LOGIN).document(logData));
        } catch (Exception e) {
            log.error("存储登录日志[data={}]数据到ES异常", logData, e);
        }
    }

    public void saveLogOperate(LogOperate logData) {
        // TODO 是否需要分租户或分时间处理
        try {
            elasticsearchClient.index(i -> i.index(ESConstants.INDEX_LOG_OPERATE).document(logData));
        } catch (Exception e) {
            log.error("存储操作日志[data={}]数据到ES异常", logData, e);
        }
    }


}
