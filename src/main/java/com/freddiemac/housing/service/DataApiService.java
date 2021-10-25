package com.freddiemac.housing.service;

import com.freddiemac.housing.model.SuggestionData;
import com.freddiemac.housing.repo.SuggestionRepo;
import com.freddiemac.housing.service.request.RequestBuilder;
import com.freddiemac.housing.service.request.UriAndRequest;
import com.freddiemac.housing.suggestion.SuggestionMetadata;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;

import java.util.function.Function;

@NoArgsConstructor
@Data
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

    public Flux<T> getData(SuggestionMetadata suggestionMetadata)
    {
        return this.requestBuilder.createSuggestionRequest(suggestionMetadata)
                .flatMap(this::getData);
    }

    //Todo: save data in repos, and also get data from repos in addition to the other services



    protected Flux<T> getData(UriAndRequest uriAndRequest)
    {
        return builder.baseUrl(uriAndRequest.url().toString())
                .build()
                .method(uriAndRequest.getMethod())
                .retrieve()
                .bodyToFlux(this.suggestionDataClzz);
    }

}
