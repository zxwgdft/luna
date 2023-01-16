package com.luna.his.core.log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.luna.his.core.util.MultiIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.luna.framework.api.SystemException;
import com.luna.framework.spring.SpringBeanHelper;
import com.luna.framework.utils.convert.DateFormatUtil;
import com.luna.framework.utils.reflect.Entity;
import com.luna.framework.utils.reflect.EntityField;

import lombok.extern.slf4j.Slf4j;

/**
 * @author TontoZhou
 */
@Slf4j
@Component
public class DataUpdateConverter {

    @Autowired
    private FixedTypeConverter fixedTypeConverter;

    private Object getValue(FieldValue fv, Object obj, Map<FieldType, List<LazyValue>> lazyLoadMap,
                            FieldUpdate update, boolean isOld) {
        FieldValueConverter converter = fv.unit.converter;
        Object value = fv.value;
        // 如果存在指定converter优先使用
        if (converter != null) {
            value = converter.convert(value, obj, fv.unit.logField);
        } else if (value != null && value != "空") {
            FieldType type = fv.unit.logField.fieldType();
            if (type != FieldType.NONE) {
                LazyValue lazyValue = new LazyValue(type, value, update, isOld, fv.unit.logField.isMulti());
                List<LazyValue> list = lazyLoadMap.computeIfAbsent(type, k -> new ArrayList<>());
                list.add(lazyValue);
            } else {
                if (value instanceof Date) {
                    value = DateFormatUtil.getThreadSafeFormat(fv.unit.logField.dateFormat()).format((Date) value);
                } else if (value instanceof Boolean) {
                    value = (Boolean) value ? "是" : "否";
                }
            }
        }
        return value;
    }

    public List<FieldUpdate> getFieldUpdate(Object origin, Object current, List<FieldUpdate> updates) {
        if (origin == null && current == null) {
            return updates;
        }

        if (updates == null) {
            updates = new ArrayList<>();
        }

        Map<FieldType, List<LazyValue>> lazyLoadMap = new HashMap<>();
        if (origin == null) {
            // 如果源数据为null，新增情况
            Map<String, FieldValue> map = getGrabber(current.getClass()).getFieldValueMap(current);
            if (map != null) {
                for (FieldValue fv : map.values()) {
                    if (fv.value == null || "".equals(fv.value)) {
                        continue;
                    }
                    FieldUpdate update = new FieldUpdate(fv.unit.name);
                    update.setNewVal(getValue(fv, current, lazyLoadMap, update, false));
                    updates.add(update);
                }
            }
        } else if (current == null) {
            Map<String, FieldValue> map = getGrabber(origin.getClass()).getFieldValueMap(origin);
            if (map != null) {
                for (FieldValue fv : map.values()) {
                    if (fv.value == null || "".equals(fv.value)) {
                        continue;
                    }
                    FieldUpdate update = new FieldUpdate(fv.unit.name);
                    update.setOldVal(getValue(fv, origin, lazyLoadMap, update, true));
                    updates.add(update);
                }
            }
        } else {
            Map<String, FieldValue> originMap = getGrabber(origin.getClass()).getFieldValueMap(origin);
            Map<String, FieldValue> currentMap = getGrabber(current.getClass()).getFieldValueMap(current);

            for (Map.Entry<String, FieldValue> entry : currentMap.entrySet()) {
                FieldValue ofv = originMap.get(entry.getKey());
                FieldValue cfv = entry.getValue();

                if (cfv == null) {
                    // 存在不对等的日志字段则不处理
                    log.warn("日志操作数据变更字段不对等，在类[{}]中不存在日志字段[{}]", current.getClass(), entry.getKey());
                    continue;
                }

                Object ov = ofv.value;
                Object cv = cfv.value;

                if ("".equals(ov)) {
                    ov = null;
                }

                if ("".equals(cv)) {
                    cv = null;
                }

                if (ov == null && cv == null) {
                    continue;
                }

                if (ov == null) {
                    ofv.value = "空";
                }

                if (cv == null) {
                    cfv.value = "空";
                }

                if (Objects.equals(ov, cv) && ofv.unit.logField.isFilterEqual()) {
                    // 都不为null但相等则不处理
                    continue;
                }
                FieldUpdate update = new FieldUpdate(ofv.unit.name);
                update.setOldVal(getValue(ofv, origin, lazyLoadMap, update, true));
                update.setNewVal(getValue(cfv, current, lazyLoadMap, update, false));
                updates.add(update);
            }
        }

        if (lazyLoadMap.size() > 0) {
            // 处理延迟加载的值
            for (Map.Entry<FieldType, List<LazyValue>> entry : lazyLoadMap.entrySet()) {
                FieldType type = entry.getKey();
                List<LazyValue> list = entry.getValue();
                fixedTypeConverter.getRealValues(type, list);
            }
        }

        return updates;
    }

    static class LazyValue {
        FieldType type;
        List<Long> multiVal;
        Long singleVal;
        FieldUpdate update;
        boolean isOld;
        boolean isMulti;

        LazyValue(FieldType type, Object val, FieldUpdate update, boolean isOld, boolean isMulti) {
            if (isMulti) {
                this.multiVal = MultiIdUtil.split2LongList((String) val);
                this.isMulti = true;
            } else {
                this.singleVal = (Long) val;
            }

            this.type = type;
            this.update = update;
            this.isOld = isOld;
        }
    }

    /**
     * 存放字段值转化器
     */
    private static Map<Class, FieldValueConverter> converterMap = new ConcurrentHashMap<>();

    static {
        // 一些可固定的转化方法可在这里创建并放入map
    }

    /**
     * 存放字段抓取器
     */
    private static Map<String, FieldGrabber> grabberCache = new ConcurrentHashMap<>();

    /**
     * 获取查询条件构建起
     */
    private static FieldGrabber getGrabber(Class<?> clazz) {
        String key = clazz.getName();
        FieldGrabber grabber = grabberCache.get(key);
        if (grabber == null) {
            // 同步创建Builder
            synchronized (grabberCache) {
                grabber = grabberCache.get(key);
                if (grabber == null) {
                    grabber = new FieldGrabber(clazz);
                    grabberCache.put(key, grabber);
                }
            }
        }
        return grabber;
    }

    private static class FieldGrabber {

        private ArrayList<FieldUnit> fieldUnits;

        private FieldGrabber(Class<?> queryClass) {
            this.fieldUnits = new ArrayList<>();
            for (EntityField entityField : Entity.getEntity(queryClass).getEntityFields()) {
                LogField logField = entityField.getAnnotation(LogField.class);
                if (logField != null) {
                    String name = logField.value();
                    if (name.length() == 0) {
                        name = logField.name();
                    }

                    String field = logField.field();
                    if (field.length() == 0) {
                        field = entityField.getName();
                    }

                    Class<? extends FieldValueConverter> converterClass = logField.converter();

                    FieldValueConverter converter = null;
                    if (converterClass != FieldValueConverter.NoneConverter.class) {
                        converter = converterMap.get(converterClass);
                        if (converter == null) {
                            synchronized (converterMap) {
                                converter = converterMap.get(converterClass);
                                if (converter == null) {
                                    converter = SpringBeanHelper.getFirstBeanByType(converterClass);
                                    if (converter == null) {
                                        try {
                                            converter = converterClass.newInstance();
                                        } catch (Exception e) {
                                            log.error("尝试创建FieldValueConverter[class={}]异常", converterClass);
                                            throw new SystemException(SystemException.CODE_ERROR_CODE);
                                        }
                                    }
                                    converterMap.put(converterClass, converter);
                                }
                            }
                        }
                    }

                    FieldUnit unit = new FieldUnit(name, field, entityField.getGetMethod(), converter, logField);
                    fieldUnits.add(unit);
                }
            }
        }

        private Map<String, FieldValue> getFieldValueMap(Object obj) {
            if (fieldUnits.size() == 0) {
                return null;
            }
            Map<String, FieldValue> map = new HashMap<>((int) (fieldUnits.size() / 0.75 + 1));
            for (FieldUnit unit : fieldUnits) {
                try {
                    Object value = unit.getMethod.invoke(obj);
                    map.put(unit.field, new FieldValue(unit.name, value, unit));
                } catch (Exception e) {
                    log.error("get log fieldMap error!", e);
                }
            }
            return map;
        }
    }

    private static class FieldUnit {
        private String name;
        private String field;
        private Method getMethod;
        private FieldValueConverter converter;
        private LogField logField;

        private FieldUnit(String name, String field, Method getMethod, FieldValueConverter converter, LogField logField) {
            this.name = name;
            this.field = field;
            this.getMethod = getMethod;
            this.converter = converter;
            this.logField = logField;
        }
    }

    private static class FieldValue {
        String name;
        Object value;
        FieldUnit unit;

        FieldValue(String name, Object value, FieldUnit unit) {
            this.name = name;
            this.value = value;
            this.unit = unit;
        }
    }
}
