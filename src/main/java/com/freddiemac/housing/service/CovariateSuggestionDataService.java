package com.freddiemac.housing.service;

import com.freddiemac.housing.model.CovariateSuggestionData;
import com.freddiemac.housing.repo.SuggestionRepo;

public class CovariateSuggestionDataService<T extends CovariateSuggestionData, U extends SuggestionRepo<CovariateSuggestionData>> extends DataApiService<T,U,CovariateSuggestionData> {
    public CovariateSuggestionDataService(U covariateSuggestonRepo, Class<T> populationDensityClass)
    {
        this.repo = covariateSuggestonRepo;
        this.suggestionDataClzz = populationDensityClass;
    }
}