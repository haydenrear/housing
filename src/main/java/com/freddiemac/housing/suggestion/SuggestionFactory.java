package com.freddiemac.housing.suggestion;

import com.freddiemac.housing.model.SuggestionData;
import com.freddiemac.housing.model.TargetSuggestionData;
import com.freddiemac.housing.repo.SuggestionRepo;
import com.freddiemac.housing.service.CovariateSuggestionDataService;
import com.freddiemac.housing.service.DataApiService;
import com.freddiemac.housing.service.TargetSuggestionDataService;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SuggestionFactory {

    // injected from the component scan
    protected List<TargetSuggestionDataService<? extends SuggestionData, ? extends SuggestionRepo<? extends SuggestionData>>> targetSuggestionDataServices;
    protected List<CovariateSuggestionDataService<? extends SuggestionData, ? extends SuggestionRepo<? extends SuggestionData>>> covariateSuggestionDataServices;

    public SuggestionFactory(
            List<TargetSuggestionDataService<? extends SuggestionData, ? extends SuggestionRepo<? extends SuggestionData>>> targetSuggestionDataServices,
            List<CovariateSuggestionDataService<? extends SuggestionData, ? extends SuggestionRepo<? extends SuggestionData>>> covariateSuggestionDataServices
    )
    {
        this.targetSuggestionDataServices = targetSuggestionDataServices;
        this.covariateSuggestionDataServices = covariateSuggestionDataServices;
    }

    public Flux<Suggestion> createSuggestion(SuggestionMetadata suggestionMetadata)
    {
        Flux<? extends Suggestion> covariates = Flux
                .fromIterable(covariateSuggestionDataServices)
                .flatMap(covariateSuggestionDataService -> createSuggestion(covariateSuggestionDataService.getData(suggestionMetadata), covariateSuggestionDataService.getSuggestionDataClzz().getSimpleName()));
        Flux<? extends Suggestion> targets = Flux
                .fromIterable(targetSuggestionDataServices)
                .flatMap(targetSuggestionDataService -> createSuggestion(targetSuggestionDataService.getData(suggestionMetadata), targetSuggestionDataService.getSuggestionDataClzz().getSimpleName()));
        return Flux.concat(covariates, targets);
    }

    protected <T extends SuggestionData> Mono<Suggestion> createSuggestion(Flux<T> suggestionData, String suggestionType)
    {
        return suggestionData
                .collect(Collectors.groupingBy(SuggestionData::dateLocation))
                .flatMapMany(d -> {
                    return Flux.fromIterable(d.entrySet())
                            .map(entry -> {
                                Collections.sort(entry.getValue());
                                return Map.entry(entry.getKey(), entry.getValue());
                    });
                })
                .collectMap(Map.Entry::getKey,Map.Entry::getValue)
                .map(map -> map.get(Collections.max(map.keySet(), Comparator.comparing(SuggestionData.DateLocation::getDate))))
                .map(suggestionList -> {
                    T t = suggestionList.get(0);
                    return new Suggestion(t,suggestionType);
                });
    }

}