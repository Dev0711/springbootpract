package com.project.springpract.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record InternalCreateUserRequest(
        UUID id,
        String email,
        String name,
        String phoneNumber,
        String role
) {
}
