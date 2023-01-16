package com.luna.schedule.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "xxl-job")
public class XxlJobProperties {

    private String adminAddresses;

    private String accessToken;

    private String executorAppname;

    private String executorAddress;

    private String executorIp;

    private int executorPort;

    private String executorLogPath;

    private int executorLogRetentionDays;

}
