package com.meepleengine.meepleserver.service.mongo;

import com.meepleengine.meepleserver.config.MongoConfig;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Service
public class MongoService extends AbstractMongoClientConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(MongoService.class);

    private final MongoConfig mongoConfig;

    private MongoClient client;

    @Autowired
    public MongoService(MongoConfig mongoConfig) {
        this.mongoConfig = mongoConfig;
    }

    @Override
    public MongoClient mongoClient() {
        client = MongoClients.create(mongoConfig.getConnectionString());
        return client;
    }

    @Bean
    public MongoCollection<Document> userCollection() {
        return createCollection("user");
    }

    @Bean
    public MongoCollection<Document> lobbyCollection() {
        return createCollection("lobby");
    }

    private MongoCollection<Document> createCollection(String name) {
        return mongoClient().getDatabase(mongoConfig.getDatabaseName()).getCollection(name);
    }

    @Override
    protected String getDatabaseName() {
        return mongoConfig.getDatabaseName();
    }

    @PreDestroy
    public void destroy() {
        logger.info("Shutting down MongoDB Connection");
        client.close();
    }
}
