package com.freddiemac.housing.repo;

import com.freddiemac.housing.model.InternalReturnRateData;
import com.freddiemac.housing.model.TargetSuggestionData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class TargetSuggestionRepoTest {

    @Autowired
    TargetSuggestionRepo targetSuggestionRepo;
    @Autowired
    ApplicationContext ctx;

    @Test
    public void runner()
    {
        InternalReturnRateData data = getInternalReturnRateData();
        StepVerifier.create(targetSuggestionRepo.save(data))
                .assertNext(val -> {
                    assertThat(val.getId()).isNotBlank();
                })
                .thenCancel()
                .verify();
    }

    @Test
    public void test()
    {
        InternalReturnRateData bean = ctx.getBean(InternalReturnRateData.class);
    }

    private InternalReturnRateData getInternalReturnRateData()
    {
        InternalReturnRateData data = new InternalReturnRateData();
        data.setData(new Float[]{1f,2f,3f});
        data.setDate(Date.from(Instant.now()));
        data.setLocation(new GeoJsonPoint(1d, 2d));
        data.setZipPoly(new GeoJsonPolygon(List.of(new GeoJsonPoint(1d, -1d), new GeoJsonPoint(-1d, 1d), new GeoJsonPoint(1d, 1d), new GeoJsonPoint(-1d, -1d))));
        return data;
    }


}
