package com.project.springpract.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorResponseDTO {
    private String error;
    private int statusCode;
    private String message;
    private LocalDateTime Timestamp;

    public ErrorResponseDTO(String error, int statusCode, String message) {
        this.error = error;
        this.statusCode = statusCode;
        this.message = message;
        this.Timestamp = LocalDateTime.now();
    }



}
