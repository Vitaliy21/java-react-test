package com.react.test.controller;

import com.react.test.dto.UserDto;
import com.react.test.dto.Website;
import com.react.test.service.UserService;
import com.react.test.service.WebsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserReactController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/register")
    public ResponseEntity<Void> addUser(@RequestBody UserDto userDto) {
        userService.addUser(userDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/user/login")
    public ResponseEntity<UserDto> login(@RequestBody UserDto userDto) {
        UserDto user = new UserDto();
        user.setId(1);
        user.setUsername("test user");
        user.setPassword("test password");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/user/search/{username}")
    public ResponseEntity<UserDto> getUser(@PathVariable String username) {
        return new ResponseEntity<>(userService.getUser(username), HttpStatus.OK);
    }

}
