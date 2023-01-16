package com.luna.his.core.log;

import java.util.ArrayList;
import java.util.List;

import com.luna.framework.api.SystemException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author TontoZhou
 */
@Slf4j
public class OperateLogHelper {

    // 存储操作带来的影响（例如新增的对象，修改的字段等，以String形式存储）
    private final static ThreadLocal<List<DataUpdate>> logOperateStore = new ThreadLocal<>();
    // 存储操作的患者
    private final static ThreadLocal<Long> patientStore = new ThreadLocal<>();

    /**
     * 开始操作，新增一个操作日志对象
     */
    public static void beginOperate() {
        logOperateStore.set(new ArrayList<>());
        patientStore.set(0L);
    }

    /**
     * 获取当前操作患者
     *
     * @return {@link Long}
     */
    public static Long getPatient() {
        return patientStore.get();
    }

    /**
     * 获取当前日志操作
     *
     * @return
     */
    public static List<DataUpdate> getDataUpdated() {
        return logOperateStore.get();
    }

    /**
     * 结束操作，移除操作日志对象
     */
    public static void endOperate() {
        logOperateStore.remove();
        patientStore.remove();
    }

    /**
     * 设置操作带来的数据更新内容
     *
     * @param dataUpdate 数据更新内容
     */
    public static void setDataUpdate(DataUpdate dataUpdate) {
        List<DataUpdate> dataUpdates = logOperateStore.get();
        if (dataUpdate == null) {
            log.error("添加操作日志内容时必须先注解操作");
            throw new SystemException(SystemException.CODE_ERROR_CODE);
        }
        dataUpdates.add(dataUpdate);
    }


    /**
     * 设置操作患者
     *
     * @param patientId 患者ID
     */
    public static void setPatient(Long patientId) {
        Long patient = patientStore.get();
        if (patient == null) {
            log.error("添加操作日志内容时必须先注解操作");
            throw new SystemException(SystemException.CODE_ERROR_CODE);
        }
        patientStore.set(patientId);
    }

}
