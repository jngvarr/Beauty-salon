package ru.jngvarr.authservice.controllers;

import dao.entities.RefreshToken;
import dao.entities.people.User;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.jngvarr.authservice.dto.AuthenticationRequest;
import ru.jngvarr.authservice.dto.AuthenticationResponse;
import ru.jngvarr.authservice.dto.RefreshTokenRequest;
import ru.jngvarr.authservice.services.AuthService;
import ru.jngvarr.authservice.services.RefreshTokenService;
import ru.jngvarr.authservice.services.UserService;
import security.JwtUtil;
import security.UserDetailsServiceImpl;

import java.time.LocalDateTime;
import java.util.List;


@Log4j2
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final UserDetailsServiceImpl userDetailsService;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final AuthService authService;

    @GetMapping("/registration")
    public String getHello() {
        return "Привет от users";
    }

    @GetMapping
    public List<User> getUsers() {
        log.debug("getUsers");
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

        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        String accessToken = jwtUtil.generateToken(userDetails, true);

        // Создание куки для токена обновления
        String refreshToken = jwtUtil.generateToken(userDetails, false);
        Claims claims = jwtUtil.extractAllClaims(refreshToken);
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
        RefreshToken refreshTokenEntity = refreshTokenService.findByToken(refreshToken);
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

    @PostMapping("/logout")
    public void logout(@RequestBody RefreshTokenRequest request) {
        refreshTokenService.deleteRefreshToken(request.getRefreshToken());
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
