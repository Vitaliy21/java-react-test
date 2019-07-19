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
    public ResponseEntity<Void> addUser(@RequestBody UserDto userDto) throws Exception {
        userService.addUser(userDto);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping("/user/search/{username}")
    public ResponseEntity<UserDto> getUser(@PathVariable String username) throws Exception {
        return new ResponseEntity<UserDto>(userService.getUser(username), HttpStatus.OK);
    }

}
