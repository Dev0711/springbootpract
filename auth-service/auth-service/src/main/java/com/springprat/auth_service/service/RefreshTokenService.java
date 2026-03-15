package com.springprat.auth_service.service;



import com.springprat.auth_service.entity.RefreshToken;

import java.util.UUID;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(UUID userId);

    RefreshToken verifyRefreshToken(String token);

    void revokeRefreshToken(String token);

    void revokeAllUserTokens(UUID userId);

    void deleteExpiredTokens();
}