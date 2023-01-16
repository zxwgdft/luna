package com.luna.his.core.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author TontoZhou
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataUpdate {

    // 原数据
    private Object origin;
    // 更新后数据
    private Object current;
    // 字段数据变更，用于补充注解方式无法解决的问题
    private List<FieldUpdate> updates;

    public DataUpdate(Object origin, Object current) {
        this.origin = origin;
        this.current = current;
    }


}
