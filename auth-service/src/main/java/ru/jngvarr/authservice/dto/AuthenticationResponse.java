package ru.jngvarr.authservice.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class AuthenticationResponse {
    private final String accessToken;
}
