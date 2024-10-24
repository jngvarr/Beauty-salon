package ru.jngvarr.authservice.controllers;

import dao.entities.RefreshToken;
import dao.entities.people.SalonUser;
import exceptions.InvalidRefreshTokenException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.jngvarr.authservice.dto.AuthenticationRequest;
import ru.jngvarr.authservice.dto.AuthenticationResponse;
import ru.jngvarr.authservice.dto.RefreshTokenResponse;
import ru.jngvarr.authservice.dto.TokenRequest;
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
public class UserController {
    private final UserDetailsServiceImpl userDetailsService;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final AuthService authService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public List<SalonUser> getUsers() {
        log.debug("/getUsers");
        return userService.getUsers();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/tokens")
    public List<RefreshToken> getTokens() {
        return refreshTokenService.getTokenRepository().findAll();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/byEmail/{email}")
    public SalonUser getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/byEmail/{contact}")
    public SalonUser getUserByContact(@PathVariable String contact) {
        return userService.getUserByEmail(contact);
    }

    @PostMapping("/registration")
    public SalonUser userRegistration(@RequestBody SalonUser salonUser) {
        log.debug("user registration, id: {} ", salonUser.getId());
        return userService.createUser(salonUser);
    }

    @PostMapping("/login")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
                                                            HttpServletResponse response) throws Exception {
        try {
            authService.authenticate(authenticationRequest);
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        SalonUser salonUser = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        // Создание токена доступа
        String accessToken = jwtUtil.generateToken(salonUser, true);
        // Создание токена обновления
        String refreshToken = jwtUtil.generateToken(salonUser, false);
        Claims claims = jwtUtil.extractAllClaims(refreshToken);
        // Сохранение refresh token в базе данных
        RefreshToken rf = refreshTokenService.saveRefreshToken(refreshToken, claims);
        salonUser.getTokens().add(rf);
        userService.updateUserToken(salonUser);
        return new AuthenticationResponse(accessToken, refreshToken);
    }

    @PostMapping("/refresh")
    public RefreshTokenResponse refreshAuthenticationToken(@RequestBody TokenRequest request) {
        String refreshToken = request.getRefreshToken();
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new InvalidRefreshTokenException("Refresh token is missing");
            // Ищем токен в базе данных
        }
        RefreshToken refreshTokenEntity = refreshTokenService.findByToken(refreshToken);
        if (refreshTokenEntity == null || refreshTokenEntity.isRevoked()) {
            throw new InvalidRefreshTokenException("Invalid or revoked refresh token");
        }
        Claims claims = jwtUtil.extractAllClaims(refreshTokenEntity.getToken());
        if (refreshTokenService.validateRefreshToken(claims)) {
            String username = jwtUtil.extractUsername(claims);
            UserDetails refreshedUser = userDetailsService.loadUserByUsername(username);
            String newAccessToken = jwtUtil.generateToken(refreshedUser, true);
            return new RefreshTokenResponse(newAccessToken);
        } else {
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/logout")
    public void logout(@RequestBody String request) {
        String username = jwtUtil.extractUsername(jwtUtil.extractAllClaims(request));
        userService.logoutByUsername(username);
    }

    public SalonUser setUserAuthority(Long id) {
        return null;
    }
}
