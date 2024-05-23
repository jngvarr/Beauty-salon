package ru.jngvarr.authservice.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.x509.X509AuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import ru.jngvarr.authservice.services.CustomAuthenticationProvider;
import ru.jngvarr.authservice.services.UserDetailsServiceImpl;
import security.provider.JwtAuthFilter;
import security.provider.JwtCandidateAuthenticationProvider;

    @Configuration
    @RequiredArgsConstructor
    @EnableMethodSecurity
    @EnableWebSecurity
    @Log4j2
    @ComponentScan(basePackages = "security")
    public class RestSecurityConfiguration {
        private final UserDetailsServiceImpl userDetailsService;
        private static final String[] AUTH_WHITELIST = {
                "/error",
                "/actuator/**",
                "/users/registration",
                "/users/login"
        };
        private static final RequestMatcher WHITELIST_MATCHER = new OrRequestMatcher(
                Arrays.stream(AUTH_WHITELIST)
                        .map(AntPathRequestMatcher::new)
                        .collect(Collectors.toList())
        );
        private static final RequestMatcher PROTECTED_MATCHER = new NegatedRequestMatcher(WHITELIST_MATCHER);

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

            http
                    .authorizeHttpRequests(matcherRegistry -> matcherRegistry.requestMatchers(WHITELIST_MATCHER).permitAll())
                    .authorizeHttpRequests(matcherRegistry -> matcherRegistry.anyRequest().authenticated());

            http
                    .authenticationProvider(authenticationProvider())
                    .addFilterBefore(authenticationFilter(authenticationManager), X509AuthenticationFilter.class);

            http
                    .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .headers(customizer -> customizer
                            .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
                            .cacheControl(Customizer.withDefaults()))
                    .csrf(AbstractHttpConfigurer::disable)
                    .formLogin(AbstractHttpConfigurer::disable)
                    .httpBasic(AbstractHttpConfigurer::disable)
                    .logout(AbstractHttpConfigurer::disable);

            return http.build();
        }

        private JwtAuthFilter authenticationFilter(AuthenticationManager authenticationManager) {
            JwtAuthFilter result = new JwtAuthFilter(PROTECTED_MATCHER);
            result.setAuthenticationManager(authenticationManager);
            return result;
        }

        @Bean
        public AuthenticationProvider authenticationProvider() {
            return new JwtCandidateAuthenticationProvider();
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
