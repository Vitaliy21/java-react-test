package com.react.test.service;

import com.react.test.dto.CategoryType;
import com.react.test.dto.StatementResponseDto;
import com.react.test.dto.UserDto;
import com.react.test.repository.StatementRepository;
import com.react.test.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RemoteService remoteService;
    @Autowired
    private StatementRepository statementRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private static final String LOCAL_TIME_FORMAT = "dd-MM-yyyy HH:mm:ss";

    public List<UserDto> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public void addUser(UserDto userDto) throws Exception{
        if (userDto == null || userDto.getUsername() == null || userDto.getToken() == null || userDto.getPassword() == null) {
            throw new Exception("You must provide User details");
        }
        if (userRepository.findByUsername(userDto.getUsername()) != null) {
            throw new Exception("User with that username already exists");
        }
        try {
            userRepository.addUser(userDto);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred during user details saving");
        }
    }

    public UserDto getUser(UserDto userDto) throws Exception {
        if (userDto == null || userDto.getUsername() == null || userDto.getUsername().isEmpty()) {
            throw new Exception("You must provide User details");
        }
        UserDto userFromDb = userRepository.findByUsername(userDto.getUsername());
        if (userFromDb == null) {
            throw new Exception("User with that username not found => " + userFromDb);
        } else if (!userDto.getPassword().equals(userFromDb.getPassword())) {
            throw new Exception("Wrong password");
        } else {
            return userFromDb;
        }
    }

    public Map<CategoryType, BigDecimal> getUserData(String username, String beginDate, String endDate) {
        UserDto userFromDb = userRepository.findByUsername(username);
        long lastLongTime = statementRepository.getLastTimeOfStatement(username);
        long currentLongTime = System.currentTimeMillis() / 1000L;
        String currentTime = String.valueOf(currentLongTime);
        String lastTime;
        if (lastLongTime != 0L) {
            lastTime = String.valueOf(lastLongTime);
        } else {
            lastTime = String.valueOf(currentLongTime - 2682000);
        }

        List<StatementResponseDto> remoteResponse = remoteService.getRemoteUserStatement("0",
                lastTime, currentTime, "uOwuzdeE-0NZ6sZsrC59qyWq3IkWPCb-AF6dIANhPioE");
        LOGGER.info("From remote service has been retrieved new updated statement with size: " + remoteResponse.size());


        //insert remote response to db
        if (remoteResponse != null && !remoteResponse.isEmpty()) {
            mapLocalTime(remoteResponse);
            statementRepository.saveStatements(userFromDb, remoteResponse);
        }

        //transactions selection by specified time range (e.g. statistic for specified month)
        List<StatementResponseDto> localResponse = statementRepository.getStatementInRange(userFromDb.getUsername(), 1564617600L, 1565012959L);
        LOGGER.info("From db has been selected statement with size: " + localResponse.size());

        return fillResult(localResponse);
    }

    private void mapLocalTime(List<StatementResponseDto> remoteResponse) {
        remoteResponse.forEach(elem -> elem.setLocalTime(timestampToLocalTime(elem.getTime(), LOCAL_TIME_FORMAT)));
    }

    private String timestampToLocalTime(Long time, String localTimeFormat) {
        Date date = new Date(time*1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat(localTimeFormat);
        return dateFormat.format(date);
    }

    private Map<CategoryType, BigDecimal> fillResult(List<StatementResponseDto> localResponse) {
        Map<CategoryType, BigDecimal> result = new LinkedHashMap<>();
        List<StatementResponseDto> resultList = localResponse.stream()
                //filter positive transactions. should be counted spending(negative) transactions only
                .filter(elem -> elem.getAmount()<0)
                .collect(Collectors.toList());
        for (StatementResponseDto element : resultList) {
            BigDecimal amount = BigDecimal.valueOf(Math.abs(element.getAmount())).divide(new BigDecimal(100));
            if (result.get(element.getCategoryType()) == null) {
                result.put(element.getCategoryType(), amount);
            } else {
                result.put(element.getCategoryType(), result.get(element.getCategoryType()).add(amount));
            }
        }
        return result;
    }

}
