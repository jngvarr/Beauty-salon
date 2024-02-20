package ru.gb.web_shop_with_ap_gateaway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("storage", r -> r.path("/storage/**")
						.uri("http://localhost:8081/"))
				.route("payment", r -> r.path("/payment/**")
						.uri("http://localhost:8082/")).build();
	}
}
