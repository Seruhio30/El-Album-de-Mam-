package com.album_de_mama.back_end.importvalidation.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ImportPropertiesTests {

    private final ApplicationContextRunner contextRunner =
            new ApplicationContextRunner()
                    .withUserConfiguration(TestConfiguration.class)
                    .withPropertyValues(
                            "app.import.root=/tmp/recuerdos-import"
                    );

    @Test
    void shouldBindImportRootProperty() {
        contextRunner.run(context -> {
            ImportProperties properties =
                    context.getBean(ImportProperties.class);

            assertNotNull(properties.getRoot());
            assertEquals(
                    Path.of("/tmp/recuerdos-import"),
                    properties.getRoot()
            );
        });
    }

    @Configuration(proxyBeanMethods = false)
    @EnableConfigurationProperties(ImportProperties.class)
    static class TestConfiguration {
    }
}
