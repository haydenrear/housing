package com.freddiemac.housing.controller;

import com.freddiemac.housing.service.SuggestionService;
import com.freddiemac.housing.suggestion.Suggestion;
import com.freddiemac.housing.suggestion.SuggestionMetadata;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class HousingController {

    SuggestionService suggestionService;

    public HousingController(SuggestionService suggestionService)
    {
        this.suggestionService = suggestionService;
    }


    @GetMapping("suggestion/{city}/{state}")
    public Mono<List<Suggestion>> createSuggestion(@PathVariable String city, @PathVariable String state)
    {
        SuggestionMetadata metadata = new SuggestionMetadata();
        metadata.setCity(city);
        metadata.setState(state);
        return suggestionService.createSuggestions(metadata).collectList();
    }

}
