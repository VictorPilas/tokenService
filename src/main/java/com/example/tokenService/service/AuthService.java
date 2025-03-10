package com.example.tokenService.service;

import com.example.tokenService.model.TokenResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Service
public class AuthService {

    private final RestTemplate restTemplate;
    private final String authUrl = "http://host.docker.internal:8080/token";

    public AuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public TokenResponse getTokenRespose() throws Exception {
        return new TokenResponse(getToken(),getTokenDate());
    }

    public String getTokenDate(){
        return new SimpleDateFormat("MMMM dd, yyyy", new Locale("en")).format(new Date());
    }

    public String getToken() throws Exception {
        try {
            JSONObject json = new JSONObject();
            json.put("username", "auth-vivelibre");
            json.put("password", "password");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(json.toString(), headers);

            String jsonResponse = restTemplate.postForObject(authUrl, request, String.class);
            if (jsonResponse == null || jsonResponse.isEmpty()) {
                throw new Exception("Empty response from authentication server");
            }

            JSONObject responseJson = new JSONObject(jsonResponse);
            return responseJson.getString("token");
        } catch (RestClientException e) {
            throw new Exception("Error while calling authentication server", e);
        } catch (JSONException e) {
            throw new Exception("Error parsing authentication response", e);
        }
    }


}
