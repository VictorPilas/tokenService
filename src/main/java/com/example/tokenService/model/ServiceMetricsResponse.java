package com.example.tokenService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceMetricsResponse {
    Integer requestCounter;
    Integer storedTokens;
    Integer createdTokens;
}
