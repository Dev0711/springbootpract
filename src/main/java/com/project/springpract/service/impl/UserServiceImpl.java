package com.project.springpract.service.impl;

import com.project.springpract.dto.UserRequest;
import com.project.springpract.dto.UserResponse;
import com.project.springpract.entity.User;
import com.project.springpract.exception.UserNotFoundException;
import com.project.springpract.mapper.UserMapper;
import com.project.springpract.repository.UserRespository;
import com.project.springpract.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.cfg.defs.UUIDDef;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j

public class UserServiceImpl implements UserService {

    private final UserRespository userRespository;
    private final UserMapper userMapper;
    @Override
    public UserResponse createUser(UserRequest userRequest){
//        User saveduser = userRespository.save(user);
//        return saveduser;
        User user = userMapper.toUserEntity(userRequest);
        User savedUser = userRespository.save(user);
        log.info("User created with ID: {}", savedUser.getId());
        return userMapper.toUserResponse(savedUser);
    }                                                                    

    public User getUserById(UUID id) {
        log.info("Fetching user with ID: {}", id);
        User user = userRespository.findById(id).orElseThrow(()-> new UserNotFoundException("User not found with id: " + id));
        return user;
    }


    @Override
    public List<User> getAll() {
        return userRespository.findAll();
    }

    @Override
    public void deleteUser(UUID id) {
        if (!userRespository.existsById(id)){
            throw new RuntimeException("user not found");
        }
        userRespository.deleteById(id);


    }
}
