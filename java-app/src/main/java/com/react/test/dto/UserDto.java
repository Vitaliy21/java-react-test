package com.react.test.dto;

import java.util.LinkedHashMap;
import java.util.Map;

public class UserDto {
    private String id;
    private String username;
    private String token;
    private String password;
    private Map<String, CategoryType> categories = new LinkedHashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, CategoryType> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, CategoryType> categories) {
        this.categories = categories;
    }
}
