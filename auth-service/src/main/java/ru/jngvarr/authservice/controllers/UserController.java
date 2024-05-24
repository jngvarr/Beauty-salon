package ru.jngvarr.authservice.controllers;

import dao.entities.RefreshToken;
import dao.entities.people.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.jngvarr.authservice.dto.AuthenticationRequest;
import ru.jngvarr.authservice.dto.AuthenticationResponse;
import ru.jngvarr.authservice.repositories.RefreshTokenRepository;
import ru.jngvarr.authservice.repositories.UserRepository;
import ru.jngvarr.authservice.services.CustomAuthenticationProvider;
import ru.jngvarr.authservice.services.RefreshTokenService;
import ru.jngvarr.authservice.services.UserDetailsServiceImpl;
import ru.jngvarr.authservice.services.UserService;
import ru.jngvarr.authservice.security.JwtUtil;

import java.time.LocalDateTime;


@Data
@Log4j2
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final UserDetailsServiceImpl userDetailsService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository tokenRepository;
    //    private final AuthenticationManager authenticationManager;
    private final CustomAuthenticationProvider authenticationProvider;

    //    @GetMapping("/registration")
//    public String getUsers() {
//        return "Привет от users";
//    }
    @GetMapping("/byEmail/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/registration")
    public User userRegistration(@RequestBody User user) {
        log.debug("user registration, id: {} ", user.getId());
        return userService.createUser(user);
    }

    @PostMapping("/login")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
                                                            HttpServletResponse response) throws Exception {

        try {
            authenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String accessToken = jwtUtil.generateToken(userDetails, true);

        // Создание куки для токена обновления
        final String refreshToken = jwtUtil.generateToken(userDetails, false);

        // Сохранение refresh token в базе данных
        refreshTokenService.saveRefreshToken(refreshToken);

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
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new RuntimeException("Refresh token is missing");
        }
        // Ищем токен в базе данных
        RefreshToken refreshTokenEntity = tokenRepository.findByToken(refreshToken);
        if (refreshTokenEntity == null || refreshTokenEntity.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        if (refreshTokenService.validateRefreshToken(refreshToken)) {
            String username = jwtUtil.extractUsername(refreshToken);
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
