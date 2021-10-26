package com.freddiemac.housing.controller;

import com.freddiemac.housing.service.SuggestionService;
import com.freddiemac.housing.suggestion.Suggestion;
import com.freddiemac.housing.suggestion.SuggestionFactory;
import com.freddiemac.housing.suggestion.SuggestionMetadata;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@Controller(value = "v1")
public class HousingController {

    SuggestionService suggestionService;

    public HousingController(SuggestionService suggestionService)
    {
        this.suggestionService = suggestionService;
    }


    @PostMapping("/suggestion/{city}/{state}")
    public Flux<Suggestion> createSuggestion(@PathVariable String city, @PathVariable String state)
    {
        SuggestionMetadata metadata = new SuggestionMetadata();
        metadata.setCity(city);
        metadata.setState(state);
        return suggestionService.createSuggestions(metadata);
    }

}
