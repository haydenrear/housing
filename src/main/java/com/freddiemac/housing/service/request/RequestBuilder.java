package com.freddiemac.housing.service.request;

import com.freddiemac.housing.config.DataApiProperties;
import com.freddiemac.housing.suggestion.SuggestionMetadata;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;

@Component
public class RequestBuilder {

    DataApiProperties dataApiProperties;

    public Flux<UriAndRequest> createSuggestionRequest(SuggestionMetadata suggestionMetadata)
    {
        return Flux.fromIterable(suggestionMetadata.getProperties().entrySet())
                .map(apiProp -> getUri(apiProp)
                        .map(uri -> new UriAndRequest(uri, dataApiProperties.getSuggestionRequest(apiProp)))
                        .orElseThrow()
                );
    }

    private Optional<URI> getUri(Map.Entry<String, Map<String, Map<String, String>>> suggestionMetadata)
    {
        try {
            return Optional.of(swapUrlParams(suggestionMetadata.getValue().get("queryParams"), suggestionMetadata.getValue().get("pathParams"), new URI(suggestionMetadata.getKey())));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private URI swapUrlParams(Map<String,String> queryParams, Map<String,String> pathParams, URI url)
    {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUri(url);
        queryParams.forEach(uriComponentsBuilder::queryParam);
        return uriComponentsBuilder.build(pathParams);
    }

    private SuggestionRequest addRequestAttributes(SuggestionMetadata suggestionMetadata, Map<String,String> requestAttributes)
    {
        return null;
    }

}