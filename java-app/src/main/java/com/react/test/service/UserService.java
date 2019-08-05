package com.react.test.service;

import com.react.test.dto.CategoryType;
import com.react.test.dto.StatementResponseDto;
import com.react.test.dto.UserDto;
import com.react.test.repository.StatementRepository;
import com.react.test.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Map<CategoryType, Double> getUserData(String username, String beginDate, String endDate) throws Exception {
        UserDto userFromDb = userRepository.findByUsername(username);

        List<StatementResponseDto> remoteResponse = remoteService.getRemoteUserStatement("0",
                "1562420985", "1565012959", "uOwuzdeE-0NZ6sZsrC59qyWq3IkWPCb-AF6dIANhPioE");

        //insert remote response to db
        statementRepository.saveStatements(userFromDb, remoteResponse);

        //get from beginDate to endDate
        List<StatementResponseDto> localResponse = statementRepository.getStatementInRange(userFromDb.getUsername(), "1564617600", "1565012959");

        return fillResult(localResponse);
    }

    private Map<CategoryType, Double> fillResult(List<StatementResponseDto> localResponse) {
        Map<CategoryType, Double> result = new LinkedHashMap<>();
        List<StatementResponseDto> resultList = localResponse.stream()
                .filter(elem -> elem.getAmount()<0)
                .collect(Collectors.toList());
        resultList.stream().forEach(elem -> result.put(elem.getCategoryType(), result.get(elem.getCategoryType()) + elem.getAmount()));

        return result;
    }

}
