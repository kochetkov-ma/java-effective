package ru.iopump.qa.spring;

import java.util.Collection;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.iopump.qa.spring.beans.BeanWithCache;
import ru.iopump.qa.spring.beans.BeanWithCollection;
import ru.iopump.qa.spring.beans.PrototypeBean;
import ru.iopump.qa.spring.beans.PrototypeProxyBean;
import ru.iopump.qa.spring.beans.SingletonBean;
import ru.iopump.qa.spring.beans.SpringBeanWithoutAnnotation;
import ru.iopump.qa.spring.cfg.BeanConfiguration;
import ru.iopump.qa.spring.cfg.CacheConfiguration;
import ru.iopump.qa.spring.cfg.IBean;

@Slf4j
public class AnnotationApplicationContext {
    static {
        /* -Dspring.profiles.active="test,prod" */
        System.setProperty("spring.profiles.active", "test,prod");
    }

    @Test
    public void spring() {

        /* ApplicationContext vs BeanFactory: https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/core.html#context-introduction-ctx-vs-beanfactory */

        /* AnnotationConfigApplicationContext use AnnotatedBeanDefinitionReader for resolve annotation like @Configuration and others */

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {

            context.scan("ru.iopump.qa.spring.beans"); // Only for AnnotationConfigApplicationContext implementation
            context.register(BeanConfiguration.class); // Only for AnnotationConfigApplicationContext implementation
            context.register(CacheConfiguration.class); // Add caching configuration
            context.refresh(); // don't forget !!! AnnotationConfigApplicationContext support only single refresh !!!

            /* SECTION 1 */
            /*

            log.info("\n\n\n SECTION 1 \n\n\n");

            SpringBean springBean1 = context.getBean("springBean", SpringBean.class);

            SpringBean springBean2 = (SpringBean) context.getBean("springBean");

            SpringBean springBean3 = context.getBean(SpringBean.class);

            assert springBean1 == springBean2;
            assert springBean2 == springBean3;

            // Добавить собственную конфигурацию aplication.properties на ряду с system properties и env properties

            // Bug
            // If I call this code next class for `context.getBean(SpringBeanWithoutAnnotation.class)` will fail with NoSuchBeanDefinitionException
            // Even this bean will be registered
            try {
                SpringBeanWithoutAnnotation noSpringBean = context.getBean(SpringBeanWithoutAnnotation.class);
            } catch (NoSuchBeanDefinitionException ex) {
                log.error("No bean", ex);
            }
            context.getBeanFactory().clearMetadataCache(); // workaround
            */

            /* https://springframework.guru/spring-bean-lifecycle/ */
            ///////////////////////////
            /* SECTION 2 - PROTOTYPE */
            log.info("\n\n\n SECTION 2 - PROTOTYPE \n\n\n");

            // Create bean definition
            BeanDefinition definition = BeanDefinitionBuilder.genericBeanDefinition(SpringBeanWithoutAnnotation.class)
                .setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) // Look here
                .addConstructorArgValue("my bean")
                .getBeanDefinition();

            // Register bean definition
            context.registerBeanDefinition("myBean", definition);

            // Get 3 beans
            SpringBeanWithoutAnnotation newSpringBean1 = context.getBean("myBean", SpringBeanWithoutAnnotation.class);
            SpringBeanWithoutAnnotation newSpringBean2 = (SpringBeanWithoutAnnotation) context.getBean("myBean");
            SpringBeanWithoutAnnotation newSpringBean3 = context.getBean(SpringBeanWithoutAnnotation.class);

            // Assert
            assert newSpringBean1 != newSpringBean2;
            assert newSpringBean2 != newSpringBean3;
            assert newSpringBean1 != newSpringBean3;

            // Destroy prototypes
            context.getBeanFactory().destroyBean(newSpringBean1);
            context.getBeanFactory().destroyBean(newSpringBean2);
            context.getBeanFactory().destroyBean(newSpringBean3);

            // You can manage any classes by Spring
            // In BeanConfiguration.maxString()
            String stringBean = context.getBean("userString", String.class);
            log.info("--- [INFO] --- " + stringBean);

            /////////////////////////////////
            /* SECTION 3 - BEAN COLLECTION */
            log.info("\n\n\n SECTION 3 - BEAN COLLECTION \n\n\n");

            Collection<? extends IBean> beanCollectionViaContext = context.getBeansOfType(IBean.class).values();
            Collection<? extends IBean> beanCollectionViaMethod = (Collection<? extends IBean>) context.getBean("iBeanUserCollection");
            Collection<? extends IBean> beanCollectionViaInject = context.getBean(BeanWithCollection.class).getIBeans();

            log.info("--- [INFO] --- beanCollectionViaContext: " + beanCollectionViaContext.stream().map(IBean::info).collect(Collectors.joining(", ")));
            log.info("--- [INFO] --- beanCollectionViaMethod: " + beanCollectionViaMethod.stream().map(IBean::info).collect(Collectors.joining(", ")));
            log.info("--- [INFO] --- beanCollectionViaMethod: " + beanCollectionViaInject.stream().map(IBean::info).collect(Collectors.joining(", ")));

            /* https://www.baeldung.com/spring-inject-prototype-bean-into-singleton */
            /////////////////////////////////////////////
            /* SECTION 4 - BEAN PROTOTYPE IN SINGLETON */
            log.info("\n\n\n SECTION 4 - BEAN PROTOTYPE IN SINGLETON \n\n\n");

            SingletonBean singletonBean = context.getBean(SingletonBean.class);
            PrototypeBean prototypeBean1 = singletonBean.getPrototypeBean();
            PrototypeBean prototypeBean2 = singletonBean.getPrototypeBean();
            assert prototypeBean1 == prototypeBean2; // The same bean

            prototypeBean1 = singletonBean.getPrototypeBeanViaBeanFactory();
            prototypeBean2 = singletonBean.getPrototypeBeanViaBeanFactory();
            assert prototypeBean1 != prototypeBean2; // Different beans

            prototypeBean1 = singletonBean.getPrototypeBeanViaObjectFactory();
            prototypeBean2 = singletonBean.getPrototypeBeanViaObjectFactory();
            assert prototypeBean1 != prototypeBean2; // Different beans

            prototypeBean1 = singletonBean.getPrototypeBeanLookup();
            prototypeBean2 = singletonBean.getPrototypeBeanLookup();
            assert prototypeBean1 != prototypeBean2; // Different beans

            // Special case
            PrototypeProxyBean prototypeProxyBean1 = singletonBean.getPrototypeProxyBean();
            PrototypeProxyBean prototypeProxyBean2 = singletonBean.getPrototypeProxyBean();

            assert prototypeProxyBean1 == prototypeProxyBean2; // The same bean !!!

            assert prototypeProxyBean1.get() != prototypeProxyBean1.get(); // Different result via Spring proxy
            assert prototypeProxyBean1.get() != prototypeProxyBean2.get(); // Different result via Spring proxy

            assert prototypeProxyBean1.hashCode() == prototypeProxyBean2.hashCode(); // But hashCode the same !!!

            ///////////////////////////////
            /* SECTION 5 - CACHED METHOD */
            BeanWithCache beanWithCache = context.getBean(BeanWithCache.class);
            assert beanWithCache.getUuid() != beanWithCache.getUuid();
            assert beanWithCache.getUuidWithCache() == beanWithCache.getUuidWithCache();
        }
    }
}