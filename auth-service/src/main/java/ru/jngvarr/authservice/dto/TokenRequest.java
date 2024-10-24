package ru.jngvarr.authservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRequest {
    private String refreshToken;
}
