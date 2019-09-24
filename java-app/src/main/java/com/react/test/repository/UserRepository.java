package com.react.test.repository;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.react.test.config.AppConfiguration;
import com.react.test.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class UserRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepository.class);

    private MongoTemplate mongoTemplate;
    @Autowired
    private AppConfiguration configuration;

    @Autowired
    private void setMongoTemplate() {
        mongoTemplate = new MongoTemplate(new MongoClient(
                new MongoClientURI(configuration.getMongoUri())), configuration.getMongoUsersDbName());
    }

    public void addUser(UserDto userDto) {
        userDto.setId(UUID.randomUUID().toString());
        mongoTemplate.insert(userDto, configuration.getMongoUsersCollectionName());
        LOGGER.info("user has been successfully added");
    }

    public UserDto findByUsername(String username) {
        Query query = Query.query(Criteria.where("username").is(username));
        List<UserDto> result = mongoTemplate.find(query, UserDto.class, configuration.getMongoUsersCollectionName());
        return result.isEmpty() ? null : result.get(0);
    }

    public List<UserDto> getAllUsers() {
        return mongoTemplate.findAll(UserDto.class, configuration.getMongoUsersCollectionName());
    }

    public void updateUser(UserDto userDto) {
        mongoTemplate.save(userDto, configuration.getMongoUsersCollectionName());
    }

    public UserDto findByToken(String token) {
        Query query = Query.query(Criteria.where("token").is(token));
        List<UserDto> result = mongoTemplate.find(query, UserDto.class, configuration.getMongoUsersCollectionName());
        return result.isEmpty() ? null : result.get(0);
    }

    public UserDto findByEmail(String email) {
        Query query = Query.query(Criteria.where("email").is(email));
        List<UserDto> result = mongoTemplate.find(query, UserDto.class, configuration.getMongoUsersCollectionName());
        return result.isEmpty() ? null : result.get(0);
    }
}
