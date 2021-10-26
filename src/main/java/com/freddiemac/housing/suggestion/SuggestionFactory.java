package com.freddiemac.housing.suggestion;

import com.freddiemac.housing.model.CovariateSuggestionData;
import com.freddiemac.housing.model.SuggestionData;
import com.freddiemac.housing.model.TargetSuggestionData;
import com.freddiemac.housing.repo.SuggestionRepo;
import com.freddiemac.housing.service.CovariateSuggestionDataService;
import com.freddiemac.housing.service.TargetSuggestionDataService;
import org.reactivestreams.Publisher;
import org.springframework.context.ApplicationContextAware;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

public abstract class SuggestionFactory<T extends CovariateTarget> implements ApplicationContextAware {

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

    public abstract Flux<Suggestion> createSuggestion(SuggestionMetadata suggestionMetadata);

    protected abstract T emptyCovariateTarget(SuggestionData.DateLocation key);
    protected abstract Flux<Suggestion> createSuggestions(SortedMap<SuggestionData.DateLocation, T> data);


}