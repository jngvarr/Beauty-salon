package ru.jngvarr.staffmanagement.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"dao", "security", "rest"})
@EntityScan(basePackages = "dao.entities")
public class CoreConfig {
}
