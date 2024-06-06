package ru.jngvarr.authservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"ru.jngvarr.authservice.repositories"})
public class JpaConfig {

}
