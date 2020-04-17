package ru.iopump.qa.spring.beans;

import java.util.UUID;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE,
    proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PrototypeProxyBean {
    private final UUID uuid = UUID.randomUUID();

    public UUID get() {
        return uuid;
    }
}
