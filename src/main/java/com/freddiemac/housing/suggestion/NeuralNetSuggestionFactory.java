package com.freddiemac.housing.suggestion;

import com.freddiemac.housing.model.CovariateSuggestionData;
import com.freddiemac.housing.model.SuggestionData;
import com.freddiemac.housing.model.TargetSuggestionData;
import com.freddiemac.housing.repo.SuggestionRepo;
import com.freddiemac.housing.service.CovariateSuggestionDataService;
import com.freddiemac.housing.service.TargetSuggestionDataService;
import com.freddiemac.housing.service.request.RequestBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NeuralNetSuggestionFactory extends SuggestionFactory {

    public NeuralNetSuggestionFactory(
            List<TargetSuggestionDataService<? extends SuggestionData, ? extends SuggestionRepo<? extends SuggestionData>>> targetSuggestionDataServices,
            List<CovariateSuggestionDataService<? extends SuggestionData, ? extends SuggestionRepo<? extends SuggestionData>>> covariateSuggestionDataServices)
    {
        super(targetSuggestionDataServices, covariateSuggestionDataServices);
    }

    private <T extends CovariateSuggestionData, U extends TargetSuggestionData> List<Suggestion> createSuggestion(T[][] covariates, U[][] targets)
    {
       return null;
    }

}
