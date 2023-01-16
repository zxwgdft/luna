package com.luna.schedule.job;

import com.luna.constant.JobConstants;
import com.luna.constant.RedisConstants;
import com.luna.framework.utils.TimeUtil;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class RedisJobs {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 清除无效的redis key
     * TODO 暂时没有用到短信及黑名单等，是否需要以下任务需要考虑
     */
    @XxlJob(JobConstants.HANDLER_CLEAN_REDIS_KEY)
    public void cleanRedisKey() {
        log.info("开始清理无效的redis key");
        Date now = new Date();

        // 清理多少天的key
        int size = 2;
        Date start = TimeUtil.getDateBefore(now.getTime(), size);

        List<Integer> serials = TimeUtil.getSerialNumberByDay(start.getTime(), now.getTime());
        List<String> keys = new ArrayList<>(size * 3);
        for (int i = 0; i < size; i++) {
            int serial = serials.get(i);
            keys.add(RedisConstants.HASH_SMS_PHONE_COUNT_DAY_PREFIX + serial);
            keys.add(RedisConstants.HASH_SMS_IP_COUNT_DAY_PREFIX + serial);
            keys.add(RedisConstants.HASH_IP_LOGIN_ERROR_COUNT_DAY_PREFIX + serial);
        }

        long count = stringRedisTemplate.delete(keys);
        log.info("成功清除{}个无效key", count);
        log.info("清理无效的redis key任务结束");
    }

}
