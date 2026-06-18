package com.alabbas.store.service;

import com.alabbas.store.entity.RevokedToken;
import com.alabbas.store.repository.RevokedTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;

@Service
public class RevokedTokenService {

    private final RevokedTokenRepository revokedTokenRepository;

    public RevokedTokenService(RevokedTokenRepository revokedTokenRepository) {
        this.revokedTokenRepository = revokedTokenRepository;
    }

    @Transactional
    public void revoke(String token, Instant expiresAt) {
        revokedTokenRepository.deleteByExpiresAtBefore(Instant.now());

        String tokenHash = hashToken(token);
        if (revokedTokenRepository.existsByTokenHash(tokenHash)) {
            return;
        }

        revokedTokenRepository.save(
                RevokedToken.builder()
                        .tokenHash(tokenHash)
                        .expiresAt(expiresAt)
                        .build()
        );
    }

    public boolean isRevoked(String token) {
        return revokedTokenRepository.existsByTokenHash(hashToken(token));
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 is not available", ex);
        }
    }
}
