package com.luna.his.config;

import java.util.function.Predicate;

import com.google.common.base.Function;
import com.google.common.base.Optional;

import lombok.Getter;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;

@Getter
@SuppressWarnings("all")
public enum SwaggerGroup {

    SYSTEM("用户认证", basePackage("com.luna.his.controller.core"), PathSelectors.any()),

    SETTING("系统设置", basePackage("com.luna.his.controller.setting"), PathSelectors.any()),

    CLINIC_SETTING("系统设置-诊所中心设置", basePackage("com.luna.his.controller.setting.clinic"), PathSelectors.any()),

    PLAN_SETTING("系统设置-治疗计划", basePackage("com.luna.his.controller.setting"), PathSelectors.ant("/his/setting/plan/**")),

    PATIENT("患者中心", basePackage("com.luna.his.controller.patient"), PathSelectors.any()),

    PATIENT_EMR("患者中心-电子病历", basePackage("com.luna.his.controller.medical.emr"), PathSelectors.any()),

    PATIENT_PLAN("患者中心-治疗计划", basePackage("com.luna.his.controller.medical"), PathSelectors.ant("/his/medical/plan/**")),

    VISIT("就诊管理", basePackage("com.luna.his.controller.medical"), PathSelectors.any()),

    VISIT_WORK("a预约中心-工作台", basePackage("com.luna.his.controller.medical"), PathSelectors.ant("/his/medical/visit/**")),

    ORG("组织架构", basePackage("com.luna.his.controller.org"), PathSelectors.any()),

    CHARGE("收费管理", basePackage("com.luna.his.controller.charge"), PathSelectors.any()),

    MEMBER("会员(卡)管理", basePackage("com.luna.his.controller.member"), PathSelectors.any()),

    COUPON("卡券", basePackage("com.luna.his.controller.coupon"), PathSelectors.any()),

    Drug("药品信息",basePackage("com.luna.his.controller.drug"), PathSelectors.any()),

    SMS("短信管理",basePackage("com.luna.his.controller.sms"), PathSelectors.any()),

    WAREHOUSE("库房管理",basePackage("com.luna.his.controller.warehouse"), PathSelectors.any()),

    SEARCH("搜索管理",basePackage("com.luna.his.controller.search"), PathSelectors.any()),

    ;


    /**
     * 分组名称
     */
    private final String groupName;
    /**
     * 扫描包
     */
    private final Predicate<RequestHandler> apis;
    /**
     * 扫描包
     */
    private final Predicate<String> paths;


    SwaggerGroup(String groupName, Predicate<RequestHandler> apis, Predicate<String> paths) {
        this.groupName = groupName;
        this.apis = apis;
        this.paths = paths;
    }

    private static com.google.common.base.Predicate<RequestHandler> basePackage(String... basePackages) {
        return input -> declaringClass(input).transform(handlerPackage(basePackages)).or(true);
    }

    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }

    private static Function<Class<?>, Boolean> handlerPackage(String[] basePackage) {
        return input -> {
            // 循环判断匹配
            for (String strPackage : basePackage) {
                boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                if (isMatch) {
                    return true;
                }
            }
            return false;
        };
    }
}
