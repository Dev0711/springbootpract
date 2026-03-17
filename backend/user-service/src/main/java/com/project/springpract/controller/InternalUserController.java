package com.project.springpract.controller;

import com.project.springpract.dto.InternalCreateUserRequest;
import com.project.springpract.dto.UserResponse;
import com.project.springpract.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/internal")
public class InternalUserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody InternalCreateUserRequest request) {
        UserResponse response = userService.createUserInternal(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                    "success", true,
                    "message", "User created internally",
                    "data", response
                ));
    }

    @GetMapping("/email/{email}/exists")
    public ResponseEntity<Map<String, Object>> checkEmailExists(@PathVariable String email) {
        boolean exists = userService.existByEmail(email);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "data", exists
        ));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Map<String, Object>> getUserByEmail(@PathVariable String email) {
        UserResponse response = userService.getUserByEmail(email);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "data", response
        ));
    }
}
