package com.react.test.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.react.test.dto.StatementResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;

@Service
public class RemoteService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    ObjectMapper mapper;

    private static final String URL_TEMPLATE = "https://api.monobank.ua/personal/statement/%s/%s/%s";

    public List<StatementResponseDto> getRemoteUserStatement(String accountNumber, String from, String to, String token) {
        String url = String.format(URL_TEMPLATE, accountNumber, from, to);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Token", token);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<List> response = restTemplate.exchange(url, GET, entity, List.class);
        if(response == null || response.getBody() == null) {
            return null;
        }
        List<StatementResponseDto> result = null;
        try {
            String json = mapper.writeValueAsString(response.getBody());
            result = mapper.readValue(json, new TypeReference<List<StatementResponseDto>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
