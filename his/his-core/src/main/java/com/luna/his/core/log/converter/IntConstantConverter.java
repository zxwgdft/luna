package com.luna.his.core.log.converter;

import java.util.HashMap;
import java.util.Map;

import com.luna.his.core.constant.EmployeeJob;
import com.luna.his.core.constant.IntCodeEnum;
import com.luna.his.core.constant.VisitAppointmentType;
import com.luna.his.core.constant.VisitState;
import org.springframework.stereotype.Component;

import com.luna.his.core.log.FieldValueConverter;
import com.luna.his.core.log.LogField;

/**
 * @author TontoZhou
 */
@Component
public class IntConstantConverter implements FieldValueConverter {

    private final static Map<Class, Map<Integer, String>> enumMap = new HashMap<>();

    static {
        enumMap.put(EmployeeJob.class, getCodeNameMap(EmployeeJob.class));
        enumMap.put(VisitAppointmentType.class, getCodeNameMap(VisitAppointmentType.class));
        enumMap.put(VisitState.class, getCodeNameMap(VisitState.class));
    }

    private static Map<Integer, String> getCodeNameMap(Class enumClass) {
        Map<Integer, String> map = new HashMap<>();
        for (Object _item : enumClass.getEnumConstants()) {
            IntCodeEnum item = (IntCodeEnum) _item;
            map.put(item.getCode(), item.getName());
        }
        return map;
    }

    public static String getName(Class clazz, int code) {
        Map<Integer, String> map = enumMap.get(clazz);
        return map == null ? null : map.get(code);
    }

    @Override
    public Object convert(Object fieldVal, Object obj, LogField logField) {
        if (fieldVal != null && fieldVal instanceof Number) {
            int code = ((Number) fieldVal).intValue();
            return getName(logField.enumType(), code);
        }
        return null;
    }


}
