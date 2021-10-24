package com.freddiemac.housing.suggestion;

import com.freddiemac.housing.config.props.DataApiProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "/application.yml", properties = "suggestion.metadata.test=true")
@ContextConfiguration(classes = SuggestionMetadataTest.ContextConfig.class, initializers = ConfigDataApplicationContextInitializer.class)
@ExtendWith(SpringExtension.class)
public class SuggestionMetadataTest {

    @Autowired
    DataApiProperties dataApiProperties;

    @Test
    public void testDataApiPropertiesInjected()
    {
        System.out.println(dataApiProperties);
        assertThat(dataApiProperties.getSuggestionData().size()).isNotZero();
        assertThat(dataApiProperties.getSuggestionRequests().size()).isNotZero();
    }

    @Configuration
    @ConditionalOnProperty(value = "suggestion.metadata.test", havingValue = "true")
    @ComponentScan(basePackageClasses = DataApiProperties.class)
    @EnableConfigurationProperties
    static class ContextConfig {
        @Bean
        DataApiProperties dataApiProperties()
        {
            return new DataApiProperties();
        }
    }

}
