package ru.jngvarr.servicemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dao.Servize;

@Repository
public interface SalonServiceRepository extends JpaRepository<Servize,Long>{
}