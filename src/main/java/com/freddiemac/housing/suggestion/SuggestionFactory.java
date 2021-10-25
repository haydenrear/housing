package com.freddiemac.housing.suggestion;

import com.freddiemac.housing.model.CovariateSuggestionData;
import com.freddiemac.housing.model.SuggestionData;
import com.freddiemac.housing.model.TargetSuggestionData;
import com.freddiemac.housing.repo.SuggestionRepo;
import com.freddiemac.housing.service.CovariateSuggestionDataService;
import com.freddiemac.housing.service.DataApiService;
import com.freddiemac.housing.service.TargetSuggestionDataService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

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
        var covariatesFlux = Flux.fromIterable(covariateSuggestionDataServices).map(covariateSuggestionDataService -> covariateSuggestionDataService.getData(suggestionMetadata));
        var targetsFlux = Flux.fromIterable(targetSuggestionDataServices).map(targetSuggestionDataService -> targetSuggestionDataService.getData(suggestionMetadata));
        createSuggestion(covariatesFlux, targetsFlux);
        //Todo:
        return null;
    }

    private <T extends CovariateSuggestionData, U extends TargetSuggestionData> void createSuggestion(Flux<Flux<T>> covariatesFlux, Flux<Flux<U>> targetsFlux)
    {


        var covariatesGrouped = covariatesFlux.map(covariateFlux -> covariateFlux.collect(Collectors.groupingBy(SuggestionData::dateLocation)));
        var targetsGrouped = targetsFlux.map(targetFlux -> targetFlux.collect(Collectors.groupingBy(SuggestionData::dateLocation)));
        //Todo:


    }


    @Data
    @AllArgsConstructor
    static class CovariateTarget {
        List<List<? extends SuggestionData>> covariates;
        List<List<? extends SuggestionData>> targets;
        SuggestionData.DateLocation dateLocation;
    }

    protected <T extends SuggestionData> Mono<Suggestion> createSuggestion(Flux<SuggestionFactory.CovariateTarget> suggestionData, String suggestionType)
    {
            //Todo:;
        return null;
    }

}