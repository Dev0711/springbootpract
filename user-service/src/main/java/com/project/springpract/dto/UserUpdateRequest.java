package com.project.springpract.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record UserUpdateRequest(
         String name,
         @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
         String phoneNumber,
         @Email(message = "please enter valid email")
         String email

) {


}
