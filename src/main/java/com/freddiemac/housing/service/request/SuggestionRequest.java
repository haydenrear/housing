package com.freddiemac.housing.service.request;

import java.util.Map;

public abstract class SuggestionRequest {
    public abstract SuggestionRequest addRequestAttributes(Map<String,String> requestAttributes);
}