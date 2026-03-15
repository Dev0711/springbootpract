package com.project.springpract.service;

import com.project.springpract.dto.UserRequest;
import com.project.springpract.dto.UserResponse;
import com.project.springpract.dto.UserUpdateRequest;
import com.project.springpract.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponse createUser(UserRequest userRequest);

    User getUserById(UUID id);
    List<User> getAll();
    void deleteUser(UUID id);
    UserResponse getUserByEmail(String email);
    boolean existByEmail(String email);
    Page<UserResponse> getAllUsersPaginated(Pageable pageable);
    UserResponse updateUser(UUID id, UserUpdateRequest userRequest);


}
