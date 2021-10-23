package com.freddiemac.housing.repo;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.NoRepositoryBean;
import reactor.core.publisher.Flux;

@NoRepositoryBean
public interface SuggestionRepo<T> extends ReactiveMongoRepository<T,String> {
    Flux<T> findByLocationIsWithin(GeoJsonPolygon geoJsonPolygon);
    Flux<T> findByLocationIsNearOrderByLocationDesc(GeoJsonPoint point);
}
