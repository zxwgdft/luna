package com.luna.his.core.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author TontoZhou
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldUpdate {

    // 字段名称
    private String name;
    // 旧值
    private Object oldVal;
    // 新值
    private Object newVal;

    public FieldUpdate(String name) {
        this.name = name;
    }

}
