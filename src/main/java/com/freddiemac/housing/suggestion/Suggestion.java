package com.freddiemac.housing.suggestion;

import com.freddiemac.housing.model.SuggestionData;
import com.freddiemac.housing.model.TargetSuggestionData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Component
@Scope("prototype")
public class Suggestion {

    String zip;
    String address;
    String state;
    String city;

    GeoJsonPolygon zipPoly;
    GeoJsonPoint location;
    double value;


    public <T extends TargetSuggestionData> Suggestion(T t, String suggestionType)
    {
        this.address = t.getAddress();
        this.location = t.getLocation();
        this.zip = t.getDateLocation().getLocation();
        this.value = t.getData() != null && t.getData().length > 0 ? t.getData()[0] : 0d;
        //Todo:
    }
}
