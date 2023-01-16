package com.luna.his.search.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.mapping.TextProperty;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch._types.mapping.WildcardProperty;
import com.luna.framework.utils.reflect.LambdaUtil;
import com.luna.his.search.entity.EsPatient;
import com.luna.his.search.entity.LogLogin;
import com.luna.his.search.entity.LogOperate;
import com.luna.his.search.util.ESConstants;
import com.luna.his.search.util.ESUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @author TontoZhou
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SearchInitializeService {

    private final ElasticsearchClient elasticsearchClient;

    /**
     * 初始化搜索服务
     */
    @PostConstruct
    public void init() {
        log.info("----开始初始化搜索相关配置----");
        // 创建索引
        createPatientIndex();
        /**
         * 日志索引可以不主动创建，从而通过自动创建更方便的实现分租户存储，
         * 但是索引中的字符串默认类型是text+keyword，需要在ES处理或者在
         * 搜索时通过 field.keyword 去关键字查询
         */
        createLoginLogIndex();
        createOperateLogIndex();
    }

    /**
     * 创建患者索引（如果不存在）
     */
    private void createPatientIndex() {
        try {
            elasticsearchClient.indices().get(r -> r.index(ESConstants.INDEX_PATIENT));
            return;
        } catch (ElasticsearchException e) {
            // 应该是查询不到索引的异常，这里不处理
        } catch (IOException ie) {
            log.error("查询索引[index={}]异常", ESConstants.INDEX_PATIENT, ie);
        }

        try {
            elasticsearchClient.indices().create(
                    builder -> {
                        TypeMapping.Builder tmBuilder = ESUtil.getTypeMapping(EsPatient.class);
                        // 使用Wildcard Field字段，用于不利于分词的模糊查询
                        tmBuilder.properties(LambdaUtil.getFieldName(EsPatient::getName), new WildcardProperty.Builder().build()._toProperty());
                        tmBuilder.properties(LambdaUtil.getFieldName(EsPatient::getNamePy), new WildcardProperty.Builder().build()._toProperty());
                        tmBuilder.properties(LambdaUtil.getFieldName(EsPatient::getMobiles), new WildcardProperty.Builder().build()._toProperty());

                        // TODO 以下字段应该用Text Field更合适，后期应该加入中文分词并改写这里（查询条件那里也需要修改）
                        tmBuilder.properties(LambdaUtil.getFieldName(EsPatient::getAddress), new WildcardProperty.Builder().build()._toProperty());

                        builder.index(ESConstants.INDEX_PATIENT)
                                .mappings(tmBuilder.build());

                        return builder;
                    }
            );
            log.info("成功创建索引index={}", ESConstants.INDEX_PATIENT);
        } catch (Exception e) {
            // 可能是索引已经存在的异常
            log.error("创建索引[index={}]异常，如果索引已经存在则可忽略该错误", ESConstants.INDEX_PATIENT, e);
        }
    }

    /**
     * 创建用户登录日志索引（如果不存在）
     */
    private void createLoginLogIndex() {
        try {
            elasticsearchClient.indices().get(r -> r.index(ESConstants.INDEX_LOG_LOGIN));
            return;
        } catch (ElasticsearchException e) {
            // 应该是查询不到索引的异常，这里不处理
        } catch (IOException ie) {
            log.error("查询索引[index={}]异常", ESConstants.INDEX_LOG_LOGIN, ie);
        }

        try {
            elasticsearchClient.indices().create(
                    builder -> {
                        TypeMapping.Builder tmBuilder = ESUtil.getTypeMapping(LogLogin.class);
                        // 特殊字段请在下面进行手动设置
                        // tmBuilder.properties(LambdaUtil.getFieldName(LogLogin::getUserName), new WildcardProperty.Builder().build()._toProperty());

                        builder.index(ESConstants.INDEX_LOG_LOGIN)
                                .mappings(tmBuilder.build());
                        return builder;
                    }
            );
            log.info("成功创建索引index={}", ESConstants.INDEX_LOG_LOGIN);
        } catch (Exception e) {
            // 可能是索引已经存在的异常
            log.error("创建索引[index={}]异常，如果索引已经存在则可忽略该错误", ESConstants.INDEX_LOG_LOGIN, e);
        }
    }

    /**
     * 创建用户登录日志索引（如果不存在）
     */
    private void createOperateLogIndex() {
        try {
            elasticsearchClient.indices().get(r -> r.index(ESConstants.INDEX_LOG_OPERATE));
            return;
        } catch (ElasticsearchException e) {
            // 应该是查询不到索引的异常，这里不处理
        } catch (IOException ie) {
            log.error("查询索引[index={}]异常", ESConstants.INDEX_LOG_OPERATE, ie);
        }

        try {
            elasticsearchClient.indices().create(
                    builder -> {
                        TypeMapping.Builder tmBuilder = ESUtil.getTypeMapping(LogOperate.class);
                        // 特殊字段请在下面进行手动设置
                        tmBuilder.properties(LambdaUtil.getFieldName(LogOperate::getContent),
                                new TextProperty.Builder().index(false).build()._toProperty());

                        builder.index(ESConstants.INDEX_LOG_OPERATE)
                                .mappings(tmBuilder.build());
                        return builder;
                    }
            );
            log.info("成功创建索引index={}", ESConstants.INDEX_LOG_LOGIN);
        } catch (Exception e) {
            // 可能是索引已经存在的异常
            log.error("创建索引[index={}]异常，如果索引已经存在则可忽略该错误", ESConstants.INDEX_LOG_LOGIN, e);
        }
    }
}
