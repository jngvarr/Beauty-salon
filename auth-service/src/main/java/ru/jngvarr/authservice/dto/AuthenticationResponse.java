package ru.jngvarr.authservice.dto;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private final String accessToken;
    private final String refreshToken;
}
