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
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    LocationService<TargetSuggestionData> locationService;

    @BeforeEach
    void beforeAll()
    {
        this.locationService = applicationContext.getBean(LocationService.class, builder, targetSuggestionRepo);
    }

    @Test
    void contextLoads()
    {
    }

//    @Test
    public void fillDatabaseWithData() throws InterruptedException
    {
        dateLocations().flatMap(dateLocation -> {
                    var target = internalReturnRateData(dateLocation.getT1(), dateLocation.getT2())
                            .flatMap(targetSuggestionRepo::save);
                    System.out.println(dateLocation.getT1());
                    var covariate = populationDensity(dateLocation.getT1(), Tuples.of(dateLocation.getT2().getT1(), dateLocation.getT2().getT2()))
                            .flatMap(covariateSuggestionRepo::save);
                    return Flux.concat(target,covariate);
                })
                .subscribe();
        Thread.sleep(100000);
    }

    Flux<Tuple2<SuggestionData.DateLocation, Tuple2<GeoJsonPolygon, GeoJsonPoint>>> dateLocationsFrom()
    {
        return this.targetSuggestionRepo.findAll()
                .map(val -> Tuples.of(val.getDateLocation(), Tuples.of(val.getZipPoly(), val.getLocation())))
                .distinct();
    }

    Mono<InternalReturnRateData> internalReturnRateData(SuggestionData.DateLocation dateLocation, Tuple2<GeoJsonPolygon, GeoJsonPoint> t2)
    {
        var irr = new InternalReturnRateData(Math.random() * 10000000 * (Math.random()));
        irr.setDateLocation(dateLocation);
        return this.locationService.getDataFromGoogle(dateLocation.getLocation())
                .map(toParse -> {
                    irr.setZipPoly(t2.getT1());
                    irr.setLocation(t2.getT2());
                    irr.setAddress(address(dateLocation.getLocation()));
                    irr.setDateLocation(dateLocation);
                    irr.setData();
                    return irr;
                });
    }

    Mono<PopulationDensity> populationDensity(SuggestionData.DateLocation dateLocation,
                                              Tuple2<GeoJsonPolygon, GeoJsonPoint> t2)
    {
        var pop = new PopulationDensity(Math.random() * 100);
        pop.setDateLocation(dateLocation);
        return this.locationService.getDataFromGoogle(dateLocation.getLocation())
                .map(toParse -> {
                    pop.setZipPoly(t2.getT1());
                    pop.setLocation(t2.getT2());
                    pop.setData();
                    return pop;
                });
    }

    Flux<Tuple2<SuggestionData.DateLocation, Tuple2<GeoJsonPolygon, GeoJsonPoint>>> dateLocations()
    {
        List<String> zipcodes = zipcodes(5);
//        List<Date> dates = yearsByMonth(35);
        List<Date> dates = yearsByMonth(10);
        return Flux.fromIterable(zipcodes).flatMap(zip -> this.locationService.getDataFromGoogle(zip)
                .map(toParse -> this.locationService.parseData(toParse, 0))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .flatMapMany(tuple -> Flux.fromIterable(dates).map(date -> Tuples.of(new SuggestionData.DateLocation(date, zip), tuple)))
        );

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

    @Test
    public void testAddress()
    {
        System.out.println(address("20904"));
    }

    public String address(String zip)
    {
        Faker faker = new Faker();
        Address address = faker.address();
        String s = address.fullAddress();
        var items = s.split(",");
        var last = items[items.length-1];
        String[] s1 = last.strip().split(" ");
        var state = s1[0];
        items = Arrays.copyOf(items, items.length -1);
        var toReturn = StringUtils.join(items, "")+" "+state;
        return toReturn + " " +zip;
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
