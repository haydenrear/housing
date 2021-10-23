package com.freddiemac.housing.service;

import com.freddiemac.housing.model.TargetSuggestionData;
import com.freddiemac.housing.repo.SuggestionRepo;
import com.freddiemac.housing.repo.TargetSuggestionRepo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
public class TargetSuggestionDataService<T extends TargetSuggestionData, U extends SuggestionRepo<TargetSuggestionData>> extends DataApiService<T, U, TargetSuggestionData> {
    public TargetSuggestionDataService(U repo, Class<T> suggestionDataClzz)
    {
        super(repo, suggestionDataClzz);
    }
}
