package com.example.tokenService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.Instant;

@Data
@AllArgsConstructor
public class TokenEntry {
    private TokenResponse tokenResponse;
    private Instant expiryTime;
}
