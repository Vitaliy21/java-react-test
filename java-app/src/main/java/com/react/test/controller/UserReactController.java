package com.react.test.controller;

import com.react.test.dto.UserDto;
import com.react.test.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserReactController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserReactController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/user/register")
    public ResponseEntity<?> addUser(@RequestBody UserDto userDto) {
        try {
            userService.addUser(userDto);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/user/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
        List<String> result;
        try {
            result = userService.getUserData(userDto);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/user/search/{username}")
    public ResponseEntity<UserDto> getUser(@PathVariable String username) throws Exception {
        return new ResponseEntity<>(userService.getUser(username), HttpStatus.OK);
    }

}
