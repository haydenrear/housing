package com.freddiemac.housing.suggestion;

import com.freddiemac.housing.model.*;
import com.freddiemac.housing.repo.SuggestionRepo;
import com.freddiemac.housing.service.CovariateSuggestionDataService;
import com.freddiemac.housing.service.TargetSuggestionDataService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InternalReturnSuggestionFactory extends SuggestionFactory<CovariateTargetPriceDiff> {

    public InternalReturnSuggestionFactory(
            List<TargetSuggestionDataService<? extends SuggestionData, ? extends SuggestionRepo<? extends SuggestionData>>> targetSuggestionDataServices,
            List<CovariateSuggestionDataService<? extends SuggestionData, ? extends SuggestionRepo<? extends SuggestionData>>> covariateSuggestionDataServices)
    {
        super(targetSuggestionDataServices, covariateSuggestionDataServices);
    }

    @Override
    public Flux<Suggestion> createSuggestion(SuggestionMetadata suggestionMetadata)
    {
        var covariatesFlux = Flux.fromIterable(covariateSuggestionDataServices)
                .filter(covar -> covar.getSuggestionDataClzz().equals(PopulationDensity.class))
                .last()
                .flatMapMany(suggestionData -> suggestionData.getData(suggestionMetadata).cast(PopulationDensity.class));

        var targetsFlux = Flux.fromIterable(targetSuggestionDataServices)
                .filter(covar -> covar.getSuggestionDataClzz().equals(InternalReturnRateData.class))
                .last()
                .flatMapMany(suggestionData -> suggestionData.getData(suggestionMetadata).cast(InternalReturnRateData.class));

        return createSuggestionImpl(covariatesFlux, targetsFlux);
    }

    public Flux<Suggestion> createSuggestionImpl(Flux<PopulationDensity> covariatesFlux, Flux<InternalReturnRateData> targetsFlux)
    {
        var groupedCovar = covariatesFlux.collect(Collectors.groupingBy(SuggestionData::getDateLocation));
        var groupedTargets = targetsFlux.collect(Collectors.groupingBy(SuggestionData::getDateLocation));

        var covars = collectSuggestions(groupedCovar);
        var targets = collectSuggestions(groupedTargets);

        // get change in density
        Map<SuggestionData.DateLocation, List<Double>> toAverage = new HashMap<>();

        return covars.flatMapMany(covarFound -> {
            return targets.map(targetFound -> {
                for(var covar : covarFound.entrySet()){
                    double prevCovar = 0d;
                    double prevTarget = 0d;
                    for(var date : covar.getValue().entrySet()){
                        var avgCovar = getAverage(date.getValue()).orElse(0d) - prevCovar;
                        var dateListMap = targetFound.get(covar.getKey());

                        if(dateListMap== null || dateListMap.isEmpty())
                            continue;

                        var covarList = dateListMap.get(date.getKey());

                        if(covarList == null || covarList.isEmpty())
                            continue;

                        var avgTarget = getAverage(covarList).orElse(0d) - prevTarget;

                        prevCovar = avgCovar;
                        prevTarget = avgTarget;
                        SuggestionData.DateLocation key = new SuggestionData.DateLocation(date.getKey(),
                                                                                          covar.getKey());

                        double finalPrevCovar = prevCovar;
                        double finalPrevTarget = prevTarget;

                        toAverage.compute(key, (prev, next) -> {
                            if (next == null)
                                next = new ArrayList<>();
                            next.add(finalPrevCovar / finalPrevTarget);
                            return next;
                        });
                    }
                }

                return toAverage.entrySet().stream()
                        .map(entry -> Map.entry(entry.getKey(), entry.getValue().stream().mapToDouble(d -> d).average()))
                        .collect(Collectors.toMap(Map.Entry::getKey,value -> value.getValue().orElse(0)));

            }).flatMapMany(val -> {
                return groupedTargets.flatMapMany(map -> {
                    var last = Collections.max(map.keySet(), Comparator.comparing(SuggestionData.DateLocation::getDate));
                    return Flux.fromIterable(map.get(last)).flatMap(toCalculateFor -> {
                        var data = toCalculateFor.getData();
                        var price = data.length > 0 ? data[0] : 0;
                        price += price * val.get(last);
                        toCalculateFor.setProjectedPrice(price);
                        return Flux.just(new Suggestion(toCalculateFor,"Internal Return Rate"));
                    });
                });
            });
        });
    }

    private OptionalDouble getAverage(List<? extends SuggestionData> date)
    {
        if(date == null)
            return OptionalDouble.empty();
        return date.stream().mapToDouble(d -> d.getData() != null && d.getData().length > 0
                ? d.getData()[0]
                : 0).average();
    }

    private <T extends SuggestionData> Mono<Map<String,Map<Date,List<T>>>> collectSuggestions(
            Mono<Map<SuggestionData.DateLocation, List<T>>> groupedCovar
    )
    {
        Map<String,Map<Date,List<T>>> collected = new HashMap<>();
        return groupedCovar.map(dateLocationListMap -> {
            for(var entry : dateLocationListMap.entrySet()){
                var location = entry.getKey().getLocation();
                collected.compute(location, (prev,next) -> {
                    if(next == null)
                        next = new HashMap<>();
                    next.compute(entry.getKey().getDate(), (innerPrev,innerNext) -> {
                        if(innerNext == null)
                            innerNext = new ArrayList<>();
                        innerNext.addAll(entry.getValue());
                        return innerNext;
                    });
                    return next;
                });
            }
            return collected;
        });
    }

    @Override
    protected CovariateTargetPriceDiff emptyCovariateTarget(SuggestionData.DateLocation key)
    {
        return new CovariateTargetPriceDiff(new ArrayList<>(), new ArrayList<>(), key);
    }

    @Override
    protected Flux<Suggestion> createSuggestions(SortedMap<SuggestionData.DateLocation, CovariateTargetPriceDiff> data)
    {
        throw new UnsupportedOperationException();
    }



}
