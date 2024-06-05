package ru.jngvarr.authservice.services;

import dao.entities.RefreshToken;
import exceptions.NeededObjectNotFound;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.jngvarr.authservice.repositories.RefreshTokenRepository;
import security.JwtUtil;
import security.UserDetailsServiceImpl;
import security.repositories.UserRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Data
@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository tokenRepository;
    private final JwtUtil jwtUtil;

    public RefreshToken saveRefreshToken(String refreshToken, Claims claims) {
        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.setToken(refreshToken);
        newRefreshToken.setExpiryDate(convertToLocalDateTime(jwtUtil.extractExpiration(claims)));
        return tokenRepository.save(newRefreshToken);
    }

    @Transactional
    public void deleteRefreshToken(String refreshToken) {
        RefreshToken toDeleteRefreshToken = tokenRepository.findByToken(refreshToken);
        if (toDeleteRefreshToken != null) {
            tokenRepository.deleteTokenByToken(refreshToken);
        } else throw new NeededObjectNotFound("Token not found");
    }


    public boolean validateRefreshToken(Claims claims) {
        UserDetails userDetails = new UserDetailsServiceImpl(userRepository)
                .loadUserByUsername(jwtUtil.extractUsername(claims));
        return jwtUtil.validateToken(claims, userDetails);
    }

    private LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public RefreshToken findByToken(String token) {
        return tokenRepository.findByToken(token);
    }
    public RefreshToken findByAccessToken(String token) {

        return tokenRepository.findByToken(token);
    }

    @Scheduled(cron = "@daily") // Запускать ежедневно
    public void removeExpiredTokens() {
        tokenRepository.deleteAllExpiredSince(Instant.now());
    }
}
