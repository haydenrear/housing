package com.freddiemac.housing.service;

import com.freddiemac.housing.model.SuggestionData;
import com.freddiemac.housing.suggestion.*;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class SuggestionService {

    SuggestionAggregator suggestionAggregator;
    List<SuggestionFactory<? extends CovariateTarget>> suggestionFactories;

    public SuggestionService(SuggestionAggregator suggestionAggregator,
                             List<SuggestionFactory<? extends CovariateTarget>> suggestionFactories)
    {
        this.suggestionAggregator = suggestionAggregator;
        this.suggestionFactories = suggestionFactories;
    }

    public Flux<Suggestion> createSuggestions(SuggestionMetadata suggestionMetadata)
    {
        return aggregateSuggestions(
                Flux.fromIterable(suggestionFactories)
                    .flatMap(suggestionFactory -> suggestionFactory.createSuggestion(suggestionMetadata))
        );
    }

    public Flux<Suggestion> aggregateSuggestions(Flux<Suggestion> suggestions)
    {
        return suggestionAggregator.suggestions(suggestions);
    }

}
