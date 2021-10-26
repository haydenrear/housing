package com.freddiemac.housing.config;

import com.freddiemac.housing.model.*;
import com.freddiemac.housing.repo.CovariateSuggestionRepo;
import com.freddiemac.housing.repo.SuggestionRepo;
import com.freddiemac.housing.repo.TargetSuggestionRepo;
import com.freddiemac.housing.service.CovariateSuggestionDataService;
import com.freddiemac.housing.service.DataApiService;
import com.freddiemac.housing.service.LocationService;
import com.freddiemac.housing.service.TargetSuggestionDataService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Configuration
public class HousingConfig implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Bean("covariate")
    public List<CovariateSuggestionDataService<? extends CovariateSuggestionData, CovariateSuggestionRepo>> covariateDataServices(
            CovariateSuggestionRepo covariateSuggestonRepo
    )
    {
        return List.of(
                createDataService(PopulationDensity.class,covariateSuggestonRepo,CovariateSuggestionDataService.class)
                //Todo: instantiate covariateSuggestionData with more types of SuggestionData extending CovariateSuggestionData
        );
    }

    @Bean("target")
    public List<TargetSuggestionDataService<? extends TargetSuggestionData, TargetSuggestionRepo>> targetSuggestionDataServices(
            TargetSuggestionRepo targetSuggestionRepo
    )
    {
        return List.of(
                createDataService(InternalReturnRateData.class, targetSuggestionRepo, TargetSuggestionDataService.class)
        );
    }

    private <T extends DataApiService<U,V,Z>, U extends SuggestionData, V extends SuggestionRepo<Z>, Z extends SuggestionData> T createDataService(
            Class<U> suggestionDataClzz, V targetSuggestionRepo, Class<T> dataApiService
    )
    {
        return applicationContext.getBean(dataApiService,targetSuggestionRepo,suggestionDataClzz);
    }

    @Override
    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }
}
