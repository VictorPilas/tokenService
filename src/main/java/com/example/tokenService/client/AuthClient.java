package com.example.tokenService.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthClient {

    @Value("${auth.service.url:http://localhost:8080/token}")
    private String authServiceUrl;

    public String getToken() throws Exception {
        try {
            RestTemplate restTemplate = new RestTemplate();
            JSONObject json = new JSONObject();
            json.put("username", "auth-vivelibre");
            json.put("password", "password");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(json.toString(), headers);

            String jsonResponse = restTemplate.postForObject(authServiceUrl, request, String.class);
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
