package com.react.test.repository;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.react.test.config.AppConfiguration;
import com.react.test.dto.StatementResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public void saveStatements(String username, List<StatementResponseDto> newStatements) {
        mongoTemplate.insert(newStatements, username + "_statements");
        LOGGER.info("statements has been successfully added");
    }

    public List<StatementResponseDto>  getStatementsAfterDate(String username, String beginDate) {
        Query query = Query.query(Criteria.where("username").is(username))
                .addCriteria(Criteria.where("date").gte(beginDate));
        List<StatementResponseDto> result = mongoTemplate.find(query, StatementResponseDto.class, username + "_statements");
        return result;
    }
}
