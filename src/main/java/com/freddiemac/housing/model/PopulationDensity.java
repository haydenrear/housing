package com.freddiemac.housing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@EqualsAndHashCode(callSuper = true)
@Scope("prototype")
@Component
@AllArgsConstructor
@Data
public class PopulationDensity extends CovariateSuggestionData {
}
