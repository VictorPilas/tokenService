package com.example.tokenService.service;

import com.example.tokenService.model.TokenResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AuthService authService;

    private final String mockToken = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    private final String validResponse = new JSONObject().put("token", mockToken).toString();

    AuthServiceTest() throws JSONException {
    }

    @BeforeEach
    void setUp() {
        authService = new AuthService(restTemplate);
    }

    @Test
    void testGetToken_Success() throws Exception {
        when(restTemplate.postForObject(anyString(), any(), eq(String.class))).thenReturn(validResponse);

        String token = authService.getToken();

        assertEquals(mockToken, token);
    }

    @Test
    void testGetToken_EmptyResponse() {
        when(restTemplate.postForObject(anyString(), any(), eq(String.class))).thenReturn("");

        Exception exception = assertThrows(Exception.class, authService::getToken);
        assertTrue(exception.getMessage().contains("Empty response from authentication server"));
    }

    @Test
    void testGetToken_RestClientException() {
        when(restTemplate.postForObject(anyString(), any(), eq(String.class)))
                .thenThrow(new RestClientException("Server error"));

        Exception exception = assertThrows(Exception.class, authService::getToken);
        assertTrue(exception.getMessage().contains("Error while calling authentication server"));
    }

    @Test
    void testGetToken_JSONException() {
        when(restTemplate.postForObject(anyString(), any(), eq(String.class)))
                .thenReturn("invalid-json");

        Exception exception = assertThrows(Exception.class, authService::getToken);
        assertTrue(exception.getMessage().contains("Error parsing authentication response"));
    }

    @Test
    void testGetTokenResponse_Success() throws Exception {
        when(restTemplate.postForObject(anyString(), any(), eq(String.class))).thenReturn(validResponse);

        TokenResponse tokenResponse = authService.getTokenRespose();

        assertNotNull(tokenResponse);
        assertEquals(mockToken, tokenResponse.getToken());
    }

    @Test
    void testGetTokenDate() {
        String date = authService.getTokenDate();

        assertNotNull(date);
        assertTrue(date.matches("[A-Za-z]+ \\d{2}, \\d{4}"));
    }
}
