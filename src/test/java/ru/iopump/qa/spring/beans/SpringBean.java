package ru.iopump.qa.spring.beans;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * https://springframework.guru/spring-bean-lifecycle/
 */
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SpringBean {

    public final String userName;

    public SpringBean(@Value("${spring.user.variable:unknown}") String userName) {
        log.info("SpringBean construct");
        this.userName = userName;
    }

    public String call() {
        return "Name from bean : " + userName;
    }
}
