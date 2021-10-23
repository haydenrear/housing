package com.freddiemac.housing.config;

import com.freddiemac.housing.util.YamlPropertyFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "suggestion")
@PropertySource(factory = YamlPropertyFactory.class, value = "classpath:application.yml")
public class DataApiProperties {
    Map<String, Map<String,String>> urlData;
    Map<String, Map<String,String>> requestAttributes;
}
