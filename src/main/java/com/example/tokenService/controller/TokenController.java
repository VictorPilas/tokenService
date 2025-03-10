package com.example.tokenService.controller;

import com.example.tokenService.exception.TokenGenerationException;
import com.example.tokenService.model.TokenResponse;
import com.example.tokenService.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/get-token")
public class TokenController {

    @Autowired
    private AuthService authService;

    public TokenController() {
    }

    @GetMapping()
    public ResponseEntity<TokenResponse> getToken() {
        try {
            return ResponseEntity.ok(authService.getTokenRespose());
        } catch (Exception e) {
            throw new TokenGenerationException("Error generating token", e);
        }
    }
}
