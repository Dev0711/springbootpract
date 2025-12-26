package com.project.springpract.service;

import com.project.springpract.entity.User;


import java.util.List;
import java.util.UUID;

public interface UserService {

    User createUser(User user);

    User getUserById(UUID id);
    List<User> getAll();
    void deleteUser(UUID id);


}
