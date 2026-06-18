package com.alabbas.store.repository;

import com.alabbas.store.entity.RevokedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface RevokedTokenRepository extends JpaRepository<RevokedToken, Long> {

    boolean existsByTokenHash(String tokenHash);

    long deleteByExpiresAtBefore(Instant now);
}
