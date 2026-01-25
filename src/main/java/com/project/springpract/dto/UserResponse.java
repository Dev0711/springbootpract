package com.project.springpract.dto;

import com.project.springpract.entity.Address;
import com.project.springpract.entity.UserRole;
import com.project.springpract.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        String phoneNumber,
        UserRole role,
        UserStatus userStatus,
        List<Address> addresses
) {
}
