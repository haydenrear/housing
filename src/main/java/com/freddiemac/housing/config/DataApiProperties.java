package com.freddiemac.housing.config;

import com.freddiemac.housing.service.request.SuggestionRequest;
import com.freddiemac.housing.util.YamlPropertyFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "suggestion")
@PropertySource(factory = YamlPropertyFactory.class, value = "classpath:application.yml")
@Data
public class DataApiProperties {

    private Map<String,Map<String,Map<String,Map<String,String>>>> suggestionData;

//    private Map<String, Map<String,Map<String,String>>> urlData;
//    private Map<String, Map<String,String>> requestAttributes;
    private Map<String, Class<? extends SuggestionRequest>> suggestionRequests;

}
