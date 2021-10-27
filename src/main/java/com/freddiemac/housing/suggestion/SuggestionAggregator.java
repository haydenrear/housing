package com.freddiemac.housing.suggestion;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class SuggestionAggregator {

    public Flux<Suggestion> suggestions(List<Suggestion> suggestions)
    {
        return Flux.fromIterable(suggestions.subList(0, Math.min(suggestions.size(), 10)));
    }

}
