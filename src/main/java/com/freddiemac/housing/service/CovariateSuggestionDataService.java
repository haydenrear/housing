package com.freddiemac.housing.service;

import com.freddiemac.housing.model.CovariateSuggestionData;
import com.freddiemac.housing.repo.SuggestionRepo;
import com.freddiemac.housing.service.request.RequestBuilder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@NoArgsConstructor
public class CovariateSuggestionDataService<T extends CovariateSuggestionData, U extends SuggestionRepo<CovariateSuggestionData>> extends DataApiService<T,U,CovariateSuggestionData> {
    public CovariateSuggestionDataService(U covariateSuggestonRepo, Class<T> populationDensityClass)
    {
        this.repo = covariateSuggestonRepo;
        this.suggestionDataClzz = populationDensityClass;
    }

    @Override
    @Autowired
    public void setBuilder(WebClient.Builder builder, RequestBuilder requestBuilder)
    {
        this.builder = builder;
        this.requestBuilder = requestBuilder;
    }
}