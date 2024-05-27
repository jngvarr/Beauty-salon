package ru.jngvarr.authservice.services;

import dao.entities.RefreshToken;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.jngvarr.authservice.repositories.RefreshTokenRepository;
import security.JwtUtil;
import security.UserDetailsServiceImpl;
import security.repositories.UserRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository tokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    public void saveRefreshToken(String refreshToken, Claims claims) {
        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.setToken(refreshToken);
        newRefreshToken.setExpiryDate(convertToLocalDateTime(jwtUtil.extractExpiration(claims)));
        refreshTokenRepository.save(newRefreshToken);
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
    public RefreshToken findByToken(String token){
        return tokenRepository.findByToken(token);
    }
}
