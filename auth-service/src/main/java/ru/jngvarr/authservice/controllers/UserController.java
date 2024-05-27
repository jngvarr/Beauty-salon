package ru.jngvarr.authservice.controllers;

import dao.entities.RefreshToken;
import dao.entities.people.User;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.jngvarr.authservice.dto.AuthenticationRequest;
import ru.jngvarr.authservice.dto.AuthenticationResponse;
import ru.jngvarr.authservice.repositories.RefreshTokenRepository;
import ru.jngvarr.authservice.services.AuthService;
import ru.jngvarr.authservice.services.RefreshTokenService;
import security.JwtUtil;
import security.UserDetailsServiceImpl;
import ru.jngvarr.authservice.services.UserService;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Log4j2
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final UserDetailsServiceImpl userDetailsService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository tokenRepository;
    private final AuthService authService;


    @GetMapping("/registration")
    public String getHello() {
        return "Привет от users";
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }


    @GetMapping("/byEmail/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    //    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/registration")
    public User userRegistration(@RequestBody User user) {
        log.debug("user registration, id: {} ", user.getId());
        return userService.createUser(user);
    }

    @PostMapping("/login")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
                                                            HttpServletResponse response) throws Exception {
        try {
            authService.authenticate(authenticationRequest);
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String accessToken = jwtUtil.generateToken(userDetails, true);

        // Создание куки для токена обновления
        final String refreshToken = jwtUtil.generateToken(userDetails, false);
        Claims claims = getJwtUtil().extractAllClaims(refreshToken);
        // Сохранение refresh token в базе данных
        refreshTokenService.saveRefreshToken(refreshToken, claims);

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true); // Установка флага secure для HTTPS
        response.addCookie(refreshTokenCookie);
        return new AuthenticationResponse(accessToken);
    }

    @PostMapping("/refresh")
    public AuthenticationResponse refreshAuthenticationToken(HttpServletRequest request) {
        String refreshToken = extractTokenFromCookie(request);
        Claims claims = jwtUtil.extractAllClaims(refreshToken);
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new RuntimeException("Refresh token is missing");
        }
        // Ищем токен в базе данных
        RefreshToken refreshTokenEntity = tokenRepository.findByToken(refreshToken);
        if (refreshTokenEntity == null || refreshTokenEntity.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        if (refreshTokenService.validateRefreshToken(claims)) {
            String username = jwtUtil.extractUsername(claims);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String newAccessToken = jwtUtil.generateToken(userDetails, true);
            return new AuthenticationResponse(newAccessToken);
        } else {
            throw new RuntimeException("Invalid refresh token");
        }
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
