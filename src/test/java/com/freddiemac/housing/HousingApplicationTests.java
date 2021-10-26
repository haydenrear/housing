package com.freddiemac.housing;

import com.freddiemac.housing.model.TargetSuggestionData;
import com.freddiemac.housing.repo.CovariateSuggestionRepo;
import com.freddiemac.housing.repo.TargetSuggestionRepo;
import com.freddiemac.housing.service.LocationService;
import com.github.javafaker.Address;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.function.Tuple2;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
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


    @Test
    void contextLoads()
    {
        IntStream.range(0,150).forEach(i -> {
//            createPopulationDensity(i)
        });
    }

    @Test
    public void testZips()
    {
        System.out.println(zipcodes(10));
        LocationService<TargetSuggestionData> locationService = applicationContext.getBean(LocationService.class, builder, targetSuggestionRepo);
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
