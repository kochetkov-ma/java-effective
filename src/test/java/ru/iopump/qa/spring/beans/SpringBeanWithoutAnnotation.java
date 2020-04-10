package ru.iopump.qa.spring.beans;

import java.io.Closeable;
import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@Value
@Slf4j
public class SpringBeanWithoutAnnotation implements

    Closeable,

    InitializingBean,  // Инициализация
    ApplicationContextAware,
    BeanNameAware,
    BeanFactoryAware,
    DisposableBean

{

    @org.springframework.beans.factory.annotation.Value("${spring.user.variable:unknown}")
    String fromProps;
    String value;

    //// BEAN LIFECYCLE

    @PostConstruct
    public void postConstruct() {
        log.info("[PROTOTYPE][SpringBeanWithoutAnnotation] postConstruct ??? WHY BEFORE afterPropertiesSet ???");
    }

    @PreDestroy
    public void preDestroy() {
        log.info("[PROTOTYPE][SpringBeanWithoutAnnotation] preDestroy");
    }


    @Override
    public void close() {
        log.info("[PROTOTYPE][SpringBeanWithoutAnnotation] close");

    }

    @Override
    public void afterPropertiesSet() {
        log.info("[PROTOTYPE][SpringBeanWithoutAnnotation] afterPropertiesSet");
    }

    @Override
    public void setBeanFactory(@Nonnull BeanFactory beanFactory) throws BeansException {
        log.info("[PROTOTYPE][SpringBeanWithoutAnnotation] aware setBeanFactory");
    }

    @Override
    public void setBeanName(@Nonnull String name) {
        log.info("[PROTOTYPE][SpringBeanWithoutAnnotation] aware setBeanName " + name);
    }

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
        log.info("[PROTOTYPE][SpringBeanWithoutAnnotation] aware setApplicationContext");
    }



    @Override
    public void destroy() {
        log.info("[PROTOTYPE][SpringBeanWithoutAnnotation] destroy");
        close();
    }
}
