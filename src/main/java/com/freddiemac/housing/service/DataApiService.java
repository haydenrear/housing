package com.freddiemac.housing.service;

import com.freddiemac.housing.model.SuggestionData;
import com.freddiemac.housing.repo.SuggestionRepo;
import com.freddiemac.housing.service.request.RequestBuilder;
import com.freddiemac.housing.service.request.UriAndRequest;
import com.freddiemac.housing.suggestion.SuggestionMetadata;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;

import java.util.function.Function;

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

    public <K> Flux<GroupedFlux<K, Flux<T>>> getData(SuggestionMetadata suggestionMetadata, Function<Flux<T>, K> keyMapper)
    {
        return this.requestBuilder.createSuggestionRequest(suggestionMetadata)
                .map(this::getData)
                .groupBy(keyMapper);
    }

    protected Flux<T> getData(UriAndRequest uriAndRequest)
    {
        return builder.baseUrl(uriAndRequest.url().toString())
                .build()
                .method(uriAndRequest.getMethod())
                .retrieve()
                .bodyToFlux(this.suggestionDataClzz);
    }

}
