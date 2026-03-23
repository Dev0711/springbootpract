package com.springpract.product_service.dto;

import lombok.Data;

@Data
public class ErrorResponseDTO {
    private String message;
    private String error;
    private String path;
    private String status;
    private String timestamp;
}
