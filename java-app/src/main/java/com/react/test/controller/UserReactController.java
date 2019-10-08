package com.react.test.controller;

import com.react.test.dto.CategoryType;
import com.react.test.dto.UserCategoryDto;
import com.react.test.dto.UserDto;
import com.react.test.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @GetMapping(value = "/user/details/{username}/{beginDate}/{endDate}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> details(@PathVariable String username, @PathVariable String beginDate, @PathVariable String endDate) {
        Map<CategoryType, BigDecimal> result;
        try {
            result = userService.getUserData(username, beginDate, endDate);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/user/search/{username}/{category}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> merchants(@PathVariable String username, @PathVariable String category) {
        List<String> merchants = userService.getMerchantsByCategory(username, category);
        return new ResponseEntity<>(merchants, HttpStatus.OK);
    }

    @PostMapping("/user/updateCategory")
    public ResponseEntity<?> updateWebsite(@RequestBody UserCategoryDto userCategoryDto) {
        userService.updateCategory(userCategoryDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/user/categories/{username}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> categories(@PathVariable String username) {
        Set<CategoryType> result;
        try {
            result = userService.getCategoriesByUsername(username);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }

}
