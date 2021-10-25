package com.freddiemac.housing.suggestion;

import com.freddiemac.housing.model.CovariateSuggestionData;
import com.freddiemac.housing.model.SuggestionData;
import com.freddiemac.housing.model.TargetSuggestionData;
import com.freddiemac.housing.repo.SuggestionRepo;
import com.freddiemac.housing.service.CovariateSuggestionDataService;
import com.freddiemac.housing.service.TargetSuggestionDataService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
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
        return createSuggestion(covariatesFlux, targetsFlux);
    }

    private <T extends CovariateSuggestionData, U extends TargetSuggestionData> Flux<Suggestion> createSuggestion(Flux<Flux<T>> covariatesFlux, Flux<Flux<U>> targetsFlux)
    {

        Map<SuggestionData.DateLocation, CovariateTarget> covariateTargetMap = new HashMap<>();

        var covariatesGrouped = covariatesFlux.flatMap(covariateFlux -> covariateFlux.collect(Collectors.groupingBy(SuggestionData::dateLocation)));
        var targetsGrouped = targetsFlux.flatMap(targetFlux -> targetFlux.collect(Collectors.groupingBy(SuggestionData::dateLocation)));
        var covariates = extracted(covariateTargetMap, covariatesGrouped);
        var targets = extracted(covariateTargetMap, targetsGrouped);
        var total = Flux.concat(covariates,targets);

        return total.takeLast(1)
                .flatMap(this::createSuggestions);

    }

    private <T extends SuggestionData> Mono<Map<SuggestionData.DateLocation, CovariateTarget>> extracted(
            Map<SuggestionData.DateLocation, CovariateTarget> covariateTargetMap,
            Flux<Map<SuggestionData.DateLocation, List<T>>> covariatesGrouped)
    {
        return covariatesGrouped.map(covariateMap -> {
            covariateMap.forEach((key1, value) -> covariateTargetMap.compute(key1, (key, prev) -> {
                if(prev == null)
                    prev = new CovariateTarget(new ArrayList<>(), new ArrayList<>(), key);
                if (prev.covariates == null)
                    prev.covariates = new ArrayList<>();
                prev.covariates.add(value);
                return prev;
            }));
            return covariateMap;
        }).then(Mono.just(covariateTargetMap));
    }

    private Flux<Suggestion> createSuggestions(Map<SuggestionData.DateLocation, CovariateTarget> data)
    {
        //Todo:
        return Flux.empty();
    }


    @Data
    @AllArgsConstructor
    static class CovariateTarget {
        List<List<? extends SuggestionData>> covariates;
        List<List<? extends SuggestionData>> targets;
        SuggestionData.DateLocation dateLocation;
    }


}