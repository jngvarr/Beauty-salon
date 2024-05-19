package ru.jngvarr.clientmanagement.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.jngvarr.clientmanagement.auth.CustomAuthenticationProvider;
import ru.jngvarr.clientmanagement.auth.JwtRequestFilter;
import ru.jngvarr.clientmanagement.services.UserDetailsServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                                .requestMatchers("/users/registration").permitAll()
                                .requestMatchers("/users/login").permitAll()
//                .requestMatchers("/public/**").permitAll()
//                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
//                ).formLogin(form -> form
//                        .loginPage("/login")
//                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Основная схема кодирования паролей
        String idForEncode = "bcrypt";

        // Настройка SCryptPasswordEncoder с параметрами
        SCryptPasswordEncoder scryptPasswordEncoder = new SCryptPasswordEncoder(
                16384, 8, 1, 32, 64);
        // мапа схем кодирования паролей
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(idForEncode, new BCryptPasswordEncoder());
        encoders.put("scrypt", scryptPasswordEncoder);

        // Создание и возврат DelegatingPasswordEncoder
        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        CustomAuthenticationProvider authenticationProvider =
                new CustomAuthenticationProvider(userDetailsService, passwordEncoder());
        return new ProviderManager(List.of(authenticationProvider));
    }
}
