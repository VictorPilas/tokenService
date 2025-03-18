package com.example.tokenService.service;

import com.example.tokenService.client.AuthClient;
import com.example.tokenService.model.ServiceMetricsResponse;
import com.example.tokenService.model.TokenEntry;
import com.example.tokenService.model.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {

    @Autowired
    private final AuthClient authClient;

    private final Map<String, TokenEntry> tokenStore = new ConcurrentHashMap<>();
    private final Duration tokenExpiryDuration = Duration.ofMinutes(10);
    private final AtomicInteger requestCounter = new AtomicInteger(0);
    private final AtomicInteger createdTokens = new AtomicInteger(0);

    public TokenResponse getTokenRespose() throws Exception {

        requestCounter.incrementAndGet();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        TokenEntry existingToken = tokenStore.get(username);

        if (existingToken != null && existingToken.getExpiryTime().isAfter(Instant.now())) {
            log.info("Existing token for user: {}", username);
            return existingToken.getTokenResponse();
        }

        String token = authClient.getToken();
        if (token == null || token.isEmpty()) {
            log.warn("Invalid Token");
            throw new RuntimeException("Invalid Token");
        }

        String tokenDate = getTokenDate();

        TokenResponse response = new TokenResponse(token,tokenDate);

        Instant expiry = Instant.now().plus(tokenExpiryDuration);
        tokenStore.put(username, new TokenEntry(response, expiry));

        createdTokens.incrementAndGet();
        log.info("New token generated for user: {}", username);

        return response;
    }

    public String getTokenDate() {
        Instant now = Instant.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .withZone(ZoneOffset.UTC);
        return formatter.format(now);
    }

    public int getRequestCount() {
        return requestCounter.get();
    }

    public int getCreatedTokens() {
        return createdTokens.get();
    }

    public int getStoredTokensCount() {
        return tokenStore.size();
    }

    public Map<String, TokenEntry> getAllTokens() {
        return tokenStore;
    }

    public ServiceMetricsResponse  getServiceMetricsResponse(){
        return new ServiceMetricsResponse(getRequestCount(),getStoredTokensCount(),getCreatedTokens());
    }


}
