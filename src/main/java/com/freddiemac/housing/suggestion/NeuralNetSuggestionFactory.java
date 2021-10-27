package com.freddiemac.housing.suggestion;

import com.freddiemac.housing.model.CovariateSuggestionData;
import com.freddiemac.housing.model.SuggestionData;
import com.freddiemac.housing.model.TargetSuggestionData;
import com.freddiemac.housing.repo.SuggestionRepo;
import com.freddiemac.housing.service.CovariateSuggestionDataService;
import com.freddiemac.housing.service.TargetSuggestionDataService;
import com.freddiemac.housing.service.request.RequestBuilder;
//import org.deeplearning4j.nn.api.OptimizationAlgorithm;
//import org.deeplearning4j.nn.conf.GradientNormalization;
//import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
//import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
//import org.deeplearning4j.nn.conf.layers.FeedForwardLayer;
//import org.deeplearning4j.nn.conf.layers.LSTM;
//import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
//import org.deeplearning4j.nn.weights.WeightInit;
//import org.nd4j.linalg.activations.Activation;
//import org.nd4j.linalg.api.buffer.DataType;
//import org.nd4j.linalg.learning.config.Nesterovs;
//import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

//@Component
public class NeuralNetSuggestionFactory extends SuggestionFactory<CovariateTarget> {

    public NeuralNetSuggestionFactory(
            @Qualifier("target") List<TargetSuggestionDataService<? extends SuggestionData, ? extends SuggestionRepo<? extends SuggestionData>>> targetSuggestionDataServices,
            @Qualifier("covariate") List<CovariateSuggestionDataService<? extends SuggestionData, ? extends SuggestionRepo<? extends SuggestionData>>> covariateSuggestionDataServices)
    {
        super(targetSuggestionDataServices, covariateSuggestionDataServices);
    }

    public Flux<Suggestion> createSuggestion(SuggestionMetadata suggestionMetadata)
    {
        var covariatesFlux = Flux.fromIterable(covariateSuggestionDataServices)
                .map(covariateSuggestionDataService -> covariateSuggestionDataService.getData(suggestionMetadata).cast(CovariateSuggestionData.class));
        var targetsFlux = Flux.fromIterable(targetSuggestionDataServices)
                .map(targetSuggestionDataService -> targetSuggestionDataService.getData(suggestionMetadata).cast(TargetSuggestionData.class));
        return createSuggestion(covariatesFlux, targetsFlux);
    }

    protected  <V extends CovariateSuggestionData, U extends TargetSuggestionData> Flux<Suggestion> createSuggestion(Flux<Flux<V>> covariatesFlux, Flux<Flux<U>> targetsFlux)
    {

        SortedMap<SuggestionData.DateLocation, CovariateTarget> covariateTargetMap = new TreeMap<>();

        var covariatesGrouped = groupFluxByDateLocation(covariatesFlux);
        var targetsGrouped = groupFluxByDateLocation(targetsFlux);
        var covariates = extracted(covariateTargetMap, covariatesGrouped);
        var targets = extracted(covariateTargetMap, targetsGrouped);
        var total = Flux.concat(covariates,targets);

        return total.takeLast(1)
                .flatMap(this::createSuggestions);

    }

    private <V extends SuggestionData> Flux<Map<SuggestionData.DateLocation, List<V>>> groupFluxByDateLocation(
            Flux<Flux<V>> covariatesFlux
    )
    {
        return covariatesFlux.flatMap(covariateFlux -> covariateFlux.collect(Collectors.groupingBy(SuggestionData::dateLocation)));
    }

    protected  <U extends SuggestionData> Mono<SortedMap<SuggestionData.DateLocation, CovariateTarget>> extracted(
            SortedMap<SuggestionData.DateLocation, CovariateTarget> covariateTargetMap,
            Flux<Map<SuggestionData.DateLocation, List<U>>> covariatesGrouped
    )
    {
        return covariatesGrouped.map(covariateMap -> {
            covariateMap.forEach((key1, value) -> covariateTargetMap.compute(key1, (key, prev) -> {
                if(prev == null)
                    prev = emptyCovariateTarget(key);
                if (prev.covariates == null)
                    prev.covariates = new ArrayList<>();
                prev.covariates.add(value);
                return prev;
            }));
            return covariateMap;
        }).then(Mono.just(covariateTargetMap));
    }

    @Override
    protected CovariateTarget emptyCovariateTarget(SuggestionData.DateLocation key)
    {
        return null;
    }

     @Override
    protected Flux<Suggestion> createSuggestions(SortedMap<SuggestionData.DateLocation, CovariateTarget> data)
    {
        return null;
    }

    private <T extends CovariateSuggestionData, U extends TargetSuggestionData> List<Suggestion> createSuggestion(T[][] covariates, U[][] targets)
    {
       return null;
    }



//    public MultiLayerConfiguration nnConfig(int batchSize, int nIn)
//    {
//        return NNConfig(
//                OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT,
//                WeightInit.XAVIER,
//                new Nesterovs(0.005),
//                GradientNormalization.ClipElementWiseAbsoluteValue, 0.5,
//                List.of(
//                        new LSTM.Builder().activation(Activation.TANH).nIn(2).nOut(batchSize),
//                        new LSTM.Builder().activation(Activation.TANH).nIn(batchSize).nOut(batchSize),
//                        new RnnOutputLayer.Builder(LossFunctions.LossFunction.MSE)
//                                .activation(Activation.SOFTMAX).nIn(batchSize).nOut(1)
//                )
//        );
//    }

//    public MultiLayerConfiguration NNConfig(
//            OptimizationAlgorithm gradientDescent,
//            WeightInit weightInit,
//            Nesterovs updater,
//            GradientNormalization gradientNorm,
//            double gradientNormThreshold,
//            List<FeedForwardLayer.Builder<?>> layers
//    )
//    {
//        var nn = new NeuralNetConfiguration.Builder()
//                .seed(123)    //Random number generator seed for improved repeatability. Optional.
//                .dataType(DataType.DOUBLE)
//                .optimizationAlgo(gradientDescent)
//                .weightInit(weightInit)
//                .updater(updater)
//                .gradientNormalization(gradientNorm)  //Not always required, but helps with this data set
//                .gradientNormalizationThreshold(gradientNormThreshold)
//                .list();
//        int counter = 0;
//        for (var layer : layers){
//            nn.layer(counter, layer.build());
//            ++counter;
//        }
//        return nn.build();
//    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {

    }
}
