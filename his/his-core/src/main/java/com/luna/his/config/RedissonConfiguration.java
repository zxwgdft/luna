package com.luna.his.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisOperations;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 参照redisson-spring-starter中的创建RedissonClient实现，
 * 把全部配置参数都移到了RedissonProperties中，可根据需要改写。
 */
@Configuration
@ConditionalOnClass({Redisson.class, RedisOperations.class})
@EnableConfigurationProperties({RedissonProperties.class})
public class RedissonConfiguration {

    private static final String REDIS_PROTOCOL_PREFIX = "redis://";
    private static final String REDISS_PROTOCOL_PREFIX = "rediss://";

    @Autowired
    private RedissonProperties redissonProperties;

    @Autowired
    private ApplicationContext ctx;

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean(RedissonClient.class)
    public RedissonClient redisson() throws IOException {
        Config config = null;
        Duration timeoutValue = redissonProperties.getTimeout();
        int timeout;
        if (null == timeoutValue) {
            timeout = 10000;
        } else {
            timeout = (int) timeoutValue.toMillis();
        }

        if (redissonProperties.getConfig() != null) {
            try {
                config = Config.fromYAML(redissonProperties.getConfig());
            } catch (IOException e) {
                try {
                    config = Config.fromJSON(redissonProperties.getConfig());
                } catch (IOException e1) {
                    throw new IllegalArgumentException("Can't parse config", e1);
                }
            }
        } else if (redissonProperties.getFile() != null) {
            try {
                InputStream is = getConfigStream();
                config = Config.fromYAML(is);
            } catch (IOException e) {
                // trying next format
                try {
                    InputStream is = getConfigStream();
                    config = Config.fromJSON(is);
                } catch (IOException e1) {
                    throw new IllegalArgumentException("Can't parse config", e1);
                }
            }
        } else if (redissonProperties.getSentinel() != null) {
            String[] nodes = convert(redissonProperties.getSentinel().getNodes());
            config = new Config();
            config.useSentinelServers()
                    .setMasterName(redissonProperties.getSentinel().getMaster())
                    .addSentinelAddress(nodes)
                    .setDatabase(redissonProperties.getDatabase())
                    .setConnectTimeout(timeout)
                    .setPassword(redissonProperties.getPassword());
        } else if (redissonProperties.getCluster() != null) {
            String[] nodes = convert(redissonProperties.getCluster().getNodes());

            config = new Config();
            config.useClusterServers()
                    .addNodeAddress(nodes)
                    .setConnectTimeout(timeout)
                    .setPassword(redissonProperties.getPassword());
        } else {
            config = new Config();
            String prefix = redissonProperties.isSsl() ? REDISS_PROTOCOL_PREFIX : REDIS_PROTOCOL_PREFIX;
            config.useSingleServer()
                    .setAddress(prefix + redissonProperties.getHost() + ":" + redissonProperties.getPort())
                    .setConnectTimeout(timeout)
                    .setDatabase(redissonProperties.getDatabase())
                    .setPassword(redissonProperties.getPassword());
        }

        return Redisson.create(config);
    }

    private String[] convert(List<String> nodesObject) {
        List<String> nodes = new ArrayList<String>(nodesObject.size());
        for (String node : nodesObject) {
            if (!node.startsWith(REDIS_PROTOCOL_PREFIX) && !node.startsWith(REDISS_PROTOCOL_PREFIX)) {
                nodes.add(REDIS_PROTOCOL_PREFIX + node);
            } else {
                nodes.add(node);
            }
        }
        return nodes.toArray(new String[nodes.size()]);
    }

    private InputStream getConfigStream() throws IOException {
        Resource resource = ctx.getResource(redissonProperties.getFile());
        InputStream is = resource.getInputStream();
        return is;
    }
}
