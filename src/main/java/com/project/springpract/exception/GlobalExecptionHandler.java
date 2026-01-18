package com.project.springpract.exception;

import com.project.springpract.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice

public class GlobalExecptionHandler  {
    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<ErrorResponseDTO> UserNotFoundException(UserNotFoundException ex) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO("NOT_FOUND", 404, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDTO);

    }


}
