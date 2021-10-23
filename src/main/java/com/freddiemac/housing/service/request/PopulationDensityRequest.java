package com.freddiemac.housing.service.request;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

public class PopulationDensityRequest extends SuggestionRequest {

    public String location;
    Date date;

    @Override
    public PopulationDensityRequest addRequestAttributes(Map<String, String> requestAttributes)
    {
        this.location = requestAttributes.get("location");
        this.date = Date.from(Instant.ofEpochMilli(Long.parseLong(requestAttributes.get("date"))));
        return this;
    }
}
