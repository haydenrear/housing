package com.freddiemac.housing.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonId;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;

import javax.annotation.PostConstruct;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Objects;

@Data
@Getter
public abstract class SuggestionData {

    @BsonId
    protected String id;

    protected Date date;

    protected GeoJsonPolygon zipPoly;
    protected GeoJsonPoint location;

    protected Double[] data;

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
    public static class DateLocation implements Comparable<Date> {
        Date date;
        String location;

        @Override
        public int compareTo(Date o)
        {
            return o.compareTo(date);
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            DateLocation that = (DateLocation) o;

            if (!Objects.equals(date, that.date)) return false;
            return Objects.equals(location, that.location);
        }

        @Override
        public int hashCode()
        {
            int result = date != null
                    ? date.hashCode()
                    : 0;
            result = 31 * result + (location != null
                    ? location.hashCode()
                    : 0);
            return result;
        }
    }
}
