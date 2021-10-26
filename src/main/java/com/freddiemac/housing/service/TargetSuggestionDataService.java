package com.freddiemac.housing.service;

import com.freddiemac.housing.model.TargetSuggestionData;
import com.freddiemac.housing.repo.SuggestionRepo;
import com.freddiemac.housing.repo.TargetSuggestionRepo;
import com.freddiemac.housing.service.request.RequestBuilder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class TargetSuggestionDataService<T extends TargetSuggestionData, U extends SuggestionRepo<TargetSuggestionData>> extends DataApiService<T, U, TargetSuggestionData> {
    public TargetSuggestionDataService(U repo, Class<T> suggestionDataClzz)
    {
        super(repo, suggestionDataClzz);
    }

    public TargetSuggestionDataService() {}

    @Override
    @Autowired
    public void setBuilder(WebClient.Builder builder, RequestBuilder requestBuilder)
    {
        this.builder = builder;
        this.requestBuilder = requestBuilder;
    }



}
