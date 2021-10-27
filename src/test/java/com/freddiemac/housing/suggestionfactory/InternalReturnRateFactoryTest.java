package com.freddiemac.housing.suggestionfactory;

import com.freddiemac.housing.model.InternalReturnRateData;
import com.freddiemac.housing.model.PopulationDensity;
import com.freddiemac.housing.model.SuggestionData;
import com.freddiemac.housing.suggestion.InternalReturnSuggestionFactory;
import com.freddiemac.housing.suggestion.Suggestion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class InternalReturnRateFactoryTest {

    @Autowired
    InternalReturnSuggestionFactory internalReturnSuggestionFactory;

    @Test
    public void internalReturnRateFactory()
    {
        PopulationDensity populationDensity = new PopulationDensity(1d);
        populationDensity.setData();
        populationDensity.setDateLocation(
                new SuggestionData.DateLocation(Date.from(Instant.ofEpochMilli(1234952L)), "first"));
        PopulationDensity populationDensity1 = new PopulationDensity(2d);
        populationDensity1.setData();
        populationDensity1.setDateLocation(
                new SuggestionData.DateLocation(Date.from(Instant.ofEpochMilli(1234952L).minus(2, ChronoUnit.DAYS)),
                                                "second"));
        PopulationDensity populationDensity2 = new PopulationDensity(3d);
        populationDensity2.setDateLocation(
                new SuggestionData.DateLocation(Date.from(Instant.ofEpochMilli(1234952L).minus(3, ChronoUnit.DAYS)),
                                                "third"));
        populationDensity2.setData();
        InternalReturnRateData internalReturnRateData = new InternalReturnRateData(1d);
        internalReturnRateData.setData();
        internalReturnRateData.setDateLocation(
                new SuggestionData.DateLocation(Date.from(Instant.ofEpochMilli(1234952L)), "first"));
        InternalReturnRateData internalReturnRateData1 = new InternalReturnRateData(2d);
        internalReturnRateData1.setData();
        internalReturnRateData1.setDateLocation(
                new SuggestionData.DateLocation(Date.from(Instant.ofEpochMilli(1234952L).minus(2, ChronoUnit.DAYS)),
                                                "second"));
        InternalReturnRateData internalReturnRateData2 = new InternalReturnRateData(3d);
        internalReturnRateData2.setData();
        internalReturnRateData2.setDateLocation(
                new SuggestionData.DateLocation(Date.from(Instant.ofEpochMilli(1234952L).minus(3, ChronoUnit.DAYS)),
                                                "third"));

        var covar = Flux.just(populationDensity, populationDensity1, populationDensity2);
        var target = Flux.just(internalReturnRateData, internalReturnRateData1, internalReturnRateData2);
        var found = (Flux<Suggestion>) ReflectionTestUtils.invokeMethod(internalReturnSuggestionFactory, "createSuggestionImpl", covar, target);

        StepVerifier.create(found)
                .assertNext(suggestion -> {
                    assertThat(suggestion).isNotNull();
                    System.out.println(suggestion);
                })
                .thenCancel()
                .verify();
    }

}
