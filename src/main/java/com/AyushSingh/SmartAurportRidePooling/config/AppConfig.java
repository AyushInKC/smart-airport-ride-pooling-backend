package com.AyushSingh.SmartAurportRidePooling.config;

import com.AyushSingh.SmartAurportRidePooling.matching.MatchingEngine;
import com.AyushSingh.SmartAurportRidePooling.matching.MatchingEngineImpl;
import com.AyushSingh.SmartAurportRidePooling.pricing.PricingStrategy;
import com.AyushSingh.SmartAurportRidePooling.pricing.DefaultPricingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AppConfig {
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        return new StringRedisTemplate(factory);
    }

    @Bean
    public ThreadPoolTaskExecutor matchingExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(16);
        executor.setMaxPoolSize(32);
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("matching-");
        executor.initialize();
        return executor;
    }

    @Bean
    public MatchingEngine matchingEngine() {
        return new MatchingEngineImpl();
    }

    @Bean
    public PricingStrategy pricingStrategy() {
        return new DefaultPricingStrategy();
    }
}
