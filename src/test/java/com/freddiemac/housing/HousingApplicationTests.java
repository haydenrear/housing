package com.freddiemac.housing;

import com.freddiemac.housing.repo.CovariateSuggestionRepo;
import com.freddiemac.housing.repo.TargetSuggestionRepo;
import com.freddiemac.housing.service.LocationService;
import com.github.javafaker.Address;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Locale;
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
        var locationService = applicationContext.getBean(LocationService.class, builder, targetSuggestionRepo);
        locationService.getDataFromGoogle("20904")
                .subscribe(str -> {
                    System.out.println(str);
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
