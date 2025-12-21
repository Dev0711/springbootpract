package com.project.springpract.controller;

import com.project.springpract.entity.User;
import com.project.springpract.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public User createUser(@RequestBody User user){
        userService.createUser(user);
        return user;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser (@RequestBody Long id){
        User response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }



}
