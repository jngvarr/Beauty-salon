package feign_clients;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Интерсептор запросов feign-клиентов
 */
@Configuration
public class JwtFeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getCredentials() instanceof String token) {
                template.header("Authorization", "Bearer " + token);
            }
        };
    }
}

