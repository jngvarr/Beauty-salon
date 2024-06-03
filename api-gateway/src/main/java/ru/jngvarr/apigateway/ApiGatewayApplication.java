package ru.jngvarr.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

@SpringBootApplication
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("clients", r -> r.path("/api/clients/**")
                        .uri("http://localhost:8081/"))
                .route("users", r -> r.path("/api/users/**")
                        .uri("http://localhost:8089/"))
                .route("services", r -> r.path("/api/services/**")
                        .uri("http://localhost:8082/"))
                .route("storage", r -> r.path("/api/storage/**")
                        .uri("http://localhost:8083/"))
                .route("storage", r -> r.path("/api/staff/**")
                        .uri("http://localhost:8084/"))
                .route("appointment", r -> r.path("/api/visits/**")
                        .uri("http://localhost:8085/"))
                .route("eserver", r -> r.path("/eureka/**")
                        .uri("http://localhost:8761/")).build();
    }
}
