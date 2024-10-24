package ru.jngvarr.authservice.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RefreshTokenResponse {
    private final String accessToken;
}