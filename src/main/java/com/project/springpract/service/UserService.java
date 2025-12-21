package com.project.springpract.service;

import com.project.springpract.entity.User;


import java.util.List;

public interface UserService {

    User createUser(User user);

    User getUserById(Long Id);
    List<User> getAll();
    void deleteUser(Long Id);


}
