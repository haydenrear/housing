package com.freddiemac.housing.service;

import com.freddiemac.housing.model.SuggestionData;
import com.freddiemac.housing.repo.SuggestionRepo;
import com.freddiemac.housing.service.request.RequestBuilder;
import com.freddiemac.housing.service.request.UriAndRequest;
import com.freddiemac.housing.suggestion.SuggestionMetadata;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

@NoArgsConstructor
public abstract class DataApiService <T extends SuggestionData, U extends SuggestionRepo<V>, V extends SuggestionData> {

    WebClient.Builder builder;
    U repo;
    RequestBuilder requestBuilder;
    Class<T> suggestionDataClzz;

    public DataApiService(U repo, Class<T> suggestionDataClzz)
    {
        this.repo = repo;
        this.suggestionDataClzz = suggestionDataClzz;
    }

    public abstract void setBuilder(WebClient.Builder builder, RequestBuilder requestBuilder);

    public T[][] getData(SuggestionMetadata suggestionMetadata)
    {
        return null;
    }

    protected T[] getData(UriAndRequest uriAndRequest)
    {
        return null;
    }

}
