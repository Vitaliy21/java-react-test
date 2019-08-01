package com.react.test.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConfigurationProperties
public class AppConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Value("${mongoDb.uri:#{null}}")
    private String mongoUri;
    public String getMongoUri() {
        return mongoUri;
    }
    public void setMongoUri(String mongoUri) {
        this.mongoUri = mongoUri;
    }

    @Value("${mongoDb.mongoUsersDbName:#{null}}")
    private String mongoUsersDbName;
    public String getMongoUsersDbName() {
        return mongoUsersDbName;
    }
    public void setMongoUsersDbName(String mongoUsersDbName) {
        this.mongoUsersDbName = mongoUsersDbName;
    }

    @Value("${mongoDb.mongoUsersCollectionName:#{null}}")
    private String mongoUsersCollectionName;
    public String getMongoUsersCollectionName() {
        return mongoUsersCollectionName;
    }
    public void setMongoUsersCollectionName(String mongoUsersCollectionName) {
        this.mongoUsersCollectionName = mongoUsersCollectionName;
    }
}
