package com.AyushSingh.SmartAurportRidePooling.concurrency;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
public class RedisDistributedLock {
    private final StringRedisTemplate redisTemplate;
    public RedisDistributedLock(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    public boolean tryLock(String key, String value, long timeoutMs) {
        Boolean success = redisTemplate.opsForValue().setIfAbsent(key, value, timeoutMs, TimeUnit.MILLISECONDS);
        return Boolean.TRUE.equals(success);
    }
    public void unlock(String key, String value) {
        String val = redisTemplate.opsForValue().get(key);
        if (value.equals(val)) redisTemplate.delete(key);
    }

}
