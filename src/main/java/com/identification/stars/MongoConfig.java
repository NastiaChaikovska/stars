package com.identification.stars;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

    @Bean
    public MongoDatabase mongoDatabase() {
        MongoClient mongoClient = MongoClients.create();
        return mongoClient.getDatabase("stars_catalogue");
    }
}

