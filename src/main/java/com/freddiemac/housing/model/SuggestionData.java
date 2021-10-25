package com.freddiemac.housing.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonId;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;

import javax.annotation.PostConstruct;
import java.util.Date;

@Data
@Getter
public abstract class SuggestionData implements Comparable<SuggestionData> {

    @BsonId
    protected String id;

    protected Date date;

    protected GeoJsonPolygon zipPoly;
    protected GeoJsonPoint location;

    protected Float[] data;

    @JsonIgnore
    protected DateLocation dateLocation;

    @PostConstruct
    public abstract void setData();

    public DateLocation dateLocation()
    {
        return this.dateLocation;
    }

    public void setLocationIdentifier(LocationType locationType)
    {
        this.dateLocation = new DateLocation(date, getLocationStringFromPoint(location, locationType));
    }

    protected String getLocationStringFromPoint(GeoJsonPoint location, LocationType locationType)
    {
        //Todo:
        return "20465";
    }

    @Data
    @AllArgsConstructor
    public class DateLocation {
        Date date;
        String location;
    }
}
