package com.freddiemac.housing.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.connection.SslSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

@Configuration
public class MongoConfig extends AbstractReactiveMongoConfiguration{

    @Override
    protected String getDatabaseName()
    {
        return "alpaca";
    }


    @Override
    public MongoClient reactiveMongoClient(){
        ConnectionString connString = new ConnectionString(
                "mongodb+srv://username:password@alpaca.olrez.mongodb.net/alpaca?retryWrites=true&w=majority"
        );
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connString)
                .retryWrites(true)
                .build();
        return MongoClients.create(settings);
    }


}
