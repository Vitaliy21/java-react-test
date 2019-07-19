package com.react.test.repository;

import com.react.test.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserRepository {
    private static final Map<String, UserDto> USERS = new HashMap<>();
    private static Integer counter = 0;

    public void addUser(UserDto userDto) {
        counter++;
        userDto.setId(counter);
        USERS.put(userDto.getUsername(), userDto);
    }

    public UserDto findByUsername(String username) {
        return USERS.get(username);
    }
}
