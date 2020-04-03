package ru.iopump.qa.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import ru.iopump.qa.spring.beans.SpringBean;
import ru.iopump.qa.spring.beans.SpringBeanWithoutAnnotation;

@Slf4j
public class AnnotationApplicationContext {
    static {
        /* -Dspring.profiles.active="test,prod" */
        System.setProperty("spring.profiles.active", "test,prod");

        /* Custom variable */
        System.setProperty("spring.user.variable", "max");
    }

    public static void main(String[] args) {

        /* ApplicationContext vs BeanFactory: https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/core.html#context-introduction-ctx-vs-beanfactory */

        try (GenericApplicationContext context = new AnnotationConfigApplicationContext("ru.iopump.qa.spring.beans")) {
//            SpringBean springBean1 = context.getBean("springBean", SpringBean.class);
//            SpringBean springBean2 = (SpringBean) context.getBean("springBean");
//            SpringBean springBean3 = context.getBean(SpringBean.class);
//
//            assert springBean1 == springBean2;
//            assert springBean2 == springBean3;

            /* Bug */
            /* If I call this code next class for `context.getBean(SpringBeanWithoutAnnotation.class)` will fail with NoSuchBeanDefinitionException */
            /* Even this bean will be registered */
            try {
                SpringBeanWithoutAnnotation noSpringBean = context.getBean(SpringBeanWithoutAnnotation.class);
            } catch (NoSuchBeanDefinitionException ex) {
                log.error("No bean", ex);
            }

            context.getBeanFactory().clearMetadataCache();
            AbstractBeanDefinition definition = BeanDefinitionBuilder.genericBeanDefinition(SpringBeanWithoutAnnotation.class)
                .setScope("prototype")
                .addConstructorArgValue("my bean")
                .getBeanDefinition();

            context.registerBeanDefinition("myBean", definition);

            try {
                SpringBeanWithoutAnnotation noSpringBean = (SpringBeanWithoutAnnotation)context.getBean("springBeanWithoutAnnotation");
            } catch (NoSuchBeanDefinitionException ex) {
                log.error("No bean", ex);
            }

            SpringBeanWithoutAnnotation newSpringBean1 = context.getBean("myBean", SpringBeanWithoutAnnotation.class);
            SpringBeanWithoutAnnotation newSpringBean2 = (SpringBeanWithoutAnnotation) context.getBean("myBean");
            SpringBeanWithoutAnnotation newSpringBean3 = context.getBean(SpringBeanWithoutAnnotation.class);

            assert newSpringBean1 != newSpringBean2;
            assert newSpringBean2 != newSpringBean3;
            assert newSpringBean1 != newSpringBean3;

        }
    }
}