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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.jngvarr.authservice.dto.AuthenticationRequest;
import ru.jngvarr.authservice.dto.AuthenticationResponse;
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

    @PreAuthorize("hasAnyRole('TECH_ADMIN')")
    @GetMapping
    public List<User> getUsers() {
        log.debug("getUsers");
        return userService.getUsers();
    }
    @PreAuthorize("hasAnyRole('TECH_ADMIN')")
    @GetMapping("/tokens")
    public List<RefreshToken> getTokens() {
        return refreshTokenService.getTokenRepository().findAll();
    }

    @GetMapping("/byEmail/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

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

        User user = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        // Создание токена доступа
        String accessToken = jwtUtil.generateToken(user, true);
        // Создание токена обновления
        String refreshToken = jwtUtil.generateToken(user, false);
        Claims claims = jwtUtil.extractAllClaims(refreshToken);
        // Сохранение refresh token в базе данных
        RefreshToken rf = refreshTokenService.saveRefreshToken(refreshToken, claims);
        user.getTokens().add(rf);
        userService.updateUserToken(user);
        return new AuthenticationResponse(accessToken);
    }
    @PostMapping("/refresh")
    public AuthenticationResponse refreshAuthenticationToken(HttpServletRequest request) {
        String refreshToken = extractTokenFromCookie(request);
        Claims claims = jwtUtil.extractAllClaims(refreshToken);
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new RuntimeException("Refresh token is missing");
        }
        // Ищем токен в базе данных //TODO доделать реализацию
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

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/logout")
    public void logout(@RequestBody String request) {
        String username = jwtUtil.extractUsername(jwtUtil.extractAllClaims(request));
        userService.logoutByUsername(username);
//        public void logout (HttpServletRequest request){
//            String authHeader = request.getHeader("Authorization");
//            if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                String token = StringUtils.trimToNull(authHeader.substring(7));
//                String username = jwtUtil.extractUsername(jwtUtil.extractAllClaims(token));
//                userService.logoutByUsername(username);
//            } else throw new RuntimeException("Wrong request");
//        }
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

//    @Secured("ROLE_TECH_ADMIN") public void addRoleAdmin() {
//    }
}
