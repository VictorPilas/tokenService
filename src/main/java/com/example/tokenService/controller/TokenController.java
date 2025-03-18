package com.example.tokenService.controller;

import com.example.tokenService.exception.TokenGenerationException;
import com.example.tokenService.model.ServiceMetricsResponse;
import com.example.tokenService.model.TokenResponse;
import com.example.tokenService.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {


    private final TokenService tokenService;

    @GetMapping("/token")
    public ResponseEntity<TokenResponse> getToken() {
        try {
            return ResponseEntity.ok(tokenService.getTokenRespose());
        } catch (Exception e) {
            throw new TokenGenerationException("Error generating token", e);
        }
    }

    @GetMapping("/metrics")
    public ResponseEntity<ServiceMetricsResponse> getMetrics() {
        try {
            return ResponseEntity.ok(tokenService.getServiceMetricsResponse());
        } catch (Exception e) {
            throw new TokenGenerationException("Error getting metrics", e);
        }
    }
}
