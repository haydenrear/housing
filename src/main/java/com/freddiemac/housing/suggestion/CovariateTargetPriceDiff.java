package com.freddiemac.housing.suggestion;

import com.freddiemac.housing.model.InternalReturnRateData;
import com.freddiemac.housing.model.PopulationDensity;
import com.freddiemac.housing.model.SuggestionData;

import java.util.List;
import java.util.stream.Collectors;

public class CovariateTargetPriceDiff extends CovariateTarget {


    public CovariateTargetPriceDiff(List<List<? extends SuggestionData>> covariates,
                                    List<List<? extends SuggestionData>> targets,
                                    SuggestionData.DateLocation dateLocation)
    {
        super(covariates, targets, dateLocation);
        covariates = filterForReturnRate(covariates, PopulationDensity.class);
        targets = filterForReturnRate(targets, InternalReturnRateData.class);
    }

    private List<List<? extends SuggestionData>> filterForReturnRate(
            List<List<? extends SuggestionData>> covariates, Class<? extends SuggestionData> obj)
    {
        return covariates.stream()
                .map(suggestionData -> suggestionData.stream().filter(
                        suggestionDataItem -> suggestionDataItem.getClass().equals(
                                obj)).collect(
                        Collectors.toList()))
                .collect(Collectors.toList());
    }
}
