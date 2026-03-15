package com.springprat.auth_service.service.impl;

import com.springprat.auth_service.service.RefreshTokenService;
import com.springprat.auth_service.entity.RefreshToken;
import com.springprat.auth_service.exeption.InvalidTokenException;
import com.springprat.auth_service.exeption.TokenExpiredException;
import com.springprat.auth_service.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration; // in days

    @Override
    public RefreshToken createRefreshToken(UUID userId) {
        log.info("Creating refresh token for user: {}", userId);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .userId(userId)
                .expiryDate(LocalDateTime.now().plusDays(refreshTokenExpiration))
                .revoked(false)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyRefreshToken(String token) {
        log.info("Verifying refresh token");

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid refresh token"));

        if (refreshToken.getRevoked()) {
            throw new InvalidTokenException("Refresh token has been revoked");
        }

        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new TokenExpiredException("Refresh token has expired");
        }

        return refreshToken;
    }

    @Override
    public void revokeRefreshToken(String token) {
        log.info("Revoking refresh token");

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid refresh token"));

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void revokeAllUserTokens(UUID userId) {
        log.info("Revoking all tokens for user: {}", userId);
        refreshTokenRepository.revokeAllUserTokens(userId);
    }

    @Override
    public void deleteExpiredTokens() {
        log.info("Deleting expired refresh tokens");
        refreshTokenRepository.deleteExpiredTokens();
    }
}