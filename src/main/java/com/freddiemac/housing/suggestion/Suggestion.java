package com.freddiemac.housing.suggestion;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;

public class Suggestion {

    String zip;
    String address;
    String state;
    String city;

    GeoJsonPolygon zipPoly;
    GeoJsonPoint location;

}
