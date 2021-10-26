package com.freddiemac.housing.suggestion;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class SuggestionAggregator {

    public Flux<Suggestion> suggestions(Flux<Suggestion> suggestions)
    {
        return suggestions.take(10);
    }

}
