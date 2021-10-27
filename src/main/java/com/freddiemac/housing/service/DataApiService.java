package com.freddiemac.housing.service;

import com.freddiemac.housing.model.SuggestionData;
import com.freddiemac.housing.repo.SuggestionRepo;
import com.freddiemac.housing.service.request.RequestBuilder;
import com.freddiemac.housing.service.request.UriAndRequest;
import com.freddiemac.housing.suggestion.SuggestionMetadata;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Objects;

@Data
public abstract class DataApiService <T extends SuggestionData, U extends SuggestionRepo<V>, V extends SuggestionData> {

    WebClient.Builder builder;
    U repo;
    RequestBuilder requestBuilder;
    Class<T> suggestionDataClzz;
    LocationService<V> locationService;

    public DataApiService() {}

    public DataApiService(U repo, Class<T> suggestionDataClzz)
    {
        this.repo = repo;
        this.suggestionDataClzz = suggestionDataClzz;
    }

    @Autowired
    public void setLocationService(LocationService<V> locationService)
    {
        this.locationService = locationService;
    }



    public abstract void setBuilder(WebClient.Builder builder, RequestBuilder requestBuilder);

    public Flux<V> getData(SuggestionMetadata suggestionMetadata)
    {
         Flux<V> toReturn = locationService.getDataFromGoogle(suggestionMetadata.getState())
                 .map(str -> locationService.parseData(str, 0))
                 .flatMapMany(geoJson -> {
                     if(geoJson.isPresent()){
                         return this.repo.findByLocationIsWithin(geoJson.get().getT1());
                     }
                     return Flux.empty();
                 });
        var request = this.requestBuilder.createSuggestionRequest(suggestionMetadata)
                .flatMap(this::getData)
                .map(s -> (V) s);
        return Flux.concat(toReturn, request);
    }

    protected Flux<? extends SuggestionData> getData(UriAndRequest uriAndRequest)
    {
        return builder.baseUrl(uriAndRequest.url().toString())
                .build()
                .method(uriAndRequest.getMethod())
                .retrieve()
                .bodyToFlux(this.suggestionDataClzz);
    }

    public U getRepo()
    {
        return repo;
    }

    public void setRepo(U repo)
    {
        this.repo = repo;
    }

    public Class<T> getSuggestionDataClzz()
    {
        return suggestionDataClzz;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataApiService<?, ?, ?> that = (DataApiService<?, ?, ?>) o;

        if (!Objects.equals(builder, that.builder)) return false;
        if (!Objects.equals(repo, that.repo)) return false;
        if (!Objects.equals(requestBuilder, that.requestBuilder)) return false;
        if (!Objects.equals(suggestionDataClzz, that.suggestionDataClzz)) return false;
        return Objects.equals(locationService, that.locationService);
    }

    @Override
    public int hashCode()
    {
        int result = builder != null
                ? builder.hashCode()
                : 0;
        result = 31 * result + (repo != null
                ? repo.hashCode()
                : 0);
        result = 31 * result + (requestBuilder != null
                ? requestBuilder.hashCode()
                : 0);
        result = 31 * result + (suggestionDataClzz != null
                ? suggestionDataClzz.hashCode()
                : 0);
        result = 31 * result + (locationService != null
                ? locationService.hashCode()
                : 0);
        return result;
    }
}
