package com.project.springpract.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class UserDTO {
    private UUID id;

    private String name;

    private String email;

    private String phoneNumber;

    private String address;

    private Integer age;
    private String gender;
    private String profilePictureUrl;
    private String timeZone;
    private String role;
    private String createdAt;
    private String updatedAt;

}