package com.freddiemac.housing.suggestion;

import com.freddiemac.housing.model.SuggestionData;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;

public class Suggestion {

    String zip;
    String address;
    String state;
    String city;

    GeoJsonPolygon zipPoly;
    GeoJsonPoint location;


    public <T extends SuggestionData> Suggestion(T t, String suggestionType)
    {

        //Todo:
    }
}
