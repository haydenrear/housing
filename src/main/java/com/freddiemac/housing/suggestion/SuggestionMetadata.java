package com.freddiemac.housing.suggestion;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Scope("prototype")
@AllArgsConstructor
@NoArgsConstructor
public class SuggestionMetadata {

    Map<String, Map<String,String>> properties;

}
