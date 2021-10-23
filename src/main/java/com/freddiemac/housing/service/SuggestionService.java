package com.freddiemac.housing.service;

import com.freddiemac.housing.model.SuggestionData;
import com.freddiemac.housing.suggestion.Suggestion;
import com.freddiemac.housing.suggestion.SuggestionAggregator;
import com.freddiemac.housing.suggestion.SuggestionFactory;
import com.freddiemac.housing.suggestion.SuggestionMetadata;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SuggestionService {

    SuggestionAggregator suggestionAggregator;
    List<SuggestionFactory> suggestionFactories;

    public SuggestionService(SuggestionAggregator suggestionAggregator,
                             List<SuggestionFactory> suggestionFactories)
    {
        this.suggestionAggregator = suggestionAggregator;
        this.suggestionFactories = suggestionFactories;
    }

    public Flux<Suggestion> createSuggestions(SuggestionMetadata suggestionMetadata)
    {
        return aggregateSuggestions(
                Flux.fromIterable(suggestionFactories)
                    .map(suggestionFactory -> suggestionFactory.createSuggestion(suggestionMetadata))
        );
    }

    public Flux<Suggestion> aggregateSuggestions(Flux<Suggestion> suggestions)
    {
        return null;
    }

}
