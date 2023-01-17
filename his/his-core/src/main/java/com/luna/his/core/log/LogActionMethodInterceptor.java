package com.luna.his.core.log;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.luna.framework.security.WebSecurityManager;
import com.luna.framework.utils.convert.JsonUtil;
import com.luna.his.core.HisUserSession;
import com.luna.his.patient.model.Patient;
import com.luna.his.patient.service.PatientService;
import com.luna.his.core.service.entity.LogOperate;
import com.luna.his.core.service.LogSearchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 操作日志AOP
 * <p>
 * 注意：如果注解与service方法，应防止嵌套的操作日志情况出现，如果需要嵌套则需要改写
 *
 * @author TontoZhou
 * @since 2020/3/19
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogActionMethodInterceptor {
    private final LogSearchService logSearchService;
    private final ExecutorService executorService;
    private final DataUpdateConverter dataUpdateConverter;

    private final PatientService patientService;

    @Around("@annotation(actionLog)")
    public void afterAction(ProceedingJoinPoint proceedingJoinPoint, ActionLog actionLog) throws Throwable {
        // 开始操作
        OperateLogHelper.beginOperate();
        try {
            // 执行业务方法
            proceedingJoinPoint.proceed();
            try {
                Long patientId = OperateLogHelper.getPatient();
                final LogOperate logData = new LogOperate();
                HisUserSession hisUserSession = (HisUserSession) WebSecurityManager.getCurrentUserSession();
                logData.setUserId(hisUserSession.getUserId());
                logData.setUserName(hisUserSession.getUserName());
                logData.setUserNo(hisUserSession.getUserNo());
                logData.setPlatform(LogConstants.PLATFORM_SAAS);
                logData.setHospitalId(hisUserSession.getHospitalId());
                logData.setHospitalName(hisUserSession.getHospitalName());
                logData.setOperateTime(new Date());
                Patient patient = patientService.get(patientId);
                logData.setPatientName(Objects.isNull(patient) ? "未知" : patient.getName());
                logData.setModule(actionLog.module().name());
                logData.setAction(actionLog.action());
                logData.setActionType(actionLog.actionType().name());
                logData.setTenantId(hisUserSession.getTenantId());

                final List<DataUpdate> dataUpdates = OperateLogHelper.getDataUpdated();
                // 异步处理
                executorService.execute(() -> {
                    asyncHandleLog(logData, dataUpdates);
                });
            } catch (Exception e) {
                log.error("处理操作日志异常", e);
            }
        } finally {
            // 结束操作，必须结束，否则可能带来线程变量数据间错乱
            OperateLogHelper.endOperate();
        }
    }

    protected void asyncHandleLog(LogOperate logData, List<DataUpdate> dataUpdates) {
        // TODO 异步执行，如果并发高且写入数据库时间长则可改为队列处理
        try {
            List<FieldUpdate> updates = null;
            for (DataUpdate dataUpdate : dataUpdates) {
                List<FieldUpdate> result = dataUpdateConverter.getFieldUpdate(
                        dataUpdate.getOrigin(),
                        dataUpdate.getCurrent(),
                        dataUpdate.getUpdates()
                );
                if (updates == null) {
                    updates = result;
                } else if (result != null && result.size() > 0) {
                    updates.addAll(result);
                }
            }
            if (updates != null && updates.size() > 0) {
                logData.setContent(JsonUtil.getJson(updates));
            }
            logSearchService.saveLogOperate(logData);
        } catch (IOException e) {
            log.error("不应该发生的异常，请检查代码", e);
        }
    }
}
