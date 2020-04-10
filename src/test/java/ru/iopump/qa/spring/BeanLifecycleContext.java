package ru.iopump.qa.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.iopump.qa.spring.beans.SpringBeanWithoutAnnotation;
import ru.iopump.qa.spring.cfg.BeanConfiguration;

@Slf4j
public class BeanLifecycleContext {
    static {
        /* -Dspring.profiles.active="test,prod" */
        System.setProperty("spring.profiles.active", "test,prod");
    }

    public static void main(String[] args) throws InterruptedException {

        /* https://springframework.guru/spring-bean-lifecycle/ */
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        context.scan("ru.iopump.qa.spring.beans"); // Only for AnnotationConfigApplicationContext implementation
        context.register(BeanConfiguration.class); // Only for AnnotationConfigApplicationContext implementation
        context.refresh(); // don't forget !!! AnnotationConfigApplicationContext support only single refresh !!!

        context.registerShutdownHook();


        log.info("\n\n\n !!!!!!!!!!!!!!!!!!! SECTION !!!!!!!!!!!!!!!!!!! \n\n\n");


        BeanDefinition definition = BeanDefinitionBuilder.genericBeanDefinition(SpringBeanWithoutAnnotation.class)
            .setScope(ConfigurableBeanFactory.SCOPE_SINGLETON) // Look here
            .addConstructorArgValue("my bean")
            .getBeanDefinition();

        context.registerBeanDefinition("myBean", definition);

        log.info("[INIT BEAN] ");
        SpringBeanWithoutAnnotation newSpringBean1 = context.getBean("myBean", SpringBeanWithoutAnnotation.class);


        log.info("\n\n\n !!!!!!!!!!!!!!!!!!! [OUR BEAN] {} !!!!!!!!!!!!!!!!!!!!!!! \n\n\n", newSpringBean1);
    }
}