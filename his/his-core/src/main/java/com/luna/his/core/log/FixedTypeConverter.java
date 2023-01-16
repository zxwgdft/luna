package com.luna.his.core.log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.luna.his.core.mapper.LogOperateMapper;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * 固定类型值转化
 *
 * @author TontoZhou
 */
@Component
@RequiredArgsConstructor
public class FixedTypeConverter {

    private final LogOperateMapper logOperateMapper;

    /**
     * 获取真正需要的值
     *
     * @param type
     * @param list
     */
    public void getRealValues(FieldType type, List<DataUpdateConverter.LazyValue> list) {
        Set<Long> ids = new HashSet<>();
        for (DataUpdateConverter.LazyValue item : list) {
            if (item.isMulti) {
                List<Long> multiVal = item.multiVal;
                if (multiVal != null && multiVal.size() > 0) {
                    ids.addAll(multiVal);
                }
            } else {
                if (item.singleVal != null) {
                    ids.add(item.singleVal);
                }
            }
        }

        List<IdName> result = null;
        if (type == FieldType.EMPLOYEE) {
            result = logOperateMapper.findEmployeeName(ids);
        } else if (type == FieldType.HOSPITAL) {
            result = logOperateMapper.findHospitalName(ids);
        } else if (type == FieldType.PATIENT) {
            result = logOperateMapper.findPatientName(ids);
        }

        Map<Long, String> map = null;
        if (result != null) {
            map = new HashMap<>();
            for (IdName i : result) {
                map.put(i.id, i.name);
            }
        }

        for (DataUpdateConverter.LazyValue item : list) {
            String realVal = null;
            if (item.isMulti) {
                List<Long> multiVal = item.multiVal;
                if (multiVal != null && multiVal.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (Long val : multiVal) {
                        String rv = map.get(val);
                        if (rv != null) {
                            sb.append(rv).append("，");
                        }
                    }
                    if (sb.length() > 0) {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    realVal = sb.toString();
                }
            } else {
                if (item.singleVal != null) {
                    realVal = map.get(item.singleVal);
                }
            }

            if (item.isOld) {
                item.update.setOldVal(realVal);
            } else {
                item.update.setNewVal(realVal);
            }
        }
    }

    @Getter
    @Setter
    public static class IdName {
        private Long id;
        private String name;
    }

}
