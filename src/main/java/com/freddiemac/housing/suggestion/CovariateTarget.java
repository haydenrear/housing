package com.freddiemac.housing.suggestion;

import com.freddiemac.housing.model.SuggestionData;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CovariateTarget {

    List<List<? extends SuggestionData>> covariates;
    List<List<? extends SuggestionData>> targets;

    SuggestionData.DateLocation dateLocation;

}
