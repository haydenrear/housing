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

    public abstract void setBuilder(WebClient.Builder builder, RequestBuilder requestBuilder);

    public Flux<V> getData(SuggestionMetadata suggestionMetadata)
    {
         return locationService.getDataFromGoogle(suggestionMetadata.getCity())
                 .map(str -> {
                     return locationService.parseData(str, 0);
                 }).flatMapMany(geoJson -> {
                     if(geoJson.isPresent()){
                         return this.repo.findByLocationIsWithin(geoJson.get().getT1());
                     }
                     return Flux.empty();
                 });
//        return this.requestBuilder.createSuggestionRequest(suggestionMetadata)
//                .flatMap(this::getData)
//                .map(s -> (V) s);
//        suggestionMetadata.getProperties()
//                .map(suggestionProperties -> {
//                    suggestionProperties.getUriReplacements().get("city");
//                    suggestionProperties.getUriReplacements().get("city");
//                })
//        return repo.findByLocationIsWithin()
    }

    protected Flux<? extends SuggestionData> getData(UriAndRequest uriAndRequest)
    {
        return builder.baseUrl(uriAndRequest.url().toString())
                .build()
                .method(uriAndRequest.getMethod())
                .retrieve()
                .bodyToFlux(this.suggestionDataClzz);
    }

    public WebClient.Builder getBuilder()
    {
        return builder;
    }

    public void setBuilder(WebClient.Builder builder)
    {
        this.builder = builder;
    }

    public U getRepo()
    {
        return repo;
    }

    public void setRepo(U repo)
    {
        this.repo = repo;
    }

    public RequestBuilder getRequestBuilder()
    {
        return requestBuilder;
    }

    public void setRequestBuilder(RequestBuilder requestBuilder)
    {
        this.requestBuilder = requestBuilder;
    }

    public Class<T> getSuggestionDataClzz()
    {
        return suggestionDataClzz;
    }

    public void setSuggestionDataClzz(Class<T> suggestionDataClzz)
    {
        this.suggestionDataClzz = suggestionDataClzz;
    }

    public LocationService<V> getLocationService()
    {
        return locationService;
    }

    public void setLocationService(LocationService<V> locationService)
    {
        this.locationService = locationService;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataApiService<?, ?, ?> that = (DataApiService<?, ?, ?>) o;

        if (builder != null
                ? !builder.equals(that.builder)
                : that.builder != null) return false;
        if (repo != null
                ? !repo.equals(that.repo)
                : that.repo != null) return false;
        if (requestBuilder != null
                ? !requestBuilder.equals(that.requestBuilder)
                : that.requestBuilder != null) return false;
        if (suggestionDataClzz != null
                ? !suggestionDataClzz.equals(that.suggestionDataClzz)
                : that.suggestionDataClzz != null) return false;
        return locationService != null
                ? locationService.equals(that.locationService)
                : that.locationService == null;
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
