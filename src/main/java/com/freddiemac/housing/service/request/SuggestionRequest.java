package com.freddiemac.housing.service.request;

import java.util.Map;

public abstract class SuggestionRequest {
    public abstract void addRequestAttributes(Map<String,String> requestAttributes);
}