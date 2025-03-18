package com.example.tokenService.config;

import com.example.tokenService.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenCleaner {

    private final TokenService tokenService;

    @Scheduled(fixedRate = 60000)
    public void cleanExpiredTokens() {
        Instant now = Instant.now();

        tokenService.getAllTokens().entrySet().removeIf(entry -> {
            boolean expired = entry.getValue().getExpiryTime().isBefore(now);
            if (expired) {
                log.info("Token expired for user: {}", entry.getKey());
            }
            return expired;
        });
    }
}
