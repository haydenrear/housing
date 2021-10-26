package com.freddiemac.housing;

import com.freddiemac.housing.model.InternalReturnRateData;
import com.freddiemac.housing.model.PopulationDensity;
import com.freddiemac.housing.model.SuggestionData;
import com.freddiemac.housing.model.TargetSuggestionData;
import com.freddiemac.housing.repo.CovariateSuggestionRepo;
import com.freddiemac.housing.repo.TargetSuggestionRepo;
import com.freddiemac.housing.service.LocationService;
import com.freddiemac.housing.util.DateService;
import com.github.javafaker.Address;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.function.Tuple2;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SpringBootTest
class HousingApplicationTests {

    @Autowired
    CovariateSuggestionRepo covariateSuggestionRepo;
    @Autowired
    TargetSuggestionRepo targetSuggestionRepo;
    @Autowired
    WebClient.Builder builder;
    @Autowired
    ApplicationContext applicationContext;
    private LocationService<TargetSuggestionData> locationService;


    @Test
    void contextLoads()
    {
    }

    @Test
    public void fillDatabaseWithData()
    {
        dateLocations().stream()
                .map(dateLocation -> {
                    return Stream.of();
                });
    }

    InternalReturnRateData internalReturnRateData(SuggestionData.DateLocation dateLocation)
    {
        var irr = new InternalReturnRateData(Math.random() * 10000000 * (Math.random()));
        irr.setDateLocation(dateLocation);
        Optional<Tuple2<GeoJsonPolygon, GeoJsonPoint>> objects = this.locationService.parseData(
                dateLocation.getLocation(), 0);
        irr.setZipPoly(objects.get().getT1());
        irr.setLocation(objects.get().getT2());
        irr.setData();
        return irr;
    }

    PopulationDensity populationDensity(SuggestionData.DateLocation dateLocation)
    {
        var pop = new PopulationDensity(Math.random() * 100);
        pop.setDateLocation(dateLocation);
        pop.setData();
        Optional<Tuple2<GeoJsonPolygon, GeoJsonPoint>> objects = this.locationService.parseData(
                dateLocation.getLocation(), 0);
        pop.setZipPoly(objects.get().getT1());
        pop.setLocation(objects.get().getT2());
        return pop;
    }

    List<SuggestionData.DateLocation> dateLocations()
    {
        List<String> zipcodes = zipcodes(25);
//        List<Date> dates = yearsByMonth(35);
        List<Date> dates = yearsByMonth(30);
        return zipcodes.stream().flatMap(zip -> {
            return dates.stream().flatMap(date -> {
                return Stream.of(new SuggestionData.DateLocation(date, zip));
            });
        }).collect(Collectors.toList());

    }

    @Test
    public void testYearsByMonth()
    {
        List<Date> dates = yearsByMonth(30);
        dates.forEach(System.out::println);
    }

    private List<Date> yearsByMonth(int i)
    {
        List<Date> toAdd = new ArrayList<>();
        IntStream.range(1,i).forEach(val -> {
            AtomicReference<String> value = new AtomicReference<>("2021-01-01");
            value.set(value.get().replace("2021", String.valueOf(2021-val)));
            toAdd.addAll(IntStream.range(1, 13)
                    .mapToObj(month ->{

                        var str = "";
                        if(month >= 10)
                            str = String.valueOf(month);
                        else {
                            str = "0"+String.valueOf(month);
                        }
                        String s = value.get();
                        var splits = s.split("-");
                        return DateService.parseDate(splits[0]+"-"+str+"-"+splits[2]);
                    })
                    .collect(Collectors.toList()));
        });
        return toAdd;
    }

    @Test
    public void testZips()
    {
        System.out.println(zipcodes(10));
        locationService = applicationContext.getBean(LocationService.class, builder, targetSuggestionRepo);
        zipcodes(10).forEach(zip -> {
                            locationService.getDataFromGoogle(zip)
                                    .subscribe(str -> {
                                        Optional<Tuple2<GeoJsonPolygon, GeoJsonPoint>> objects = locationService.parseData(str, 0);
                                        if(objects.isPresent()) {
                                            var objectsFound = objects.get();
                                            System.out.println(objectsFound.getT1());
                                            System.out.println(objectsFound.getT2());
                                        }
                                    });
                        });
    }

    public List<String> zipcodes(int number)
    {
        var locale = new Locale("en-US");
        Faker faker = new Faker(locale);
        return IntStream.range(0,number).mapToObj(i -> {
            Address address = faker.address();
            System.out.println(address);
            return address.zipCode();
        }).collect(Collectors.toList());
    }




}
