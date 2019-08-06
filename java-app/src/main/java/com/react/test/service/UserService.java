package com.react.test.service;

import com.react.test.dto.CategoryType;
import com.react.test.dto.StatementResponseDto;
import com.react.test.dto.UserDto;
import com.react.test.repository.StatementRepository;
import com.react.test.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

        //insert remote response to db
        if (remoteResponse != null && !remoteResponse.isEmpty()) {
            statementRepository.saveStatements(userFromDb, remoteResponse);
        }

        //get from beginDate to endDate
        List<StatementResponseDto> localResponse = statementRepository.getStatementInRange(userFromDb.getUsername(), 1564617600L, 1565012959L);

        return fillResult(localResponse);
    }

    private Map<CategoryType, BigDecimal> fillResult(List<StatementResponseDto> localResponse) {
        Map<CategoryType, BigDecimal> result = new LinkedHashMap<>();
        List<StatementResponseDto> resultList = localResponse.stream()
                .filter(elem -> elem.getAmount()<0)
                .collect(Collectors.toList());
        for (StatementResponseDto element : resultList) {
            BigDecimal amount = BigDecimal.valueOf(Math.abs(element.getAmount())/100);
            if (result.get(element.getCategoryType()) == null) {
                result.put(element.getCategoryType(), amount);
            } else {
                result.put(element.getCategoryType(), result.get(element.getCategoryType()).add(amount));
            }
        }
        return result;
    }

}
