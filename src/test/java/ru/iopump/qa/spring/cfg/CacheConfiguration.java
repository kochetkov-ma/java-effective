package ru.iopump.qa.spring.cfg;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableCaching // Enable caching
public class CacheConfiguration {

    // And create bean with caching map
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("uuid");
    }
}
