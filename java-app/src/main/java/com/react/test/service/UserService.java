package com.react.test.service;

import com.react.test.dto.UserDto;
import com.react.test.dto.Website;
import com.react.test.repository.UserRepository;
import com.react.test.repository.WebsiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void addUser(UserDto userDto) {
        if (userDto == null || (userDto.getUsername() == null && userDto.getToken() == null && userDto.getPassword() == null)) {
            throw new RuntimeException("You must provide User details");
        }
        try {
            userRepository.addUser(userDto);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred during user details saving");
        }
    }

    public UserDto getUser(final String username) {
        if (username == null || username.isEmpty()) {
            throw new RuntimeException("You must provide valid website id");
        }
        UserDto user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("Users detail not found for the given username => " + username);
        } else {
            return user;
        }
    }
}
