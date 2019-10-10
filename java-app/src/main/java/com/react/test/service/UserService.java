package com.react.test.service;

import com.react.test.dto.*;
import com.react.test.repository.StatementRepository;
import com.react.test.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
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
    private static final Map<String, Long> LAST_USERS_CALLS = new HashMap<>();

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
        if (userRepository.findByToken(userDto.getToken()) != null) {
            throw new Exception("User with that token already exists");
        }
        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            throw new Exception("User with that email already exists");
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

            //get updated data from remote if last calling was later then 1 min
            List<StatementResponseDto> remoteResponse = new ArrayList<>();
            if (LAST_USERS_CALLS.get(userDto.getUsername()) != null && System.currentTimeMillis() - LAST_USERS_CALLS.get(userDto.getUsername()) < 1000 * 60) {
                LOGGER.info("skipped remote call as less then 1 min after last calling");
            } else {
                List<StatementResponseDto> remoteUserStatements;
                try {
                    remoteUserStatements = remoteService.getRemoteUserStatement("0", lastTime, currentTime, userFromDb.getToken());
                    LAST_USERS_CALLS.put(userDto.getUsername(), System.currentTimeMillis());
                } catch (Exception e) {
                    LOGGER.error("Remote calling failed. Cause: " + e);
                    throw new Exception("Wrong Token");
                }
                remoteResponse.addAll(remoteUserStatements);
                LOGGER.info("From remote service has been retrieved new updated statement with size: " + remoteResponse.size());
            }

            //insert remote response to db
            if (!remoteResponse.isEmpty()) {
                mapLocalTime(remoteResponse);
                statementRepository.saveStatements(userFromDb, remoteResponse);
            }
            return userFromDb;
        }
    }

    public List<UserDetailsDto> getUserData(String username) {

        List<UserDetailsDto> result = new ArrayList<>();

        UserDto userFromDb = userRepository.findByUsername(username);

        YearMonth now = YearMonth.now();
        LocalDate currentStartDay = now.atDay(1);
        LocalDate currentEndDay = now.atEndOfMonth();
        LocalDate previousStart = now.minusMonths(1).atDay(1);
        LocalDate previousEnd = now.atDay(1).minusDays(1);

        Timestamp currentMonthBeginDate = Timestamp.valueOf(currentStartDay.atStartOfDay());
        Timestamp currentMonthEndDate = Timestamp.valueOf(currentEndDay.atTime(23, 59));
        Timestamp previousMonthBeginDate = Timestamp.valueOf(previousStart.atStartOfDay());
        Timestamp previousMonthEndDate = Timestamp.valueOf(previousEnd.atTime(23, 59));

        //transactions selection by specified time range (e.g. statistic for specified month)
        List<StatementResponseDto> localResponseForCurrent = statementRepository.getStatementInRange(userFromDb.getUsername(),
                currentMonthBeginDate.getTime()/1000, currentMonthEndDate.getTime()/1000);
        List<StatementResponseDto> localResponseForPrevious = statementRepository.getStatementInRange(userFromDb.getUsername(),
                previousMonthBeginDate.getTime()/1000, previousMonthEndDate.getTime()/1000);
        LOGGER.info("From db has been selected statement for current month with size: " + localResponseForCurrent.size());
        LOGGER.info("From db has been selected statement for previous month with size: " + localResponseForPrevious.size());

        Map<String, BigDecimal> mapCurrent = fillResult(localResponseForCurrent);
        Map<String, BigDecimal> mapPrevious = fillResult(localResponseForPrevious);

        Set<String> categoriesByUsername = sortForView(getCategoriesByUsername(username));
        categoriesByUsername.forEach(e-> result.add(new UserDetailsDto(e, mapCurrent.get(e), mapPrevious.get(e))));

        return result;
    }

    private Set<String> sortForView(Set<String> categoriesByUsername) {
        Set<String> sorted = categoriesByUsername.stream().collect(Collectors.toCollection(TreeSet::new));
        Set<String> result = new LinkedHashSet<>(sorted);
        result.add("UNDEFINED");
        return result;
    }

    private void mapLocalTime(List<StatementResponseDto> remoteResponse) {
        remoteResponse.forEach(elem -> elem.setLocalTime(timestampToLocalTime(elem.getTime(), LOCAL_TIME_FORMAT)));
    }

    private String timestampToLocalTime(Long time, String localTimeFormat) {
        Date date = new Date(time*1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat(localTimeFormat);
        return dateFormat.format(date);
    }

    private Map<String, BigDecimal> fillResult(List<StatementResponseDto> localResponse) {
        Map<String, BigDecimal> result = new LinkedHashMap<>();
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
        List<StatementResponseDto> merchantsByCategory = statementRepository.getMerchantsByCategory(username, category);
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

    public void updateCategory(UserCategoryDto userCategoryDto) {
        UserDto userDto = userRepository.findByUsername(userCategoryDto.getUsername());
        userDto.getCategories().put(userCategoryDto.getMerchant().replace(".", ""), userCategoryDto.getCategory());
        userRepository.updateUser(userDto);

        statementRepository.updateCategoryForMerchant(userCategoryDto.getUsername(), userCategoryDto.getMerchant(), userCategoryDto.getCategory());
    }

    public void createCategory(UserCategoryDto userCategoryDto) {
        UserDto userDto = userRepository.findByUsername(userCategoryDto.getUsername());
        userDto.getNewCategories().add(userCategoryDto.getCategory());

        userRepository.updateUser(userDto);
    }

    public Set<String> getCategoriesByUsername(String username) {
        UserDto user = userRepository.findByUsername(username);
        List<String> result = new ArrayList<>(user.getCategories().values());
        result.addAll(user.getNewCategories());
        return new HashSet<>(result);
    }
}
