package com.freddiemac.housing.model;


import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;

import javax.annotation.PostConstruct;
import java.util.Date;

@Data
public abstract class SuggestionData {

    @BsonId
    protected String id;

    protected Date date;

    protected GeoJsonPolygon zipPoly;
    protected GeoJsonPoint location;

    protected Float[] data;

    @PostConstruct
    public abstract void setData();

}
