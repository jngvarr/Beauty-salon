package ru.jngvarr.authservice.repositories;

import dao.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;


@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByToken(String token);

//    @Transactional
    void deleteTokenByToken(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiryDate <= :now")
    void deleteAllExpiredSince(@Param("now") Instant now);
}
