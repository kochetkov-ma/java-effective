package ru.iopump.qa.spring;

import java.util.Collection;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.iopump.qa.spring.beans.SpringBean;
import ru.iopump.qa.spring.beans.SpringBeanWithoutAnnotation;
import ru.iopump.qa.spring.cfg.BeanConfiguration;
import ru.iopump.qa.spring.cfg.IBean;

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

        /* AnnotationConfigApplicationContext use AnnotatedBeanDefinitionReader for resolve annotation like @Configuration and others */

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {

            context.scan("ru.iopump.qa.spring.beans"); // Only for AnnotationConfigApplicationContext implementation
            context.register(BeanConfiguration.class); // Only for AnnotationConfigApplicationContext implementation
            context.refresh(); // don't forget !!! AnnotationConfigApplicationContext support only single refresh !!!

            /* SECTION 1 */
            log.info("\n\n\n SECTION 1 \n\n\n");

            SpringBean springBean1 = context.getBean("springBean", SpringBean.class);

            SpringBean springBean2 = (SpringBean) context.getBean("springBean");

            SpringBean springBean3 = context.getBean(SpringBean.class);

            assert springBean1 == springBean2;
            assert springBean2 == springBean3;

            // Добавить собственную конфигурацию aplication.properties на ряду с system properties и env properties

            /* Bug */
            /* If I call this code next class for `context.getBean(SpringBeanWithoutAnnotation.class)` will fail with NoSuchBeanDefinitionException */
            /* Even this bean will be registered */
            try {
                SpringBeanWithoutAnnotation noSpringBean = context.getBean(SpringBeanWithoutAnnotation.class);
            } catch (NoSuchBeanDefinitionException ex) {
                log.error("No bean", ex);
            }

            /* SECTION 2 */
            log.info("\n\n\n SECTION 2 \n\n\n");

            context.getBeanFactory().clearMetadataCache(); // workaround

            BeanDefinition definition = BeanDefinitionBuilder.genericBeanDefinition(SpringBeanWithoutAnnotation.class)
                .setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) // Look here
                .addConstructorArgValue("my bean")
                .getBeanDefinition();

            context.registerBeanDefinition("myBean", definition);

            try {
                SpringBeanWithoutAnnotation noSpringBean = (SpringBeanWithoutAnnotation) context.getBean("springBeanWithoutAnnotation");
            } catch (NoSuchBeanDefinitionException ex) {
                log.error("No bean", ex);
            }

            SpringBeanWithoutAnnotation newSpringBean1 = context.getBean("myBean", SpringBeanWithoutAnnotation.class);
            SpringBeanWithoutAnnotation newSpringBean2 = (SpringBeanWithoutAnnotation) context.getBean("myBean");
            SpringBeanWithoutAnnotation newSpringBean3 = context.getBean(SpringBeanWithoutAnnotation.class);

            assert newSpringBean1 != newSpringBean2;
            assert newSpringBean2 != newSpringBean3;
            assert newSpringBean1 != newSpringBean3;

            context.getBeanFactory().destroyBean(newSpringBean1);
            context.getBeanFactory().destroyBean(newSpringBean2);
            context.getBeanFactory().destroyBean(newSpringBean3);

            newSpringBean1.close();

            /* SECTION 3 */
            log.info("\n\n\n SECTION 3 \n\n\n");

            String stringBean = context.getBean("userString", String.class);
            log.info("--- [INFO] --- " + stringBean);

            Collection<IBean> beanCollection = context.getBeansOfType(IBean.class).values();
            Collection<IBean> beanUserCollection = (Collection<IBean>) context.getBean("iBeanUserCollection");

            log.info("--- [INFO] --- beanCollection: " + beanCollection.stream().map(IBean::info).collect(Collectors.joining(", ")));
            log.info("--- [INFO] --- beanUserCollection: " + beanUserCollection.stream().map(IBean::info).collect(Collectors.joining(", ")));

        }
    }
}