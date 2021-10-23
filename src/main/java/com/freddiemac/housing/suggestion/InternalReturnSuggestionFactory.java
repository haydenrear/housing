package com.freddiemac.housing.suggestion;

import com.freddiemac.housing.model.InternalReturnRateData;
import com.freddiemac.housing.model.SuggestionData;
import com.freddiemac.housing.repo.SuggestionRepo;
import com.freddiemac.housing.service.CovariateSuggestionDataService;
import com.freddiemac.housing.service.TargetSuggestionDataService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InternalReturnSuggestionFactory extends SuggestionFactory {
    public InternalReturnSuggestionFactory(
            List<TargetSuggestionDataService<? extends SuggestionData, ? extends SuggestionRepo<? extends SuggestionData>>> targetSuggestionDataServices,
            List<CovariateSuggestionDataService<? extends SuggestionData, ? extends SuggestionRepo<? extends SuggestionData>>> covariateSuggestionDataServices)
    {
        super(targetSuggestionDataServices, covariateSuggestionDataServices);
    }

    private Suggestion createSuggestion(InternalReturnRateData[] data)
    {
        return null;
    }
}
