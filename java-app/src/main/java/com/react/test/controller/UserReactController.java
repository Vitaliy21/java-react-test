package com.react.test.controller;

import com.react.test.dto.CategoryType;
import com.react.test.dto.UserDto;
import com.react.test.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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

    @PostMapping(value = "/user/login", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
        UserDto result;
        try {
            result = userService.getUser(userDto);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/user/details/{username}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> details(@PathVariable String username) {
        Map<CategoryType, BigDecimal> result;
        String beginDate = "1-08-2019";
        String endDate = "5-08-2019";
        try {
            result = userService.getUserData(username, beginDate, endDate);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
