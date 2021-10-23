package com.freddiemac.housing.service.request;

import lombok.SneakyThrows;

public class SuggestionRequestFactory {
    @SneakyThrows
    public static <T extends SuggestionRequest> T CreateSuggestionRequest(Class<T> clzz)
    {
        return clzz.getConstructor(null).newInstance(null);
    }
}
