package ru.iopump.qa.spring;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.support.GenericApplicationContext;

@Slf4j
public class DefaultListableBeanFactoryRedundantCacheTest {

    /**
     * Fail.
     */
    @Test(expected = NoSuchBeanDefinitionException.class)
    public void noSuchBeanDefinitionExceptionAfterRegisterBean() {
        try (GenericApplicationContext context = new GenericApplicationContext()) {
            context.refresh();
            NoSuchBeanDefinitionException exception = null;

            try {
                context.getBean(Bean.class);
            } catch (NoSuchBeanDefinitionException ex) {
                exception = ex;
                log.error("It's ok. " +
                        "But 'org.springframework.beans.factory.support.DefaultListableBeanFactory.getBeanNamesForType" +
                        "(java.lang.Class<?>, boolean, boolean)' has added bean to cache '#501 - cache.put(type, resolvedBeanNames);'"
                    , ex);
            }
            assertThat(exception).isInstanceOf(NoSuchBeanDefinitionException.class);

            /* No Clear Metadata Cache 'context.getBeanFactory().clearMetadataCache()' */

            final BeanDefinition definition = BeanDefinitionBuilder
                .genericBeanDefinition(Bean.class)
                .getBeanDefinition();
            context.registerBeanDefinition("myBean", definition);

            final Bean newBean = context.getBean(Bean.class);
        }
    }

    /**
     * Success.
     */
    @Test
    public void noSuchBeanDefinitionExceptionNotExistsAfterRegisterBeanAndCleanCache() {
        try (GenericApplicationContext context = new GenericApplicationContext()) {
            context.refresh();
            NoSuchBeanDefinitionException exception = null;

            try {
                context.getBean(Bean.class);
            } catch (NoSuchBeanDefinitionException ex) {
                exception = ex;
                log.error("It's ok. " +
                        "But 'org.springframework.beans.factory.support.DefaultListableBeanFactory.getBeanNamesForType" +
                        "(java.lang.Class<?>, boolean, boolean)' has added bean to cache '#501 - cache.put(type, resolvedBeanNames);'"
                    , ex);
            }
            assertThat(exception).isInstanceOf(NoSuchBeanDefinitionException.class);

            /* Clear Metadata Cache */
            context.getBeanFactory().clearMetadataCache();

            final AbstractBeanDefinition definition = BeanDefinitionBuilder
                .genericBeanDefinition(Bean.class)
                .getBeanDefinition();
            context.registerBeanDefinition("myBean", definition);

            final Bean newBean = context.getBean(Bean.class);

            assertThat(newBean).isNotNull();
        }
    }

    private static class Bean {
    }
}
