package com.springprat.auth_service.service.impl;


import com.springprat.auth_service.client.UserServiceClient;
import com.springprat.auth_service.service.AuthService;
import com.springprat.auth_service.service.RefreshTokenService;
import com.springprat.auth_service.dto.*;
import com.springprat.auth_service.entity.RefreshToken;
import com.springprat.auth_service.entity.UserCredential;
import com.springprat.auth_service.exeption.InvalidCredentialsException;
import com.springprat.auth_service.exeption.UserAlreadyExistsException;
import com.springprat.auth_service.repository.UserCredentialRepository;
import com.springprat.auth_service.util.JwtUtil;
import com.springprat.auth_service.util.PasswordResetTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserCredentialRepository userCredentialRepository;
    private final UserServiceClient userServiceClient;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final PasswordResetTokenUtil passwordResetTokenUtil;

    @Override
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        // Check if user already exists in Auth Service
        if (userCredentialRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User already exists with email: " + request.getEmail());
        }

        // Create user credential
        UserCredential userCredential = UserCredential.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("CUSTOMER")
                .isActive(true)
                .isEmailVerified(false)
                .build();

        UserCredential savedCredential = userCredentialRepository.save(userCredential);

        // Create user in User Service
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .id(savedCredential.getId())
                .email(request.getEmail())
                .name(request.getFirstName())
                .phoneNumber(request.getPhoneNumber())
                .role("CUSTOMER")
                .build();

        try {
            UserResponse userResponse = userServiceClient.createUser(createUserRequest).getData();

            // Generate tokens
            String accessToken = jwtUtil.generateAccessToken(
                    savedCredential.getEmail(),
                    savedCredential.getId(),
                    savedCredential.getRole()
            );

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(savedCredential.getId());

            log.info("User registered successfully: {}", savedCredential.getEmail());

            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken.getToken())
                    .tokenType("Bearer")
                    .expiresIn(jwtUtil.getAccessTokenExpirationInSeconds())
                    .user(userResponse)
                    .build();

        } catch (Exception e) {
            // Rollback: delete credential if user service creation fails
            // Use deleteById to avoid OptimisticLockingFailureException
            userCredentialRepository.deleteById(savedCredential.getId());
            log.error("Failed to create user in User Service: {}", e.getMessage());
            throw new RuntimeException("Failed to create user. Please try again.");
        }
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        // Find user credential
        UserCredential userCredential = userCredentialRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException());

        // Check if account is locked
        if (userCredential.getAccountLockedUntil() != null &&
                LocalDateTime.now().isBefore(userCredential.getAccountLockedUntil())) {
            throw new InvalidCredentialsException("Account is locked. Please try again later.");
        }

        // Check if account is active
        if (!userCredential.getIsActive()) {
            throw new InvalidCredentialsException("Account is inactive. Please contact support.");
        }

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), userCredential.getPassword())) {
            // Increment failed login attempts
            userCredential.setFailedLoginAttempts(userCredential.getFailedLoginAttempts() + 1);

            // Lock account after 5 failed attempts
            if (userCredential.getFailedLoginAttempts() >= 5) {
                userCredential.setAccountLockedUntil(LocalDateTime.now().plusMinutes(30));
                userCredentialRepository.save(userCredential);
                throw new InvalidCredentialsException("Account locked due to multiple failed login attempts. Try again in 30 minutes.");
            }

            userCredentialRepository.save(userCredential);
            throw new InvalidCredentialsException();
        }

        // Reset failed login attempts on successful login
        userCredential.setFailedLoginAttempts(0);
        userCredential.setAccountLockedUntil(null);
        userCredential.setLastLogin(LocalDateTime.now());
        userCredentialRepository.save(userCredential);

        // Get user details from User Service
        UserResponse userResponse = userServiceClient.getUserByEmail(request.getEmail()).getData();

        // Generate tokens
        String accessToken = jwtUtil.generateAccessToken(
                userCredential.getEmail(),
                userCredential.getId(),
                userCredential.getRole()
        );

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userCredential.getId());

        log.info("User logged in successfully: {}", userCredential.getEmail());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .expiresIn(jwtUtil.getAccessTokenExpirationInSeconds())
                .user(userResponse)
                .build();
    }

    @Override
    public TokenResponse refreshToken(String refreshTokenString) {
        log.info("Refreshing access token");

        // Verify refresh token
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(refreshTokenString);

        // Get user credential
        UserCredential userCredential = userCredentialRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        // Generate new access token
        String newAccessToken = jwtUtil.generateAccessToken(
                userCredential.getEmail(),
                userCredential.getId(),
                userCredential.getRole()
        );

        // Optionally rotate refresh token (create new one and revoke old)
        refreshTokenService.revokeRefreshToken(refreshTokenString);
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(userCredential.getId());

        log.info("Token refreshed successfully for user: {}", userCredential.getEmail());

        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .tokenType("Bearer")
                .expiresIn(jwtUtil.getAccessTokenExpirationInSeconds())
                .build();
    }

    @Override
    public void logout(String refreshToken) {
        log.info("Logging out user");
        refreshTokenService.revokeRefreshToken(refreshToken);
        log.info("User logged out successfully");
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        log.info("Password reset requested for email: {}", request.getEmail());

        UserCredential userCredential = userCredentialRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        // Generate password reset token
        String resetToken = passwordResetTokenUtil.generateToken();

        userCredential.setPasswordResetToken(resetToken);
        userCredential.setPasswordResetTokenExpiry(LocalDateTime.now().plusHours(1));

        userCredentialRepository.save(userCredential);

        // TODO: Send email with reset link
        log.info("Password reset token generated: {}", resetToken);
        log.info("In production, send email to: {} with reset link", request.getEmail());
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        log.info("Resetting password");

        UserCredential userCredential = userCredentialRepository.findByPasswordResetToken(request.getToken())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid or expired reset token"));

        // Check if token is expired
        if (userCredential.getPasswordResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new InvalidCredentialsException("Reset token has expired");
        }

        // Update password
        userCredential.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userCredential.setPasswordResetToken(null);
        userCredential.setPasswordResetTokenExpiry(null);

        userCredentialRepository.save(userCredential);

        // Revoke all refresh tokens for security
        refreshTokenService.revokeAllUserTokens(userCredential.getId());

        log.info("Password reset successfully for user: {}", userCredential.getEmail());
    }
}