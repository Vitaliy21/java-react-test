package com.react.test.repository;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.react.test.config.AppConfiguration;
import com.react.test.dto.CategoryType;
import com.react.test.dto.StatementResponseDto;
import com.react.test.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class StatementRepository {

    @Autowired
    private AppConfiguration configuration;
    private MongoTemplate mongoTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(StatementRepository.class);

    @Autowired
    public void setMongoTemplate() {
        mongoTemplate = new MongoTemplate(new MongoClient(
                new MongoClientURI(configuration.getMongoUri())), configuration.getMongoStatementsDbName());
    }

    public void saveStatements(UserDto user, List<StatementResponseDto> newStatements) {
        Map<String, CategoryType> categories = user.getCategories();
//        newStatements.stream().forEach(transaction -> {
//            if(categories.get(transaction.getDescription()) == null) {
//                transaction.setCategoryType(CategoryType.UNDEFINED);
//            } else {
//                transaction.setCategoryType(categories.get(transaction.getDescription()));
//            }
//        });
        for (StatementResponseDto transaction : newStatements) {
            if(categories.get(transaction.getDescription()) == null) {
                transaction.setCategoryType(CategoryType.UNDEFINED);
            } else {
                transaction.setCategoryType(categories.get(transaction.getDescription()));
            }
        }
        mongoTemplate.insert(newStatements, user.getUsername() + "_statements");
        LOGGER.info("statements has been successfully added");
    }

    public List<StatementResponseDto> getStatementInRange(String username, String beginDate, String endDate) {
        Query query = Query.query(Criteria.where("username").is(username))
                .addCriteria(Criteria.where("time").gte(beginDate))
                .addCriteria(Criteria.where("time").lte(endDate));
        List<StatementResponseDto> result = mongoTemplate.find(query, StatementResponseDto.class, username + "_statements");
        return result;
    }
}
