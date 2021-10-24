package com.freddiemac.housing.suggestion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SuggestionProperties {
    String suggestionType;
    Map<String, String> queryReplacements;
    Map<String, String> uriReplacements;
    Map<String, String> requestAttributes;
    String httpMethod;
}
