package com.example.tokenService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.example.tokenService.controller.TokenController;
import com.example.tokenService.exception.TokenGenerationException;
import com.example.tokenService.model.TokenResponse;
import com.example.tokenService.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class TokenControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private TokenController tokenController;

    @Test
    void testGetTokenSuccess() throws Exception {
        TokenResponse mockResponse = new TokenResponse("mockToken", "February 21, 2018");
        when(authService.getTokenRespose()).thenReturn(mockResponse);

        ResponseEntity<TokenResponse> response = tokenController.getToken();
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("mockToken", response.getBody().getToken());
    }

    @Test
    void testGetTokenFailure() throws Exception {
        when(authService.getTokenRespose()).thenThrow(new RuntimeException("Service error"));
        Exception exception = assertThrows(TokenGenerationException.class, () -> tokenController.getToken());
        assertEquals("Error generating token", exception.getMessage());
    }
}

