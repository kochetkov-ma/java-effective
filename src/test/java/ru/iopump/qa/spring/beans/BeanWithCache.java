package ru.iopump.qa.spring.beans;

import java.util.UUID;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import ru.iopump.qa.spring.cfg.CacheConfiguration;

@Component
public class BeanWithCache {

    /**
     * Cfg in {@link CacheConfiguration}
     */
    @Cacheable("uuid")
    public UUID getUuidWithCache() {
        return UUID.randomUUID();
    }

    public UUID getUuid() {
        return UUID.randomUUID();
    }
}
