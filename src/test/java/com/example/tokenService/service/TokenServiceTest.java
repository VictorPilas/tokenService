package com.example.tokenService.service;

import com.example.tokenService.client.AuthClient;
import com.example.tokenService.model.ServiceMetricsResponse;
import com.example.tokenService.model.TokenEntry;
import com.example.tokenService.model.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TokenServiceTest {

    private AuthClient authClient;
    private TokenService tokenService;
    private String token;

    @BeforeEach
    void setup() {
        authClient = mock(AuthClient.class);
        tokenService = new TokenService(authClient);
        token = "XXXXXXXXXXXXX";
        
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void testGetTokenResponse_FirstTime_Success() throws Exception {
        when(authClient.getToken()).thenReturn(token);

        TokenResponse response = tokenService.getTokenRespose();

        assertNotNull(response);
        assertEquals(token, response.getToken());
        assertTrue(response.getDate().matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z"));
        assertEquals(1, tokenService.getStoredTokensCount());
        assertEquals(1, tokenService.getCreatedTokens());
        assertEquals(1, tokenService.getRequestCount());
        
        verify(authClient, times(1)).getToken();
    }

    @Test
    void testGetTokenResponse_ExistingToken_NoNewCall() throws Exception {

        when(authClient.getToken()).thenReturn(token);
        tokenService.getTokenRespose();
        
        TokenResponse secondResponse = tokenService.getTokenRespose();

        assertNotNull(secondResponse);
        assertEquals(token, secondResponse.getToken());
        assertEquals(2, tokenService.getRequestCount());
        assertEquals(1, tokenService.getCreatedTokens());
        assertEquals(1, tokenService.getStoredTokensCount());

        verify(authClient, times(1)).getToken();
    }

    @Test
    void testGetTokenResponse_InvalidToken_ThrowsException() throws Exception {
        when(authClient.getToken()).thenReturn("");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            tokenService.getTokenRespose();
        });

        assertTrue(exception.getMessage().contains("Invalid Token"));
        assertEquals(1, tokenService.getRequestCount());
        assertEquals(0, tokenService.getCreatedTokens());
        assertEquals(0, tokenService.getStoredTokensCount());

        verify(authClient, times(1)).getToken();
    }

    @Test
    void testGetTokenDate_FormatCorrect() {
        String date = tokenService.getTokenDate();
        assertTrue(date.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z"));
    }

    @Test
    void testGetServiceMetricsResponse_CorrectValues() throws Exception {
        when(authClient.getToken()).thenReturn(token);
        
        tokenService.getTokenRespose();
        tokenService.getTokenRespose();

        ServiceMetricsResponse metrics = tokenService.getServiceMetricsResponse();

        assertEquals(2, metrics.getRequestCounter());
        assertEquals(1, metrics.getCreatedTokens());
        assertEquals(1, metrics.getStoredTokens());
    }

    @Test
    void testTokenExpiry_NotExpired() throws Exception {
        when(authClient.getToken()).thenReturn(token);

        tokenService.getTokenRespose();

        Map<String, TokenEntry> tokens = tokenService.getAllTokens();
        TokenEntry entry = tokens.get("testUser");

        assertNotNull(entry);
        assertTrue(entry.getExpiryTime().isAfter(Instant.now()));
    }
}
