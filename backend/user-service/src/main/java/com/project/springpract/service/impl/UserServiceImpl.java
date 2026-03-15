package com.project.springpract.service.impl;

import com.project.springpract.dto.UserRequest;
import com.project.springpract.dto.UserResponse;
import com.project.springpract.dto.UserUpdateRequest;
import com.project.springpract.entity.User;
import com.project.springpract.exception.UserAlreadyExistsException;
import com.project.springpract.exception.UserNotFoundException;
import com.project.springpract.mapper.UserMapper;
import com.project.springpract.repository.UserRespository;
import com.project.springpract.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.cfg.defs.UUIDDef;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if(userRespository.existsByEmail(userRequest.email())){
            throw new UserAlreadyExistsException(userRequest.email()+"Email Already Exists");
        }
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

    public UserResponse getUserByEmail(String email){
        log.info("Fetching user with email: {}", email);
        User user = userRespository.findByEmail(email).orElseThrow(()-> new UserNotFoundException("User not found with email: " + email));
        return userMapper.toUserResponse(user);
        }
//    public UserResponse updateUser(UserRequest userRequest){
//
//    }

    public UserResponse updateUser(UUID id, UserUpdateRequest userRequest){
        log.info("updating the user with ID:{}",id);
        User user = userRespository.findById(id).orElseThrow(()-> new UserNotFoundException("User not found"));
        userMapper.updateEntityFromRequest(user, userRequest);
        User updateduser = userRespository.save(user);
        return userMapper.toUserResponse(updateduser);

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

    @Override
    @Transactional(readOnly = true)
    public boolean existByEmail(String email){
        return userRespository.existsByEmail(email);
    }


    @Override
    public Page<UserResponse> getAllUsersPaginated(Pageable pageable) {
        return userRespository.findAll(pageable).map(userMapper::toUserResponse);
    }

    public Page<UserResponse> searchUser(String keyword, Pageable pageable) {
        return userRespository.searchUsers(keyword, pageable).map(userMapper::toUserResponse);
    }
}
