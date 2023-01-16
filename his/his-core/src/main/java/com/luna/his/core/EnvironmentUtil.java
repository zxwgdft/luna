package com.luna.his.core;

import com.luna.framework.api.SystemException;
import com.luna.framework.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author TontoZhou
 */
@Component
public class EnvironmentUtil {

    @Autowired
    private Environment environment;

    private static Environment instance;

    @PostConstruct
    public void init() {
        instance = environment;
    }

    /**
     * 获取应用服务名
     *
     * @return
     */
    public static String getAppServerName() {
        String appName = instance.getProperty("spring.application.name");
        if (StringUtil.equals(appName, "his-main")) {
            return "";
        } else {
            if (appName.startsWith("his-main-")) {
                return appName.substring(9);
            } else {
                throw new SystemException(SystemException.CODE_ERROR_CONFIG, "配置文件中的服务名错误");
            }
        }
    }

}
