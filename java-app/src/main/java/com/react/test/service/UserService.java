package com.react.test.service;

import com.react.test.dto.CategoryType;
import com.react.test.dto.StatementResponseDto;
import com.react.test.dto.UserCategoryDto;
import com.react.test.dto.UserDto;
import com.react.test.repository.StatementRepository;
import com.react.test.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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
            long lastLongTime = statementRepository.getLastTimeOfStatement(userFromDb.getUsername());
            long currentLongTime = System.currentTimeMillis() / 1000L;
            String currentTime = String.valueOf(currentLongTime);
            String lastTime;
            if (lastLongTime != 0L) {
                lastTime = String.valueOf(lastLongTime);
            } else {
                lastTime = String.valueOf(currentLongTime - 2682000);
            }

            //get updated data from remote
            List<StatementResponseDto> remoteResponse = remoteService.getRemoteUserStatement("0",
                    lastTime, currentTime, "uOwuzdeE-0NZ6sZsrC59qyWq3IkWPCb-AF6dIANhPioE");
            LOGGER.info("From remote service has been retrieved new updated statement with size: " + remoteResponse.size());

            //insert remote response to db
            if (!remoteResponse.isEmpty()) {
                mapLocalTime(remoteResponse);
                statementRepository.saveStatements(userFromDb, remoteResponse);
            }
            return userFromDb;
        }
    }

    public Map<CategoryType, BigDecimal> getUserData(String username, String beginDate, String endDate) {
        UserDto userFromDb = userRepository.findByUsername(username);

        //transactions selection by specified time range (e.g. statistic for specified month)
        List<StatementResponseDto> localResponse = statementRepository.getStatementInRange(userFromDb.getUsername(),
                localTimeToTimesamp(beginDate).getTime()/1000, localTimeToTimesamp(endDate).getTime()/1000);
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

    private Timestamp localTimeToTimesamp(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
        Timestamp timestamp = null;
        try {
            Date parsedDate = dateFormat.parse(time);
            timestamp = new java.sql.Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    //TODO: upgrade date with localDateTime:
//    LocalDateTime  localDate = LocalDateTime.now();//For reference
//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
//    String formattedString = localDate.format(formatter);
//    localDate.withDayOfMonth(1);

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

    public List<String> getMerchantsByCategory(String username, String category) {
        List<String> result = new ArrayList<>();
        CategoryType categoryType = getCategory(category);
        List<StatementResponseDto> merchantsByCategory = statementRepository.getMerchantsByCategory(username, categoryType);
        Map<String, BigDecimal> calculatedMerchants = calculateByMerchantName(merchantsByCategory);
        calculatedMerchants.forEach((key, value) -> {
            //TODO: for now no need calculate prices
//            result.add(key + "    :    " + value + "  UAH");
            result.add(key);
        });
        return result;
    }

    private Map<String, BigDecimal> calculateByMerchantName(List<StatementResponseDto> merchantsByCategory) {
        Map<String, BigDecimal> result = new LinkedHashMap<>();
        List<StatementResponseDto> filteredMerchants = merchantsByCategory.stream()
                .filter(elem -> elem.getAmount() < 0)
                .collect(Collectors.toList());
        for (StatementResponseDto element : filteredMerchants) {
            BigDecimal amount = BigDecimal.valueOf(Math.abs(element.getAmount())).divide(new BigDecimal(100));
            if (result.get(element.getDescription()) == null) {
                result.put(element.getDescription(), amount);
            } else {
                result.put(element.getDescription(), result.get(element.getDescription()).add(amount));
            }
        }
        return result;
    }

    private CategoryType getCategory(String category) {
        switch (category) {
            case "OTHER": return CategoryType.OTHER;
            case "FUN": return CategoryType.FUN;
            case "TAXI": return CategoryType.TAXI;
            case "SUPERMARKETS": return CategoryType.SUPERMARKETS;
            case "RESTAURANTS": return CategoryType.RESTAURANTS;
            default: return CategoryType.UNDEFINED;
        }
    }

    public void updateCategory(UserCategoryDto userCategoryDto) {
        UserDto userDto = userRepository.findByUsername(userCategoryDto.getUsername());
        userDto.getCategories().put(userCategoryDto.getMerchant(), userCategoryDto.getCategory());
        userRepository.updateUser(userDto);

        statementRepository.updateCategoryForMerchant(userCategoryDto.getUsername(), userCategoryDto.getMerchant(), userCategoryDto.getCategory());
    }
}
