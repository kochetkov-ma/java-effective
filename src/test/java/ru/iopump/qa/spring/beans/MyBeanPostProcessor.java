package ru.iopump.qa.spring.beans;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        log.info("[PROCESSOR] postProcessBeforeInitialization '{}' '{}'", beanName, bean);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        log.info("[PROCESSOR] postProcessAfterInitialization '{}' '{}'", beanName, bean);
        return bean;
    }
}
