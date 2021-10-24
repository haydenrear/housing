package com.freddiemac.housing.service.request;

import com.freddiemac.housing.config.DataApiProperties;
import com.freddiemac.housing.model.PopulationDensity;
import com.freddiemac.housing.suggestion.SuggestionMetadata;
import com.freddiemac.housing.suggestion.SuggestionProperties;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@Component
public class RequestBuilder {

    DataApiProperties dataApiProperties;

    public Flux<UriAndRequest> createSuggestionRequest(SuggestionMetadata suggestionMetadata)
    {
        return suggestionMetadata.getProperties()
                .map(apiProp -> getUri(apiProp)
                        .map(uri -> {
                            Class<? extends SuggestionRequest> suggestionRequest = dataApiProperties.getSuggestionRequests().get(apiProp.getSuggestionType());
                            SuggestionRequest suggestionRequestCreated = getSuggestionRequest(suggestionRequest, apiProp.getRequestAttributes());
                            return new UriAndRequest(uri, suggestionRequestCreated);
                        })
                        .orElseThrow()
                );
    }


    private <T extends SuggestionRequest> T getSuggestionRequest(Class<T> suggestionRequest,
                                                                 Map<String, String> requestAttributes)
    {
        T request = SuggestionRequestFactory.CreateSuggestionRequest(suggestionRequest);
        request.addRequestAttributes(requestAttributes);
        return request;
    }

    private Optional<URI> getUri(SuggestionProperties suggestionMetadata)
    {
        try {
            return Optional.of(swapUrlParams(suggestionMetadata.getQueryReplacements(), suggestionMetadata.getUriReplacements(), new URI(suggestionMetadata.getSuggestionType())));
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
}