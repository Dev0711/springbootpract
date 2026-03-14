package com.project.springpract.service;

import com.project.springpract.dto.UserRequest;
import com.project.springpract.dto.UserResponse;
import com.project.springpract.entity.User;


import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponse createUser(UserRequest userRequest);

    User getUserById(UUID id);
    List<User> getAll();
    void deleteUser(UUID id);


}
