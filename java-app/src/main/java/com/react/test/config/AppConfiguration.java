package com.react.test.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConfigurationProperties
public class AppConfiguration {

    private RestTemplate restTemplate;
//    ObjectMapper objectMapper;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        this.restTemplate = builder
                .messageConverters(new MappingJackson2HttpMessageConverter())
                .interceptors(new CustomClientHttpRequestInterceptor())
                .build();
        return restTemplate;
    }

//    @Bean
//    public ObjectMapper objectMapper() {
//        this.objectMapper = new ObjectMapper();
//        return objectMapper;
//    }


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

    @Value("${mongoDb.mongoStatementsDbName:#{null}}")
    private String mongoStatementsDbName;
    public RestTemplate getRestTemplate() {
        return restTemplate;
    }
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getMongoStatementsDbName() {
        return mongoStatementsDbName;
    }

    public void setMongoStatementsDbName(String mongoStatementsDbName) {
        this.mongoStatementsDbName = mongoStatementsDbName;
    }
}
