package com.project.springpract.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public record UserRequest(
                         @NotBlank(message = "Please enter name") String name,
                          @NotBlank(message = "Please enter Email")
                          @Email(message = "Please enter valid email")
                          String email,
                            @NotBlank(message = "Please enter password")

                            String password,
                            @NotBlank(message = "Please enter phone number")
                            @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Please enter valid phone number")
                            String phoneNumber

                          ) {
}
