package com.luna.his.core.permission;

import com.luna.his.core.permission.option.BitOption;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 角色
 *
 * @author TontoZhou
 * @since 2022/12/11
 */
@Data
public class Role {
    // ID
    private Long id;
    // 是否系统默认
    private Boolean isDefault;
    // 是否管理员
    private Boolean isAdmin;
    // 角色查看等级
    private Integer dataLevel;
    // 是否我负责的
    private Boolean isInMyCharge;
    // 是否预约过我的
    private Boolean isVisitedMe;
    // 允许查看多少天内报表
    private Integer reportDayLimit;
    // 工作台类型（对应岗位值）
    private Integer workspace;
    // 角色权限code集合
    private Set<String> codes;
    // 基于BitOption的权限配置-值

    @Setter(AccessLevel.NONE)
    private Map<Class, OptionValueSet> class2optionValueSet = new HashMap<>();
    @Setter(AccessLevel.NONE)
    private Map<String, OptionValueSet> name2optionValueSet = new HashMap<>();
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private volatile Map<String, Map<String, Boolean>> simpleOptionValue;

    public boolean hasPermission(String code) {
        return codes.contains(code);
    }

    /**
     * 设置BitOption
     *
     * @param enumClass 枚举类
     * @param frame     bit数据帧
     */
    public void setBitOptions(Class<? extends BitOption> enumClass, Long frame) {
        if (frame != null && frame != 0) {
            OptionValueSet set = new OptionValueSet(enumClass);
            for (BitOption option : enumClass.getEnumConstants()) {
                String key = ((Enum) option).name();
                int position = option.getPosition();
                boolean value = ((frame >> position) & 0x1) > 0;
                set.valueMap.put(key, value);
            }
            class2optionValueSet.put(set.clazz, set);
            name2optionValueSet.put(set.name, set);
        }
    }

    /**
     * 获取BitOption值
     *
     * @param enumClass 枚举类
     * @param key       枚举的key（即枚举名称）
     * @return
     */
    public boolean getOptionValue(Class<? extends BitOption> enumClass, String key) {
        OptionValueSet set = class2optionValueSet.get(enumClass);
        if (set != null) {
            Boolean result = set.valueMap.get(key);
            return result != null ? result : false;
        }
        return false;
    }

    /**
     * 获取BitOption值
     *
     * @param bitOption 枚举的BitOption
     * @return
     */
    public boolean getOptionValue(BitOption bitOption) {
        OptionValueSet set = class2optionValueSet.get(bitOption.getClass());
        if (set != null) {
            Boolean result = set.valueMap.get(((Enum) bitOption).name());
            return result != null ? result : false;
        }
        return false;
    }


    /**
     * 获取简化的配置-值（考虑到不想提供多余信息和权限使用较多，这里做了内存缓存）
     */
    public Map<String, Map<String, Boolean>> getSimplifiedOptionValue() {
        if (simpleOptionValue == null) {
            synchronized (class2optionValueSet) {
                if (simpleOptionValue == null) {
                    simpleOptionValue = new HashMap<>();
                    for (Map.Entry<String, OptionValueSet> entry : name2optionValueSet.entrySet()) {
                        simpleOptionValue.put(entry.getKey(), entry.getValue().valueMap);
                    }
                }
            }
        }
        return simpleOptionValue;
    }

    private static class OptionValueSet {
        private Class clazz;
        private String name;
        private Map<String, Boolean> valueMap;

        public OptionValueSet(Class clazz) {
            this.clazz = clazz;
            this.name = clazz.getSimpleName();
            this.valueMap = new HashMap<>();
        }
    }

}
