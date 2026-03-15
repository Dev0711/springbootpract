package com.springprat.auth_service.repository;

import com.springprat.auth_service.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, UUID> {

    Optional<UserCredential> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<UserCredential> findByPasswordResetToken(String token);
}