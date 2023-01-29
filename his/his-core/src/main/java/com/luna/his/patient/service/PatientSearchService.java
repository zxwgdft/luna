package com.luna.his.patient.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.luna.framework.api.PageResult;
import com.luna.framework.exception.BusinessException;
import com.luna.framework.security.WebSecurityManager;
import com.luna.framework.utils.StringUtil;
import com.luna.framework.utils.convert.SimpleBeanCopyUtil;
import com.luna.framework.utils.reflect.LambdaUtil;
import com.luna.his.core.HisUserSession;
import com.luna.his.core.es.ESConstants;
import com.luna.his.core.es.ESQueryHelper;
import com.luna.his.core.es.ESUtil;
import com.luna.his.patient.service.dto.PatientFullDTO;
import com.luna.his.patient.service.dto.PatientQuery;
import com.luna.his.patient.service.entity.EsPatient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author TontoZhou
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PatientSearchService {

    private final ElasticsearchClient elasticsearchClient;

    /**
     * 搜索患者数据（分页）
     *
     * @param queryParam 搜索参数
     * @return 分页结果
     */
    public PageResult<EsPatient> searchPatient(PatientQuery queryParam) {
        HisUserSession userSession = (HisUserSession) WebSecurityManager.getCurrentUserSession();
        // 必须要过滤租户
        queryParam.setTenantId(userSession.getTenantId());
        // 通过帮助类构建常规查询对象
        BoolQuery.Builder boolQuery = ESQueryHelper.buildQuery(queryParam);

        if (queryParam.getHasWeixin() != null && queryParam.getHasWeixin()) {
            boolQuery.must(q -> q.exists(i -> i.field(LambdaUtil.getFieldName(EsPatient::getWeixin))));
        }
        // 是否查询了姓名
        boolean hasQueryName = false;
        String keyword = queryParam.getKeyword();
        if (keyword != null && (keyword = keyword.trim()).length() > 0) {
            final String keyWildcard = ESUtil.getLikeValue(keyword);
            if (StringUtil.isContainsChinese(keyword)) {
                // 有汉字则一定不是查询拼音、电话、档案号
                boolQuery.must(q -> q.wildcard(i -> i.field(LambdaUtil.getFieldName(EsPatient::getName)).value(keyWildcard)));
            } else {
                BoolQuery.Builder keyQuery = new BoolQuery.Builder();
                keyQuery.should(q -> q.wildcard(i -> i.field(LambdaUtil.getFieldName(EsPatient::getName)).value(keyWildcard)));
                // 这里的拼音为首字母，如果需要可增加全拼
                keyQuery.should(q -> q.wildcard(i -> i.field(LambdaUtil.getFieldName(EsPatient::getNamePy)).value(keyWildcard)));
                if (keyword.length() > 3) {
                    //只有超过3个字符的情况下才去模糊匹配手机号码或档案号（可根据实际业务情况优化这里）
                    keyQuery.should(q -> q.wildcard(i -> i.field(LambdaUtil.getFieldName(EsPatient::getMobiles)).value(keyWildcard)));
                }
                boolQuery.must(keyQuery.build()._toQuery());
            }

            hasQueryName = true;
        }

        String name = queryParam.getName();
        if (!hasQueryName && name != null && (name = name.trim()).length() > 0) {
            String finalName = name;
            boolQuery.must(q -> q.wildcard(i -> i.field(LambdaUtil.getFieldName(EsPatient::getName)).value(ESUtil.getLikeValue(finalName))));
        }

        // TODO 自由项搜索待做

        SearchRequest request = SearchRequest.of(r ->
                r.index(ESConstants.INDEX_PATIENT)
                        .query(q -> q
                                .bool(boolQuery.build())
                        )
                        .from(queryParam.getOffset())
                        .size(queryParam.getLimit())
        );

        if (log.isDebugEnabled()) {
            log.debug("ElasticSearch查询语句：{}", request.toString());
        }

        try {
            SearchResponse<EsPatient> response = elasticsearchClient.search(request, EsPatient.class);
            return ESQueryHelper.getPageResult(response.hits(), queryParam);
        } catch (IOException e) {
            log.error("查询患者数据异常", e);
            throw new BusinessException("查询患者数据异常");
        }
    }

    /**
     * 存储患者
     */
    public void savePatient(PatientFullDTO patientFull) {
        EsPatient patient = new EsPatient();
        SimpleBeanCopyUtil.simpleCopy(patientFull, patient);

        List<String> mobiles = new ArrayList<>(3);
        List<String> mobilesRes = new ArrayList<>(3);

        if (StringUtil.isNotEmpty(patientFull.getMobile1()) && StringUtil.isNotEmpty(patientFull.getMobile1re())) {
            mobiles.add(patientFull.getMobile1());
            mobilesRes.add(patientFull.getMobile1re());
        }

        if (StringUtil.isNotEmpty(patientFull.getMobile2()) && StringUtil.isNotEmpty(patientFull.getMobile2re())) {
            mobiles.add(patientFull.getMobile2());
            mobilesRes.add(patientFull.getMobile2re());
        }

        if (StringUtil.isNotEmpty(patientFull.getMobile3()) && StringUtil.isNotEmpty(patientFull.getMobile3re())) {
            mobiles.add(patientFull.getMobile3());
            mobilesRes.add(patientFull.getMobile3re());
        }

        List<String> phones = new ArrayList<>(2);
        List<String> phoneRes = new ArrayList<>(2);

        if (StringUtil.isNotEmpty(patientFull.getPhone1()) && StringUtil.isNotEmpty(patientFull.getPhone1re())) {
            phones.add(patientFull.getPhone1());
            phoneRes.add(patientFull.getPhone1re());
        }

        if (StringUtil.isNotEmpty(patientFull.getPhone2()) && StringUtil.isNotEmpty(patientFull.getPhone2re())) {
            phones.add(patientFull.getPhone2());
            phoneRes.add(patientFull.getPhone2re());
        }

        patient.setMobiles(mobiles);
        patient.setMobilesRes(mobilesRes);
        patient.setPhones(phones);
        patient.setPhoneRes(phoneRes);

        if (log.isDebugEnabled()) {
            log.debug("ElasticSearch新增患者：{}", patient);
        }

        try {
            IndexResponse response = elasticsearchClient.index(i ->
                    i.index(ESConstants.INDEX_PATIENT)
                            .id(patient.getId().toString())
                            .document(patient)
            );
        } catch (IOException e) {
            log.error("存储患者[id={}]数据到ES异常", patientFull.getId(), e);
            throw new BusinessException("存储患者数据到ES异常");
        }
    }

}
