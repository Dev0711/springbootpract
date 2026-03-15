package com.project.springpract.repository;

import com.project.springpract.entity.User;
import com.project.springpract.entity.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRespository extends JpaRepository<User, UUID> {
    Optional<User> findByname(String username);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    List<User> findByRole(UserRole role);

    Page<User> findByRole(UserRole role, Pageable pageable);
    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<User> searchUsers(@Param("keyword") String keyword, Pageable pageable);


}