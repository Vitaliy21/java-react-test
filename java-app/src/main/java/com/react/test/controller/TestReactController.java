package com.react.test.controller;

import com.react.test.dto.Website;
import com.react.test.service.WebsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@RestController
public class TestReactController {

    @GetMapping("/testOne")
    ResponseEntity<?> testOne() {
        String repoResponce = "test one";
        return new ResponseEntity<>(repoResponce, HttpStatus.OK);
    }


    @Autowired
    private WebsiteService websiteService;
    @GetMapping("/websites")
    public ResponseEntity<List<Website>> getWebsites() throws Exception {
        return new ResponseEntity<List<Website>>(websiteService.getWebsites(), HttpStatus.OK);
    }
    @GetMapping("/website/search/{id}")
    public ResponseEntity<Website> getWebsite(@PathVariable Integer id) throws Exception {
        return new ResponseEntity<Website>(websiteService.getWebsite(id), HttpStatus.OK);
    }
    @PostMapping("/website/add")
    public ResponseEntity<Void> saveWebsite(@RequestBody Website website) throws Exception {
        websiteService.saveWebsite(website);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
    @PostMapping("/website/update")
    public ResponseEntity<Void> updateWebsite(@RequestBody Website website) throws Exception {
        websiteService.updateWebsite(website);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
    @GetMapping("/website/delete/{id}")
    public ResponseEntity<Website> deleteWebsite(@PathVariable Integer id) throws Exception {
        websiteService.deleteWebsite(id);
        return new ResponseEntity<Website>(HttpStatus.OK);
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
