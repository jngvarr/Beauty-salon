package ru.jngvarr.authservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.jngvarr.authservice.dto.AuthenticationRequest;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public void authenticate(AuthenticationRequest authenticationRequest) throws AuthenticationException {
        //Ищем пользователя с указанным логином
        String requestUsername = authenticationRequest.getUsername();
        UserDetails userDetails;
        if (requestUsername != null) {
            userDetails = userDetailsService.loadUserByUsername(requestUsername);
        } else throw new BadCredentialsException("Пользователь с таким логином не найден");

        //Сравниваем пароли из запроса и пользователя из БД
        String requestPassword = authenticationRequest.getPassword();
        if (!passwordEncoder.matches(requestPassword, userDetails.getPassword())) {
            throw new BadCredentialsException("Пароль не подходит к учетной записи пользователя");
        }
    }
}
