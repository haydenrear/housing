package com.freddiemac.housing.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.model.Indexes;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import org.bson.Document;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
public class MongoConfig extends AbstractReactiveMongoConfiguration{

    @Value("${mongo.connection.string}")
    private String connectionString;

    @Override
    protected String getDatabaseName()
    {
        return "alpaca";
    }


    @Override
    public MongoClient reactiveMongoClient(){
        ConnectionString connString = new ConnectionString(
               connectionString
        );
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connString)
                .retryWrites(true)
                .build();

        MongoClient client = MongoClients.create(settings);
        Flux.from(addGeoIndex(client, "CovariateSuggestionData"))
                .concatWith(addGeoIndex(client, "TargetSuggestionData"))
                .subscribe();
        return client;
    }

    private Publisher<String> addGeoIndex(MongoClient client, String covariateSuggestionData)
    {
        MongoCollection<Document> region = client.getDatabase(getDatabaseName())
                .getCollection(covariateSuggestionData);
        return Mono.from(region.createIndex(Indexes.geo2dsphere("zipPoly")))
                .concatWith(region.createIndex(Indexes.geo2dsphere("location")));
    }


}
