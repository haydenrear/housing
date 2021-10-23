package com.freddiemac.housing.suggestion;

import com.freddiemac.housing.config.DataApiProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
@AllArgsConstructor
@NoArgsConstructor
public class SuggestionMetadata {

    DataApiProperties dataApiProperties;
    List<String> metadata;

    public Map<String, Map<String,Map<String, String>>> getProperties()
    {
        return dataApiProperties.getUrlData().entrySet().stream()
                .filter(entry -> metadata.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
    }
}
