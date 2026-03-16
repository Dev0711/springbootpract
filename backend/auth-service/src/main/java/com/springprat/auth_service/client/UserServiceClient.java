package com.springprat.auth_service.client;

import com.springprat.auth_service.dto.ApiResponse;
import com.springprat.auth_service.dto.CreateUserRequest;
import com.springprat.auth_service.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service", url = "${user-service.url}")
public interface UserServiceClient {

    @PostMapping("/api/users/internal/create")
    ApiResponse<UserResponse> createUser(@RequestBody CreateUserRequest request);

    @GetMapping("/api/users/internal/email/{email}/exists")
    ApiResponse<Boolean> checkEmailExists(@PathVariable String email);

    @GetMapping("/api/users/internal/email/{email}")
    ApiResponse<UserResponse> getUserByEmail(@PathVariable String email);
}