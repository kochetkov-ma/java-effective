package ru.iopump.qa.spring.cfg;

import java.util.Collection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@PropertySource(
    value = "application.properties",
    encoding = "UTF-8",
    ignoreResourceNotFound = false,
    name = "user-props",
    factory = PropertyFactory.class
)
@Configuration
public class BeanConfiguration {

    @Bean("userString")
    public String maxString() {
        return "Maxim Kochetkov";
    }

    @Bean
    public IBean first() {
        return () -> "firstIBean";
    }

    @Bean
    public IBean second() {
        return () -> "secondIBean";
    }

    @Bean
    public IBean third() {
        return () -> "thirdIBean";
    }

    @Bean
    public Collection<IBean> iBeanUserCollection(Collection<IBean> iBeans) {
        return iBeans;
    }
}
