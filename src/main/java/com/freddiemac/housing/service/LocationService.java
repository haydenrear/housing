package com.freddiemac.housing.service;

import com.freddiemac.housing.model.SuggestionData;
import com.freddiemac.housing.repo.SuggestionRepo;
import lombok.NoArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Optional;

@Service
@Scope("prototype")
@NoArgsConstructor
public class LocationService<Z extends SuggestionData> {


    @Value("${google.maps.geocode.api.key}")
    String GEOCODEAPIKEY;

    @Value("${google.maps.geocode.api.url}")
    String GEOCODEAPIURL;

    @Value("${google.maps.geocode.api.reverse.url}")
    String GEOCODEREVERSEURL;

    private WebClient.Builder webClient;
    private SuggestionRepo<Z> suggestionRepo;

    @Autowired
    public void setWebClient(WebClient.Builder webClient)
    {
        this.webClient = webClient;
    }


    public LocationService(WebClient.Builder client, SuggestionRepo<Z> suggestionRepo) {
        this.webClient = client;
        this.suggestionRepo = suggestionRepo;
    }

    public Mono<Tuple2<GeoJsonPolygon,GeoJsonPoint>> setPointZip(String zip){
        return getDataFromGoogle(zip)
                .map(response -> parseData(response, 0))
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    public Mono<String> getDataFromGoogle(String byBlank){
        return webClient
                .build()
                .get()
                .uri(GEOCODEAPIURL+byBlank+"&key="+GEOCODEAPIKEY+"&bounds=true")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getDataByReverse(String byBlank){
        System.out.println(GEOCODEREVERSEURL+byBlank+"&key="+GEOCODEAPIKEY);
        return webClient
                .build()
                .get()
                .uri(GEOCODEREVERSEURL+byBlank+"&key="+GEOCODEAPIKEY)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Optional<Tuple2<GeoJsonPolygon, GeoJsonPoint>> parseData(String dataToParse, int latLongIsTwoAddressIsZero){
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject parse = (JSONObject) jsonParser.parse(dataToParse);
            JSONArray array = (JSONArray) parse.get("results");
            if(parse.get("status").equals("ZERO_RESULTS"))
                return Optional.empty();
            if(array == null || array.size() == 0)
                return Optional.empty();
            JSONObject innerObj = (JSONObject) jsonParser.parse(array.get(latLongIsTwoAddressIsZero).toString());
            JSONObject geometry = (JSONObject) jsonParser.parse(innerObj.get("geometry").toString());
            Object boundsFound = geometry.get("bounds");
            Object viewPortFound = geometry.get("viewport");
            JSONObject northeast = null;
            JSONObject southwest = null;
            if(boundsFound != null) {
                JSONObject bounds = (JSONObject) jsonParser.parse(boundsFound.toString());
                northeast = (JSONObject) jsonParser.parse(bounds.get("northeast").toString());
                southwest = (JSONObject) jsonParser.parse(bounds.get("southwest").toString());
            }
            else if  (viewPortFound != null){
                JSONObject viewport = (JSONObject) jsonParser.parse(viewPortFound.toString());
                northeast = (JSONObject) jsonParser.parse(viewport.get("northeast").toString());
                southwest = (JSONObject) jsonParser.parse(viewport.get("southwest").toString());
            }
            else return Optional.empty();
            JSONObject location = (JSONObject) jsonParser.parse(geometry.get("location").toString());
            GeoJsonPoint northeastPoint = new GeoJsonPoint((Double) northeast.get("lng"),(Double) northeast.get("lat"));
            GeoJsonPoint southwestPoint = new GeoJsonPoint((Double) southwest.get("lng"), (Double) southwest.get("lat"));
            GeoJsonPoint locationPoint = new GeoJsonPoint((Double) location.get("lng"),(Double) location.get("lat"));
            double xNorthwest = southwestPoint.getX();
            double yNorthwest = northeastPoint.getY();
            GeoJsonPoint northwestPoint = new GeoJsonPoint(xNorthwest, yNorthwest);
            double xSoutheast = northeastPoint.getX();
            double ySoutheast = southwestPoint.getY();
            GeoJsonPoint finishLoop = new GeoJsonPoint(southwestPoint.getX(), northeastPoint.getY());
            GeoJsonPoint southeastPoint = new GeoJsonPoint(xSoutheast, ySoutheast);
            return Optional.of(Tuples.of(new GeoJsonPolygon(northwestPoint, southwestPoint, southeastPoint, northeastPoint, finishLoop), locationPoint));
        } catch (ParseException ie) {
            ie.printStackTrace();
        }
        return Optional.empty();
    }

    public Flux<Z> findRegionsNearAny(String any) {
        return getDataFromGoogle(any)
                .map(response -> parseData(response, 0))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Tuple2::getT1)
                .flatMapMany(suggestionRepo::findByLocationIsWithin);
    }

    public Flux<Z> findRegionsByLongLat(String longLatitude) {
        return getDataByReverse(longLatitude)
                .map(response -> parseData(response, 2))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Tuple2::getT1)
                .map(this::getCenter)
                .flatMapMany(suggestionRepo::findByLocationIsNearOrderByLocationDesc);
    }

    public GeoJsonPoint getCenter(GeoJsonPolygon geoJsonPolygon){

        double highestX = 10000d;
        double lowestX = -10000d;
        double highestY = 10000d;
        double lowestY = -10000d;

        for(Point point : geoJsonPolygon.getPoints()){
            if(point.getX() < lowestX) {
                lowestX = point.getX();
            }
            if(point.getY() > highestY){
                highestY = point.getY();
            }
            if(point.getX() > highestX) {
                highestX = point.getX();
            }
            if(point.getY() < lowestY){
                lowestY = point.getY();
            }
        }

        double centerX = lowestX + ((highestX - lowestX) / 2);
        double centerY = lowestY + ((highestY - lowestY) / 2);

        return new GeoJsonPoint(centerX, centerY);
    }
}
