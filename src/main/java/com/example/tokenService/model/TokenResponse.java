package com.example.tokenService.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class TokenResponse implements Serializable {

    @JsonProperty("auth-vivelibre-token")
    public final String token;

    public final String date;

    public TokenResponse(String token, String date) {
        this.token = token;
        this.date = date;
    }

    public String getToken() {
        return token;
    }

    public String getDate() {
        return date;
    }
}
