package ru.jngvarr.appointmentmanagement.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.jngvarr.appointmentmanagement.model.VisitData;

@Configuration
@EntityScan(basePackages = {"dao", "ru.jngvarr.appointmentmanagement.model"})
@EnableJpaRepositories(basePackages = {"ru.jngvarr.appointmentmanagement.repositories", "security.repositories"})
public class JpaConfig {

}
