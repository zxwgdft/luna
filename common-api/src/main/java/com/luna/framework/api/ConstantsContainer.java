package com.luna.framework.api;

import java.util.List;
import java.util.Map;

public interface ConstantsContainer {

    /**
     * 获取多个类型常量
     *
     * @param typeCodes 常量类型编码数组
     */
    Map<String, List<Constant>> getTypeConstants(String... typeCodes);

    /**
     * 获取某个类型常量
     *
     * @param typeCode 常量类型编码
     */
    List<Constant> getTypeConstant(String typeCode);


    /**
     * 根据常量类型和value得到常量（基于key和value一对一）
     *
     * @param typeCode 常量类型编码
     * @param value    常量value
     * @return 常量value对应的key
     */
    Constant getConstantByValue(String typeCode, String value);

    /**
     * 根据常量类型和value得到常量key （基于key和value一对一）
     *
     * @param typeCode 常量类型编码
     * @param value    常量value
     * @return 常量key
     */
    String getKeyByValue(String typeCode, String value);

    /**
     * 根据常量类型和key得到常量
     *
     * @param typeCode 常量类型编码
     * @param key      常量key
     */
    Constant getConstantByKey(String typeCode, String key);

    /**
     * 根据常量类型和key得到常量value
     *
     * @param typeCode 常量类型编码
     * @param key      常量key
     * @return 常量value
     */
    String getValueByKey(String typeCode, String key);

}
