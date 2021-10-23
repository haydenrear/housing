package com.freddiemac.housing.suggestion;

import com.freddiemac.housing.model.SuggestionData;
import com.freddiemac.housing.model.TargetSuggestionData;
import com.freddiemac.housing.repo.SuggestionRepo;
import com.freddiemac.housing.service.CovariateSuggestionDataService;
import com.freddiemac.housing.service.TargetSuggestionDataService;

import java.util.List;

public class SuggestionFactory {

    // injected from the component scan
    protected List<TargetSuggestionDataService<? extends SuggestionData, ? extends SuggestionRepo<? extends SuggestionData>>> targetSuggestionDataServices;
    protected List<CovariateSuggestionDataService<? extends SuggestionData, ? extends SuggestionRepo<? extends SuggestionData>>> covariateSuggestionDataServices;

    public SuggestionFactory(
            List<TargetSuggestionDataService<? extends SuggestionData, ? extends SuggestionRepo<? extends SuggestionData>>> targetSuggestionDataServices,
            List<CovariateSuggestionDataService<? extends SuggestionData, ? extends SuggestionRepo<? extends SuggestionData>>> covariateSuggestionDataServices)
    {
        this.targetSuggestionDataServices = targetSuggestionDataServices;
        this.covariateSuggestionDataServices = covariateSuggestionDataServices;
    }

    public Suggestion createSuggestion(SuggestionMetadata suggestionMetadata)
    {
        return null;
    }

    protected <T extends SuggestionData> Suggestion createSuggestion(T[] suggestionData, String suggestionType)
    {
        return null;
    }

}