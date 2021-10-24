package com.freddiemac.housing.suggestion;

import com.freddiemac.housing.config.DataApiProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

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

    Map<String,Map<String,String>> queryReplacements;
    Map<String,Map<String,String>> urlPathParams;
    Map<String,Map<String,String>> requestAttributeReplacements;

    public Flux<SuggestionProperties> getProperties()
    {
        return Flux.fromIterable(metadata)
                .flatMap(uri -> Flux.fromIterable(dataApiProperties.getSuggestionData().get(uri).entrySet()))
                .map(mapEntry -> {
                    var map = mapEntry.getValue();
                    var queryParams = map.get("urlQueryParams");
                    var pathParams = map.get("urlPathParams");
                    var requestAttributes = map.get("requestAttributes");
                    return replaceVals(mapEntry, queryParams, pathParams, requestAttributes, httpMethod(mapEntry));
                });
    }

    private String httpMethod(Map.Entry<String, Map<String, Map<String, String>>> mapEntry)
    {
        return mapEntry.getValue().get("httpAttributes").get("httpMethod");
    }

    private SuggestionProperties replaceVals(
            Map.Entry<String, Map<String, Map<String, String>>> map,
            Map<String, String> queryParams,
            Map<String, String> pathParams,
            Map<String, String> requestAttributes,
            String httpMethod
    )
    {

        var request = replaceAttributes(map.getValue().get("requestAttributes"),requestAttributes);
        var urlPath = replaceAttributes(map.getValue().get("urlPathParams"),pathParams);
        var urlQuery = replaceAttributes(map.getValue().get("urlQueryParams"),queryParams);
        return new SuggestionProperties(map.getKey(),urlQuery,urlPath,request,httpMethod);
    }

    private Map<String,String> replaceAttributes(
            Map<String, String> value,
            Map<String, String> requestAttributeReplacements
    )
    {
        if(value != null)
            return value.keySet().stream()
                    .map(s -> Map.entry(s, requestAttributeReplacements.get(s)))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return Map.of();
    }

}
