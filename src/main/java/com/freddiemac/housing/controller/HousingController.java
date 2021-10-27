package com.freddiemac.housing.controller;

import com.freddiemac.housing.service.SuggestionService;
import com.freddiemac.housing.suggestion.SuggestionMetadata;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class HousingController {

    SuggestionService suggestionService;

    public HousingController(SuggestionService suggestionService)
    {
        this.suggestionService = suggestionService;
    }


    @PostMapping("suggestion")
    public ModelAndView createSuggestion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
    {
        var city = httpServletRequest.getParameter("city");
        var state = httpServletRequest.getParameter("state");
        SuggestionMetadata metadata = new SuggestionMetadata();
        metadata.setCity(city);
        metadata.setState(state);
        ModelAndView modelAndView = new ModelAndView("index");
        var suggestion = suggestionService.createSuggestions(metadata).collectList().block().get(0);
        modelAndView.getModel().put("price", suggestion.getValue());
        modelAndView.getModel().put("address", suggestion.getAddress());
        modelAndView.getModel().put("returnRate", suggestion.getValue());
        return modelAndView;
    }

    @GetMapping("/")
    public ModelAndView home() {
        return new ModelAndView("index");
    }


}
