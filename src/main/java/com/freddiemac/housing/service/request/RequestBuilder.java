package com.freddiemac.housing.service.request;

import com.freddiemac.housing.config.DataApiProperties;
import com.freddiemac.housing.suggestion.SuggestionMetadata;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Map;

@Component
public class RequestBuilder {

    DataApiProperties dataApiProperties;

    public UriAndRequest createSuggestionRequest(SuggestionMetadata suggestionMetadata)
    {
        return null;
    }

    private URI swapUrlParams(SuggestionMetadata suggestionMetadata, URI url)
    {
        return null;
    }

    private SuggestionRequest addRequestAttributes(SuggestionMetadata suggestionMetadata, Map<String,String> requestAttributes)
    {
        return null;
    }

}