package com.sunbase.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class AuthService {

    private static final String AUTH_URL = "https://qa.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp";

    public String authenticateAndGetToken() {
        RestTemplate restTemplate = new RestTemplate();

        // Create request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create request body
        Map<String, String> body = new HashMap<>();
        body.put("login_id", "test@sunbasedata.com");
        body.put("password", "Test@123");

        // Combines the header and body using HttpEntity
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);

        // Make the request and get the response
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                AUTH_URL,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
 
        // Extract the token from the response
        String token = parseTokenFromResponse(responseEntity.getBody());
        return token;
    }

    private String parseTokenFromResponse(String responseBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        
        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            return rootNode.path("access_token").asText();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to parse authentication response", e);
        }
    }
}
