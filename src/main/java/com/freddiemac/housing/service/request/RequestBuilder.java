package com.freddiemac.housing.service.request;

import com.freddiemac.housing.config.props.DataApiProperties;
import com.freddiemac.housing.suggestion.SuggestionMetadata;
import com.freddiemac.housing.suggestion.SuggestionProperties;
import org.springframework.http.HttpMethod;
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
        return suggestionMetadata.getProperties()
                .map(apiProp -> getUri(apiProp)
                        .map(uri -> {
                            Class<? extends SuggestionRequest> suggestionRequest = dataApiProperties.getSuggestionRequests().get(apiProp.getSuggestionType());
                            Optional<? extends SuggestionRequest> suggestionRequestCreated = getSuggestionRequest(suggestionRequest, apiProp.getRequestAttributes());
                            return new UriAndRequest(uri, suggestionRequestCreated.orElse(null), HttpMethod.valueOf(apiProp.getHttpMethod()));
                        })
                        .orElseThrow()
                );
    }

    private <T extends SuggestionRequest> Optional<T> getSuggestionRequest(Class<T> suggestionRequest,
                                                                 Map<String, String> requestAttributes)
    {
        if(suggestionRequest == null)
            return Optional.empty();
        T request = SuggestionRequestFactory.CreateSuggestionRequest(suggestionRequest);
        request.addRequestAttributes(requestAttributes);
        return Optional.of(request);
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