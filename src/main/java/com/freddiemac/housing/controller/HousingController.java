package com.freddiemac.housing.controller;

import com.freddiemac.housing.service.SuggestionService;
import com.freddiemac.housing.suggestion.Suggestion;
import com.freddiemac.housing.suggestion.SuggestionFactory;
import com.freddiemac.housing.suggestion.SuggestionMetadata;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import reactor.core.publisher.Flux;

import java.util.List;


//@RestController(value = "v1")
@Controller
public class HousingController {

    SuggestionService suggestionService;

    public HousingController(SuggestionService suggestionService)
    {
        this.suggestionService = suggestionService;
    }

    @PostMapping("suggestion")
    public Flux<Suggestion> createSuggestion(@RequestBody SuggestionMetadata suggestionMetadata)
    {
        return suggestionService.createSuggestions(suggestionMetadata);
    }

    @GetMapping("/")
    public ModelAndView home() {
        return new ModelAndView("index");
    }



}
