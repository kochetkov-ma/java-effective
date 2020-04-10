package ru.iopump.qa.spring.cfg;

import java.io.IOException;
import java.util.Properties;
import javax.annotation.Nonnull;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.io.support.PropertySourceFactory;

@Slf4j
@NoArgsConstructor
public class PropertyFactory implements PropertySourceFactory {

    @NotNull
    @Override
    public PropertySource<?> createPropertySource(String name, @Nonnull EncodedResource resource) throws IOException {

        final Properties properties = PropertiesLoaderUtils.loadProperties(resource);

        log.info("\n\n[PropertyFactory] {}\n", properties);


        return new PropertiesPropertySource(name, properties) {

            @Override
            public Object getProperty(String name) {
                log.info("\n\n[Populate property] {}\n\n", name);
                return super.getProperty(name);
            }

        };



    }
}
