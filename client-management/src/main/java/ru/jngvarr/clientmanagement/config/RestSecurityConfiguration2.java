//package ru.jngvarr.clientmanagement.config;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.preauth.x509.X509AuthenticationFilter;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
//import org.springframework.security.web.util.matcher.OrRequestMatcher;
//import org.springframework.security.web.util.matcher.RequestMatcher;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import security.provider.JwtAuthFilter;
//import security.provider.JwtCandidateAuthenticationProvider;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Configuration
//@RequiredArgsConstructor
//@EnableMethodSecurity
//@EnableWebSecurity
//@Log4j2
//@ComponentScan(basePackages = "security")
//public class RestSecurityConfiguration {
//    private static final String[] AUTH_WHITELIST = {
//            "/error",
//            "/actuator/**",
//            "/clients/**",
//            "/clients/create"
//    };
//    private static final RequestMatcher WHITELIST_MATCHER = new OrRequestMatcher(
//            Arrays.stream(AUTH_WHITELIST)
//                    .map(AntPathRequestMatcher::new)
//                    .collect(Collectors.toList())
//    );
//    private static final RequestMatcher PROTECTED_MATCHER = new NegatedRequestMatcher(WHITELIST_MATCHER);
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
//
//        http
//                .authorizeHttpRequests(matcherRegistry -> matcherRegistry.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll())
//                .authorizeHttpRequests(matcherRegistry -> matcherRegistry.requestMatchers(WHITELIST_MATCHER).permitAll())
//                .authorizeHttpRequests(matcherRegistry -> matcherRegistry.anyRequest().authenticated());
//
//        http
//                .authenticationProvider(authenticationProvider())
//                .addFilterBefore(authenticationFilter(authenticationManager), X509AuthenticationFilter.class);
//
//        http
//                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .headers(customizer -> customizer
//                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
//                        .cacheControl(Customizer.withDefaults()))
//                .csrf(AbstractHttpConfigurer::disable)
////                .cors(c -> c.configurationSource(request -> {
////                    CorsConfiguration configuration = new CorsConfiguration();
////                    configuration.setAllowedOrigins(List.of("*"));
////                    configuration.addAllowedHeader("*");
////                    configuration.addAllowedMethod("*");
////                    return configuration;
////                }))
////                .cors(Customizer.withDefaults())
//                .formLogin(AbstractHttpConfigurer::disable)
//                .httpBasic(AbstractHttpConfigurer::disable)
//                .logout(AbstractHttpConfigurer::disable);
//
//        return http.build();
//    }
//
//    private JwtAuthFilter authenticationFilter(AuthenticationManager authenticationManager) {
//        JwtAuthFilter result = new JwtAuthFilter(PROTECTED_MATCHER);
//        result.setAuthenticationManager(authenticationManager);
//        return result;
//    }
//
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        return new JwtCandidateAuthenticationProvider();
//    }
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of("*"));
//        configuration.addAllowedHeader("*");
//        configuration.addAllowedMethod("*");
//        UrlBasedCorsConfigurationSource result = new UrlBasedCorsConfigurationSource();
//        result.registerCorsConfiguration("/**", configuration);
//        return result;
//    }
//}