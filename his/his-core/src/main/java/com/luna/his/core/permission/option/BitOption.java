package com.luna.his.core.permission.option;

/**
 * 基于bit的boolean类型的配置项（处理配置项又多又不需要关联查询的情况）
 *
 * @author TontoZhou
 */
public interface BitOption {

    /**
     * 获取名称
     *
     * @return
     */
    String getName();

    /**
     * 获取bit位置
     *
     * @return
     */
    int getPosition();

}
