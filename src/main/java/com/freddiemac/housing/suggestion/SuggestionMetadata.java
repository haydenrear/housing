package com.freddiemac.housing.suggestion;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.freddiemac.housing.config.props.DataApiProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.sql.DataSourceDefinitions;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SuggestionMetadata {

    String city;
    String state;

    @JsonIgnore
    DataApiProperties dataApiProperties;

    List<String> metadata;

    Map<String,Map<String,String>> queryReplacements;
    Map<String,Map<String,String>> urlPathParams;
    Map<String,Map<String,String>> requestAttributeReplacements;

    public Flux<SuggestionProperties> getProperties()
    {
        return Flux.fromIterable(metadata)
                .map(uri -> {
                    var map = dataApiProperties.getSuggestionData().get(uri);
                    var queryParams = map.get("urlQueryParams");
                    var pathParams = map.get("urlPathParams");
                    var requestAttributes = map.get("requestAttributes");
                    return replaceVals(uri, queryParams, pathParams, requestAttributes, httpMethod(map.get("httpAttributes")));
                });
    }

    private String httpMethod(Map<String, String> mapEntry)
    {
        return mapEntry.get("httpMethod");
    }

    private SuggestionProperties replaceVals(
            String uri,
            Map<String, String> queryParams,
            Map<String, String> pathParams,
            Map<String, String> requestAttributes,
            String httpMethod
    )
    {

        var request = replaceAttributes(requestAttributes,requestAttributes);
        var urlPath = replaceAttributes(pathParams,pathParams);
        var urlQuery = replaceAttributes(queryParams,queryParams);
        return new SuggestionProperties(uri,urlQuery,urlPath,request,httpMethod);
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
