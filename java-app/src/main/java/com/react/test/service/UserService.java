package com.react.test.service;

import com.react.test.dto.UserDto;
import com.react.test.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

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

    public UserDto getUser(final String username) throws Exception {
        if (username == null || username.isEmpty()) {
            throw new Exception("You must provide User details");
        }
        UserDto user = userRepository.findByUsername(username);
        if (user == null) {
            throw new Exception("User with that username not found => " + username);
        } else {
            return user;
        }
    }

    public List<String> getUserData(UserDto user) throws Exception {
        List<String> result = new ArrayList<>(Arrays.asList("item1", "item2", "item3"));
        UserDto userFromDb = getUser(user.getUsername());

        if (!user.getPassword().equals(userFromDb.getPassword())) {
            throw new Exception("Wrong password");
        }

        return result;
    }

}
